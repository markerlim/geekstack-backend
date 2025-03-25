import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  HostListener,
  inject,
  Input,
  Output,
} from '@angular/core';
import { first, map, Subject } from 'rxjs';
import { CardDeckService } from '../../../../core/service/card-deck.service';
import { MatIconModule } from '@angular/material/icon';
import { UserStore } from '../../../../core/store/user.store';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';
import { CardUnionArena } from '../../../../core/model/card-unionarena.model';
import { TcgStore } from '../../../../core/store/ctcg.store';

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
  imageSrc: string = '/images/deckimage.jpg';

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
  energyCostMapForUA: Record<number, number> = {};

  userId: string = '';

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
    this.userId = this.userStore.getCurrentUser().userId;
    if (this.tcgStore.getCurrentTcg() == 'unionarena') {
      this.cardDeckService.cardsInDeck$.subscribe(() => {
        this.totalCount = this.cardDeckService.getTotalCount();
        this.colorCountForUA =
          this.cardDeckService.getColorCountForUnionArena();
        this.finalCountForUA =
          this.cardDeckService.getFinalCountForUnionArena();
        this.specialCountForUA =
          this.cardDeckService.getSpecialCountForUnionArena();
      });
      this.cardDeckService.cardsInDeck$.pipe(
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
        })
      ).subscribe((energyCostMap) => {
        this.energyCostMapForUA = energyCostMap; // Assign the result to the component property
      });
    }
  }

  getKeysFrom0To7(): number[] {
    return Array.from({ length: 8 }, (_, i) => i);
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
            console.log('Deck saved successfully:', response);
            alert('Deck saved successfully!');
          },
          error: (error) => {
            console.error('Error saving deck:', error);
            alert('Failed to save deck.');
          },
        });
    });

    this.deckname = 'New Deck';
    this.imageSrc = '/images/deckimage.jpg';
    this.clearDecklist();
  }

  toggleMenu(event: Event) {
    event.stopPropagation();
    this.isMenuOpen = !this.isMenuOpen;
  }

  clickDeckCover(event: any) {
    event.stopPropagation()
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
