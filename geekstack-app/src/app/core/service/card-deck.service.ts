import { inject, Injectable, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, Subject, takeUntil } from 'rxjs';
import { CardUnionArena } from '../model/card-unionarena.model';
import { CardOnePiece } from '../model/card-onepiece.model';
import { CardDragonBallZFW } from '../model/card-dragonballzfw.model';
import { CookieRunCard } from '../model/card-cookierunbraverse.model';
import { ListOfDecks } from '../model/listofdecks.model';
import { GeekstackService } from './geekstackdata.service';
import { GSMongoUser } from '../model/mongo-user.model';
import { UserStore } from '../store/user.store';
import { DuelmastersCard } from '../model/card-duelmaster.model';
import { HololiveCard } from '../model/card-hololive.model';
import { TCGTYPE } from '../utils/constants';

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard
  | DuelmastersCard
  | HololiveCard;

@Injectable({
  providedIn: 'root',
})
export class CardDeckService implements OnInit, OnDestroy {
  private cardsInDeckSubject = new BehaviorSubject<
    { card: GameCard; count: number }[]
  >([]);
  private decksInListSubject = new BehaviorSubject<ListOfDecks[]>([]);
  private destroy$ = new Subject<void>();

  cardsInDeck$ = this.cardsInDeckSubject.asObservable();
  decksOfUser$ = this.decksInListSubject.asObservable();

  userId: string = '';

  deckuid: string = '';
  deckcover: string = '';
  deckname: string = '';

  decks: ListOfDecks[] | null = [];
  private geekstackService = inject(GeekstackService);
  private userStore = inject(UserStore);
  constructor() {}

  ngOnInit() {
    this.userStore.gsSqlUser$.pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this.userId = res?.userId || ''; // Fallback to empty string if undefined
      },
      error: (err) => {
        console.error('Error loading user:', err);
        this.userId = ''; // Reset on error
      },
    });
  }
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private readonly MAX_CARD_COUNT = 4;

  addCard(card: GameCard) {
    console.log('Adding card:', card);
    const currentCards = this.cardsInDeckSubject.value;

    const currentTotalCount = currentCards
      .filter((item) => this.isSameCardId(item.card, card))
      .reduce((sum, item) => sum + item.count, 0);

    const existingCardIndex = currentCards.findIndex((item) =>
      this.isSameCard(item.card, card)
    );

    if (existingCardIndex > -1 && currentTotalCount < this.MAX_CARD_COUNT) {
      const updatedCards = [...currentCards];
      updatedCards[existingCardIndex] = {
        ...updatedCards[existingCardIndex],
        count: updatedCards[existingCardIndex].count + 1,
      };
      this.cardsInDeckSubject.next(updatedCards);
    } else if (currentTotalCount < this.MAX_CARD_COUNT) {
      this.cardsInDeckSubject.next([...currentCards, { card, count: 1 }]);
    }
  }

  removeCard(card: GameCard) {
    const currentCards = this.cardsInDeckSubject.value;
    const existingCardIndex = currentCards.findIndex((item) =>
      this.isSameCard(item.card, card)
    );

    if (existingCardIndex > -1) {
      const updatedCards = [...currentCards];
      if (updatedCards[existingCardIndex].count > 1) {
        updatedCards[existingCardIndex] = {
          ...updatedCards[existingCardIndex],
          count: updatedCards[existingCardIndex].count - 1,
        };
      } else {
        updatedCards.splice(existingCardIndex, 1);
      }
      this.cardsInDeckSubject.next(updatedCards);
    }
  }

  getTotalCount(): number {
    let count = 0;
    const currentCards = this.cardsInDeckSubject.value;
    currentCards.forEach((entry) => {
      count += entry.count;
    });
    return count;
  }

  getColorCountForUnionArena(): number {
    let count = 0;
    const currentCards = this.cardsInDeckSubject.value;

    currentCards.forEach((entry) => {
      const gameCard = this.mapToGameCard(
        entry.card,
        'unionarena'
      ) as CardUnionArena;
      if (gameCard.triggerState == 'color') {
        count += entry.count;
      }
    });

    return count;
  }

  getFinalCountForUnionArena(): number {
    let count = 0;
    const currentCards = this.cardsInDeckSubject.value;

    currentCards.forEach((entry) => {
      const gameCard = this.mapToGameCard(
        entry.card,
        'unionarena'
      ) as CardUnionArena;
      if (gameCard.triggerState == 'final') {
        count += entry.count;
      }
    });

    return count;
  }

  getSpecialCountForUnionArena(): number {
    let count = 0;
    const currentCards = this.cardsInDeckSubject.value;

    currentCards.forEach((entry) => {
      const gameCard = this.mapToGameCard(
        entry.card,
        'unionarena'
      ) as CardUnionArena;
      if (gameCard.triggerState == 'special') {
        count += entry.count;
      }
    });

    return count;
  }

  getCardCount(card: GameCard): number {
    const currentCards = this.cardsInDeckSubject.value;
    const foundCard = currentCards.find((item) =>
      this.isSameCard(item.card, card)
    );
    return foundCard?.count || 0;
  }

  clearList() {
    this.cardsInDeckSubject.next([]);
  }

  loadDeckFromList(
    decklist: (GameCard & { count: number })[],
    tcgType: string
  ) {
    console.log('CHECKING MAPPER:', decklist);

    const mappedDecklist = decklist.map((card) => ({
      card: this.mapToGameCard(card, tcgType),
      count: card.count ?? 1, // fallback if count somehow missing
    }));

    console.log('AFTER MAPPING:', mappedDecklist);

    this.cardsInDeckSubject.next(mappedDecklist);
  }

  setDeckDetails(deckuid: string, deckname: string, deckcover: string) {
    this.deckuid = deckuid;
    this.deckcover = deckcover;
    this.deckname = deckname;
  }

  getDeckDetails(): { deckuid: string; deckname: string; deckcover: string } {
    return {
      deckuid: this.deckuid,
      deckname: this.deckname,
      deckcover: this.deckcover,
    };
  }

  mapToGameCard(rawCard: any, tcgType: string): GameCard {
    if (!rawCard) return {} as GameCard;
    switch (tcgType) {
      case TCGTYPE.UNIONARENA:
        return rawCard as CardUnionArena;
      case TCGTYPE.ONEPIECE:
        return rawCard as CardOnePiece;
      case TCGTYPE.DRAGONBALLZFW:
        return rawCard as CardDragonBallZFW;
      case TCGTYPE.COOKIERUN:
        return rawCard as CookieRunCard;
      case TCGTYPE.DUELMASTERS:
        return rawCard as DuelmastersCard;
      case TCGTYPE.HOLOLIVE:
        return rawCard as HololiveCard;
      default:
        console.warn('Unknown card type:', rawCard);
        return rawCard as GameCard;
    }
  }

  private isSameCardId(card1: GameCard, card2: GameCard): boolean {
    return card1.cardId === card2.cardId;
  }

  private isSameCard(card1: GameCard, card2: GameCard): boolean {
    return card1.cardUid === card2.cardUid;
  }
}
