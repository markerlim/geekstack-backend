import { CommonModule } from '@angular/common';
import { Component, HostListener, inject, Input, Output } from '@angular/core';
import { first, map, Subject, takeUntil } from 'rxjs';
import { CardDeckService } from '../../../../core/service/card-deck.service';
import { MatIconModule } from '@angular/material/icon';
import { UserStore } from '../../../../core/store/user.store';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';
import { CardUnionArena } from '../../../../core/model/card-unionarena.model';
import { TcgStore } from '../../../../core/store/ctcg.store';
import { CardOnePiece } from '../../../../core/model/card-onepiece.model';
import { CookieRunCard } from '../../../../core/model/card-cookierunbraverse.model';
import { CardDragonBallZFW } from '../../../../core/model/card-dragonballzfw.model';

@Component({
  selector: 'app-deckbuilder-actions',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './deckbuilder-actions.component.html',
  styleUrls: ['./deckbuilder-actions.component.css'],
})
export class DeckbuilderActionsComponent {
  isMenuOpen = false;

  @Input()
  imageSrc: string = '/images/gsdeckimage.jpg';

  @Input()
  isSmallScreen: boolean = false;

  @Input()
  tcg: string = '';

  @Input()
  deckname: string = 'Deck Name';

  @Input()
  deckuid: string = '';

  totalCount: number = 0;
  finalCountForUA: number = 0;
  colorCountForUA: number = 0;
  specialCountForUA: number = 0;
  costMap: Record<number, number> = {};

  userId: string = '';
  private destroy$ = new Subject<void>();

  @Output()
  onDeckcoverSelect = new Subject<boolean>();

  @Output()
  onDeckloadSelect = new Subject<boolean>();

  private cardDeckService = inject(CardDeckService);
  private geekstackService = inject(GeekstackService);
  private userStore = inject(UserStore);
  private tcgStore = inject(TcgStore);
  constructor() {}

  ngOnInit() {
    this.userStore.gsSqlUser$.subscribe({
      next: (res) => {
        this.userId = res?.userId || 'noUserId';
      },
    });
    this.tcgStore.currentTcg$.subscribe({
      next: (res) => {
        this.tcg = res;
      },
    });
    this.initializeDeckDetails();
  }

  private initializeDeckDetails(): void {
    this.destroy$.next();
    if (this.tcg === 'unionarena') {
      this.cardDeckService.cardsInDeck$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => {
          this.totalCount = this.cardDeckService.getTotalCount();
          this.colorCountForUA =
            this.cardDeckService.getColorCountForUnionArena();
          this.finalCountForUA =
            this.cardDeckService.getFinalCountForUnionArena();
          this.specialCountForUA =
            this.cardDeckService.getSpecialCountForUnionArena();
        });
      this.cardDeckService.cardsInDeck$
        .pipe(
          map((cardsInDeck) => {
            const energyCostMap: Record<number, number> = {}; // Store energy costs

            cardsInDeck.forEach((entry) => {
              const gameCard = this.cardDeckService.mapToGameCard(
                entry.card,
                'unionarena'
              ) as CardUnionArena;
              let energyCost = gameCard.energycost ?? 0;

              if (energyCost > 7) {
                energyCost = 7;
              }

              if (!energyCostMap[energyCost]) {
                energyCostMap[energyCost] = 0;
              }

              energyCostMap[energyCost] += entry.count;
            });
            return energyCostMap;
          }),
          takeUntil(this.destroy$)
        )
        .subscribe((energyCostMap) => {
          this.costMap = energyCostMap;
        });
    }

