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

  @Output()
  onCardListActive = new Subject<boolean>();

  private route = inject(ActivatedRoute);
  private cardDeckService = inject(CardDeckService);
  private geekstackService = inject(GeekstackService);
  constructor() {}

  ngOnInit(): void {
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

    this.colors = this.getUniqueColorsFromList();
    this.rarities = this.getUniqueRarityFromList();
    this.boosters = this.getUniqueBoosterFromList();
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

  getUniqueColorsFromList(): string[] {
    if (!this.cardList || this.cardList.length === 0) {
      return [];
    }

    return [
      ...new Set(
        this.cardList
          .map((card) => {
            if ('color' in card) {
              return card.color;
            } else if ('energyType' in card) {
              return card.energyType;
            }
            return undefined;
          })
          .filter((color): color is string => color !== undefined)
      ),
    ];
  }

  getUniqueRarityFromList(): string[] {
    if (!this.cardList || this.cardList.length === 0) {
      return [];
    }

    return [
      ...new Set(
        this.cardList
          .map((card) => {
            if ('rarity' in card) {
              return card.rarity;
            }
            return undefined;
          })
          .filter((rarity): rarity is string => rarity !== undefined)
      ),
    ];
  }

  getUniqueBoosterFromList(): string[] {
    if (!this.cardList || this.cardList.length === 0) {
      return [];
    }

    return [
      ...new Set(
        this.cardList
          .map((card) => {
            if (this.tcgPath === 'onepiece' && 'category' in card && card.category === 'leader') {
              return undefined;
            }
            if (this.tcgPath === 'dragonballzfw' && 'cardtype' in card && (card.cardtype === 'LEADER' || card.cardtype === 'LEADER | AWAKEN')) {
              return undefined;
            }
            switch (this.tcgPath) {
              case 'unionarena':
                return (card as CardUnionArena).booster;
              case 'onepiece':
                return (card as CardOnePiece).category;
              case 'dragonballzfw':
                return (card as CardDragonBallZFW).cardtype;
              default:
                return undefined;
            }
          })
          .filter(
            (item): item is string => item !== undefined
          )
      ),
    ];
  }

  filterCards(): void {
    this.filteredCards = this.cardList.filter((card) => {
      if ('category' in card && card.category === 'leader' || 'cardtype' in card && (card.cardtype === 'LEADER' || card.cardtype === 'LEADER | AWAKEN') ) {
        return false;
      }
      
      //selectedBooster is for category
      // One Piece specific filtering
      else if (this.tcgPath === 'onepiece') {
        const onePieceCard = card as {
          color?: string;
          rarity?: string;
          category?: string;
        };
        return (
          (!this.selectedColor || onePieceCard.color === this.selectedColor) &&
          (!this.selectedRarity ||
            onePieceCard.rarity === this.selectedRarity) &&
          (!this.selectedBooster ||
            onePieceCard.category === this.selectedBooster)
        );
      }
      //selectedBooster is for cardType
      // Dragonballzfw specific filtering
      else if (this.tcgPath === 'dragonballzfw') {
        const dragonballzfw = card as {
          color?: string;
          rarity?: string;
          cardtype?: string;
        };
        return (
          (!this.selectedColor || dragonballzfw.color === this.selectedColor) &&
          (!this.selectedRarity ||
            dragonballzfw.rarity === this.selectedRarity) &&
          (!this.selectedBooster ||
            dragonballzfw.cardtype === this.selectedBooster)
        );
      } else if ('color' in card && 'booster' in card && 'rarity' in card) {
        return (
          (this.selectedColor ? card.color === this.selectedColor : true) &&
          (this.selectedBooster
            ? card.booster === this.selectedBooster
            : true) &&
          (this.selectedRarity ? card.rarity === this.selectedRarity : true)
        );
      }

      else if ('energyType' in card && 'grade' in card) {
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
