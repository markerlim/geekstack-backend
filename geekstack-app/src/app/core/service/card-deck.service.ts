import { inject, Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { CardUnionArena } from '../model/card-unionarena.model';
import { CardOnePiece } from '../model/card-onepiece.model';
import { CardDragonBallZFW } from '../model/card-dragonballzfw.model';
import { CookieRunCard } from '../model/card-cookierunbraverse.model';
import { ListOfDecks } from '../model/listofdecks.model';
import { GeekstackService } from './geekstackdata.service';
import { GSMongoUser } from '../model/mongo-user.model';
import { UserStore } from '../store/user.store';

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard;

@Injectable({
  providedIn: 'root',
})
export class CardDeckService {
  private cardsInDeckSubject = new BehaviorSubject<
    { card: GameCard; count: number }[]
  >([]);
  private decksInListSubject = new BehaviorSubject<ListOfDecks[]>([]);

  cardsInDeck$ = this.cardsInDeckSubject.asObservable();
  decksOfUser$ = this.decksInListSubject.asObservable();

  userId: string = '';

  deckuid: string = '';
  deckcover: string = '';
  deckname: string = '';

  decks: ListOfDecks[] | null = [];
  private geekstackService = inject(GeekstackService);
  private userStore = inject(UserStore);
  constructor() {
    this.userId = this.userStore.getCurrentUser().userId;
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
      const gameCard = this.mapToGameCard(entry.card, 'unionarena') as CardUnionArena;
      if (gameCard.triggerState == "color") {
        count += entry.count;
      }
    });
  
    return count;
  }

  getFinalCountForUnionArena(): number {
    let count = 0;
    const currentCards = this.cardsInDeckSubject.value;
  
    currentCards.forEach((entry) => {
      const gameCard = this.mapToGameCard(entry.card, 'unionarena') as CardUnionArena;
      if (gameCard.triggerState == "final") {
        count += entry.count;
      }
    });
  
    return count;
  }
  
  getSpecialCountForUnionArena(): number {
    let count = 0;
    const currentCards = this.cardsInDeckSubject.value;
  
    currentCards.forEach((entry) => {
      const gameCard = this.mapToGameCard(entry.card, 'unionarena') as CardUnionArena;
      if (gameCard.triggerState == "special") {
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


  loadDeckFromList(decklist: { card: GameCard; count: number }[], tcgType: string) {
    const mappedDecklist = decklist.map(entry => ({
      card:this.mapToGameCard(entry.card, tcgType), // Convert to correct type
      count: entry.count ?? 0, // Ensure count is always present
    }));
  
    console.log("Mapped Deck List:", mappedDecklist);
    this.cardsInDeckSubject.next(mappedDecklist);
  }
  
  loadListOfDeck( tcg: string) {
    const storedMongoUser = localStorage.getItem('mongoUser');
    
    if (storedMongoUser) {
      const mongoUser: GSMongoUser = JSON.parse(storedMongoUser);
    
      const deckFieldMap: Record<string, keyof GSMongoUser> = {
        "unionarena": "uadecks",
        "onepiece": "opdecks",
        "cookierunbraverse": "crbdecks",
        "dragonballzfw": "dbzfwdecks"
      };
    
      const selectedDecks = mongoUser[deckFieldMap[tcg]]; 
      if (Array.isArray(selectedDecks) && selectedDecks.length > 0) {
        console.log(`Using ${deckFieldMap[tcg]} from local storage:`, selectedDecks);
    
        const mappedDecks = selectedDecks.map(deck => ({
          ...deck,
          listofcards: deck.listofcards
            ? deck.listofcards.map((cardEntry: any) => ({
                card: this.mapToGameCard(cardEntry, tcg),
                count: cardEntry.count ?? 1
              }))
            : []
        }));
    
        console.log('Mapped decks:', mappedDecks);
        this.decksInListSubject.next(mappedDecks);
        return;
      }
    }
    
  
    this.geekstackService.userLoadListOfDeck(this.userId, tcg).subscribe({
      next: (response) => {
        if (response.length > 0) {
          const mappedDecks = response.map(deck => ({
            ...deck,
            listofcards: deck.listofcards
              ? deck.listofcards.map((cardEntry: any) => ({
                  card: this.mapToGameCard(cardEntry, tcg),
                  count: cardEntry.count ?? 1
                }))
              : []
          }));
  
          console.log('Fetched decks from API:', mappedDecks);
          this.decksInListSubject.next(mappedDecks);
          
          const updatedMongoUser = { ...JSON.parse(storedMongoUser || '{}'), uaDecks: mappedDecks };
          localStorage.setItem('mongoUser', JSON.stringify(updatedMongoUser));
        }
      },
      error: (error) => {
        console.error('Error loading decks:', error);
      },
    });
  }

  loadListOfDeckDirect(tcg: string): Promise<any[]> {
    return new Promise((resolve, reject) => {
      const storedMongoUser = localStorage.getItem('mongoUser');
      
      const deckFieldMap: Record<string, keyof GSMongoUser> = {
        "unionarena": "uadecks",
        "onepiece": "opdecks",
        "cookierunbraverse": "crbdecks",
        "dragonballzfw": "dbzfwdecks"
      };
      
      if (storedMongoUser) {
        const mongoUser: GSMongoUser = JSON.parse(storedMongoUser);
      
        const selectedDecks = mongoUser[deckFieldMap[tcg]]; 
        if (Array.isArray(selectedDecks) && selectedDecks.length > 0) {
          console.log(`Using ${deckFieldMap[tcg]} from local storage:`, selectedDecks);
      
          const mappedDecks = selectedDecks.map(deck => ({
            ...deck,
            listofcards: deck.listofcards
              ? deck.listofcards.map((cardEntry: any) => ({
                  card: this.mapToGameCard(cardEntry, tcg),
                  count: cardEntry.count ?? 1
                }))
              : []
          }));
      
          console.log('Mapped decks:', mappedDecks);
          resolve(mappedDecks); // Return the decks directly
          return;
        }
      }
  
      // If decks not found in local storage, fetch from API
      this.geekstackService.userLoadListOfDeck(this.userId, tcg).subscribe({
        next: (response) => {
          if (response.length > 0) {
            const mappedDecks = response.map(deck => ({
              ...deck,
              listofcards: deck.listofcards
                ? deck.listofcards.map((cardEntry: any) => ({
                    card: this.mapToGameCard(cardEntry, tcg),
                    count: cardEntry.count ?? 1
                  }))
                : []
            }));
    
            console.log('Fetched decks from API:', mappedDecks);
            
            // Update local storage if needed
            const updatedMongoUser = { ...JSON.parse(storedMongoUser || '{}'), [deckFieldMap[tcg]]: mappedDecks };
            localStorage.setItem('mongoUser', JSON.stringify(updatedMongoUser));
  
            resolve(mappedDecks); // Return the decks directly
          } else {
            reject('No decks found');
          }
        },
        error: (error) => {
          console.error('Error loading decks:', error);
          reject(error); // Reject in case of an error
        },
      });
    });
  }
  
  setDeckDetails(deckuid: string, deckname: string, deckcover: string){
    this.deckuid = deckuid
    this.deckcover = deckcover
    this.deckname = deckname
  }

  getDeckDetails() : {deckuid: string, deckname: string, deckcover: string}{
    return {deckuid: this.deckuid, deckname: this.deckname, deckcover: this.deckcover};
  }

  mapToGameCard(rawCard: any, tcgType: string): GameCard {
    if (!rawCard) return {} as GameCard;
    switch (tcgType) {
      case 'unionarena':
        return rawCard as CardUnionArena;
      case 'onepiece':
        return rawCard as CardOnePiece;
      case 'dragonballzfw':
        return rawCard as CardDragonBallZFW;
      case 'cookierunbraverse':
        return rawCard as CookieRunCard;
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