    if (this.tcg === 'onepiece') {
      this.cardDeckService.cardsInDeck$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => {
          this.totalCount = this.cardDeckService.getTotalCount();
        });
      this.cardDeckService.cardsInDeck$
        .pipe(
          map((cardsInDeck) => {
            const costMap: Record<number, number> = {}; // Store life costs

            cardsInDeck.forEach((entry) => {
              const gameCard = this.cardDeckService.mapToGameCard(
                entry.card,
                'onepiece'
              ) as CardOnePiece;
              const lifecost = parseInt(gameCard.lifecost) || 0;

              if (!costMap[lifecost]) {
                costMap[lifecost] = 0;
              }

              costMap[lifecost] += entry.count;
            });
            return costMap;
          }),
          takeUntil(this.destroy$)
        )
        .subscribe((costMap) => {
          this.costMap = costMap;
        });
    }

    if (this.tcg === 'cookierunbraverse') {
      this.cardDeckService.cardsInDeck$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => {
          this.totalCount = this.cardDeckService.getTotalCount();
        });
      this.cardDeckService.cardsInDeck$
        .pipe(
          map((cardsInDeck) => {
            const costMap: Record<number, number> = {};

            cardsInDeck.forEach((entry) => {
              const gameCard = this.cardDeckService.mapToGameCard(
                entry.card,
                'cookierunbraverse'
              ) as CookieRunCard;
              const level = parseInt(gameCard.cardLevelTitle) || 0;

              if (!costMap[level]) {
                costMap[level] = 0;
              }

              costMap[level] += entry.count;
            });
            return costMap;
          }),
          takeUntil(this.destroy$)
        )
        .subscribe((costMap) => {
          this.costMap = costMap;
        });
    }

    if (this.tcg === 'dragonballzfw') {
      this.cardDeckService.cardsInDeck$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => {
          this.totalCount = this.cardDeckService.getTotalCount();
        });
      this.cardDeckService.cardsInDeck$
        .pipe(
          map((cardsInDeck) => {
            const costMap: Record<number, number> = {};

            cardsInDeck.forEach((entry) => {
              const gameCard = this.cardDeckService.mapToGameCard(
                entry.card,
                'dragonballzfw'
              ) as CardDragonBallZFW;
              const cost = parseInt(gameCard.cost) || 0;

              if (!costMap[cost]) {
                costMap[cost] = 0;
              }

              costMap[cost] += entry.count;
            });
            return costMap;
          }),
          takeUntil(this.destroy$)
        )
        .subscribe((costMap) => {
          this.costMap = costMap;
        });
    }
  }

  getKeysFrom0To3(): number[] {
    return Array.from({ length: 4 }, (_, i) => i);
  }

  getKeysFrom0To7(): number[] {
    return Array.from({ length: 8 }, (_, i) => i);
  }

  getKeysFrom0To10(): number[] {
    return Array.from({ length: 11 }, (_, i) => i);
  }
  clickLoadDecklist(event: any) {
    event.stopPropagation();
    this.onDeckloadSelect.next(true);
  }

  clearDecklist() {
    this.cardDeckService.clearList();
  }

  saveDecklist() {
    console.log('Saving decklist...');
    if (this.userId === 'noUserId') {
      alert('User id failure, please refresh or relog page');
      return;
    }
    this.cardDeckService.cardsInDeck$.pipe(first()).subscribe((cardsInDeck) => {
      const decklistPayload = {
        deckname: this.deckname,
        deckcover: this.imageSrc,
        listofcards: cardsInDeck.map((item) => ({
          ...item.card,
          count: item.count,
        })),
      };
      this.geekstackService
        .userSaveDeck(this.userId, decklistPayload, this.tcg, this.deckuid)
        .subscribe({
          next: (response) => {
            const object = JSON.parse(JSON.stringify(response));
            const deckuid = object.deckuid;
            const deckObject = {
              deckuid: deckuid,
              deckcover: decklistPayload.deckcover,
              deckname: decklistPayload.deckname,
              listofcards: decklistPayload.listofcards,
            };
            this.userStore.saveDeck({ deck: deckObject, tcg: this.tcg });
            alert(object.message);
          },
          error: (error) => {
            console.error('Error saving deck:', error);
            alert('Failed to save deck.');
          },
        });
    });

    this.deckname = 'New Deck';
    this.imageSrc = '/images/gsdeckimage.jpg';
    this.clearDecklist();
  }

  toggleMenu(event: Event) {
    event.stopPropagation();
    this.isMenuOpen = !this.isMenuOpen;
  }

  clickDeckCover(event: any) {
    event.stopPropagation();
    this.onDeckcoverSelect.next(true);
  }

  onDeckNameChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.deckname = input.value;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick() {
    this.isMenuOpen = false;
  }
}
