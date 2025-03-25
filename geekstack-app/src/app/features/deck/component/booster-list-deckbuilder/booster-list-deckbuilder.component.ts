import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  HostListener,
  inject,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { CardUnionArena } from '../../../../core/model/card-unionarena.model'; // Import card model
import { CardOnePiece } from '../../../../core/model/card-onepiece.model';
import { CardDragonBallZFW } from '../../../../core/model/card-dragonballzfw.model';
import { CookieRunCard } from '../../../../core/model/card-cookierunbraverse.model';
import { FormsModule } from '@angular/forms';
import { Observable, Subject } from 'rxjs';
import { CardDeckService } from '../../../../core/service/card-deck.service';
import { TcgImageComponent } from '../../../../shared/component/tcg-image/tcg-image.component';
import { MatIconModule } from '@angular/material/icon';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';

@Component({
  selector: 'app-booster-list-deckbuilder',
  standalone: true,
  imports: [CommonModule, FormsModule, TcgImageComponent, MatIconModule],
  templateUrl: './booster-list-deckbuilder.component.html',
  styleUrls: ['./booster-list-deckbuilder.component.css'],
})
export class BoosterListDeckbuilderComponent implements OnInit {
  boosterList: Array<{
    pathname: string;
    alt: string;
    imageSrc: string;
    imgWidth: number;
  }> = [];
  filteredCards: Array<
    CardUnionArena | CardOnePiece | CardDragonBallZFW | CookieRunCard
  > = [];
  showBoosterList: boolean = true; // Flag to track whether booster list is visible
  @Input()
  isSmallScreen: boolean = false;
  tcgPath: string = '';
  cardList: Array<
    CardUnionArena | CardOnePiece | CardDragonBallZFW | CookieRunCard
  > = [];
  booster: string = '';
  colors: string[] = [];
  boosters: string[] = [];
  rarities: string[] = [];
  selectedColor = '';
  selectedBooster = '';
  selectedRarity = '';
  cardsInDeck$!: Observable<{ card: any; count: number }[]>;

  @Output()
  onCardListActive = new Subject<boolean>();

  private route = inject(ActivatedRoute);
  private cardDeckService = inject(CardDeckService);
  private geekstackService = inject(GeekstackService);
  constructor() {}

  ngOnInit(): void {
    this.cardsInDeck$ = this.cardDeckService.cardsInDeck$;
    this.route.paramMap.subscribe((params) => {
      this.tcgPath = params.get('tcg')?.replace('/', '') || '';
      this.booster = params.get('booster') || '';
      this.cardList = [];
      this.filteredCards = [];
      this.fetchBoosterList();
    });
  }

  isRemoveDisabled(card: any): boolean {
    const cardInDeck = this.cardDeckService.getCardCount(card);
    return cardInDeck === 0;
  }

  getCardCount(card: any): number {
    return this.cardDeckService.getCardCount(card);
  }

  addToDeck(card: any) {
    this.cardDeckService.addCard(card);
  }

  removeFromDeck(card: any) {
    this.cardDeckService.removeCard(card);
  }

  fetchFilterOptions(): void {
    if (!this.tcgPath || !this.booster) {
      console.log(this.tcgPath);
      console.log(this.booster);
      console.error('TCG path or booster is not set!');
      return;
    }

    this.geekstackService
      .getTcgSetFilter(this.tcgPath, 'color', this.booster)
      .subscribe({
        next: (colors) => {
          this.colors = colors || [];
        },
        error: (err) => {
          console.error('Failed to fetch colors:', err);
          this.colors = [];
        },
      });

    // Fetch boosters
    const boosterField =
      this.tcgPath === 'unionarena'
        ? 'booster'
        : this.tcgPath === 'onepiece'
        ? 'category'
        : this.tcgPath === 'dragonballzfw'
        ? 'cardtype'
        : '';

    this.geekstackService
      .getTcgSetFilter(this.tcgPath, boosterField, this.booster)
      .subscribe({
        next: (boosters) => {
          this.boosters = boosters || [];
        },
        error: (err) => {
          console.error('Failed to fetch boosters:', err);
          this.boosters = [];
        },
      });

    this.geekstackService
      .getTcgSetFilter(this.tcgPath, 'rarity', this.booster)
      .subscribe({
        next: (rarities) => {
          this.rarities = rarities || [];
        },
        error: (err) => {
          console.error('Failed to fetch rarities:', err);
          this.rarities = [];
        },
      });
  }

  fetchBoosterList(): void {
    this.geekstackService.getBoosterOfTcg(this.tcgPath).subscribe({
      next: (data) => {
        this.boosterList = data;
      },
      error: (err) => {
        console.error('Failed to fetch booster list:', err);
      },
    });
  }

  filterCards(): void {
    this.filteredCards = this.cardList.filter((card) => {
      if ('category' in card && card.category === 'leader') {
        return false;
      }

      if ('color' in card && 'booster' in card && 'rarity' in card) {
        return (
          (this.selectedColor ? card.color === this.selectedColor : true) &&
          (this.selectedBooster
            ? card.booster === this.selectedBooster
            : true) &&
          (this.selectedRarity ? card.rarity === this.selectedRarity : true)
        );
      }
      if ('energyType' in card && 'grade' in card) {
        return (
          (this.selectedColor
            ? card.energyType === this.selectedColor
            : true) &&
          (this.selectedBooster
            ? card.boostercode === this.selectedBooster
            : true) &&
          (this.selectedRarity ? card.rarity === this.selectedRarity : true)
        );
      }
      return true;
    });
  }

  fetchCardList(booster: string): void {
    this.geekstackService
      .getCardlistOfTcgOfBooster(this.tcgPath, booster)
      .subscribe({
        next: (response) => {
          this.cardList = response || [];
          this.fetchFilterOptions();
          this.filterCards();
        },
        error: (err) => {
          console.error('Failed to fetch card list:', err);
        },
        complete: () => {
          this.showBoosterList = false;
        },
      });
  }

  onBoosterClick(pathname: string): void {
    this.booster = pathname; // Update selected booster
    this.fetchCardList(pathname); // Fetch and display cards for the selected booster
  }

  onResetFilters(): void {
    this.selectedBooster = '';
    this.selectedColor = '';
    this.selectedRarity = '';
    this.filterCards();
  }
  onBackClick(): void {
    this.showBoosterList = true; // Show booster list again
    this.cardList = []; // Clear the card list
    this.booster = ''; // Reset selected booster
    this.onResetFilters();
  }

  onClickOutside() {
    this.onCardListActive.next(false);
  }
}
