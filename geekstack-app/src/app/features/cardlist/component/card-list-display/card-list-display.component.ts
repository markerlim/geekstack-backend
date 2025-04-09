import {
  Component,
  OnInit,
  ElementRef,
  ViewChild,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CardUnionArena } from '../../../../core/model/card-unionarena.model';
import { CardOnePiece } from '../../../../core/model/card-onepiece.model';
import { CardDragonBallZFW } from '../../../../core/model/card-dragonballzfw.model';
import { CookieRunCard } from '../../../../core/model/card-cookierunbraverse.model';
import { TcgImageComponent } from '../../../../shared/component/tcg-image/tcg-image.component';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';
import { DuelmastersCard } from '../../../../core/model/card-duelmaster.model';
import { TCGTYPE } from '../../../../core/utils/constants';
import { HololiveCard } from '../../../../core/model/card-hololive.model';

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard
  | DuelmastersCard
  | HololiveCard;

@Component({
  selector: 'app-card-list-display',
  standalone: true,
  imports: [CommonModule, FormsModule, TcgImageComponent],
  templateUrl: './card-list-display.component.html',
  styleUrls: ['./card-list-display.component.css'],
})
export class CardListDisplayComponent implements OnInit {
  @ViewChild('cardListContainer', { static: true })
  cardListContainer!: ElementRef;

  cardList: Array<GameCard> = [];
  tcgPath: string = '';
  booster: string = '';
  colors: string[] = [];
  boosters: string[] = [];
  rarities: string[] = [];
  selectedColor = '';
  selectedBooster = '';
  selectedRarity = '';
  filteredCards: Array<GameCard> = [];
  isLoading = false;

  private geekstackService = inject(GeekstackService);
  private route = inject(ActivatedRoute);

  constructor() {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.cardList = [];
      this.filteredCards = [];

      this.tcgPath = params.get('tcg')?.replace('/', '') || '';
      this.booster = params.get('booster') || '';
      this.fetchCardList();
    });
  }

  fetchFilterOptions(): void {
    if (!this.tcgPath || !this.booster) {
      console.error('TCG path or booster is not set!');
      return;
    }
    this.colors = this.getUniqueColorsFromList();
    this.rarities = this.getUniqueRarityFromList();
    this.boosters = this.getUniqueBoosterFromList();
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
            if (
              this.tcgPath === 'onepiece' &&
              'category' in card &&
              card.category === 'leader'
            ) {
              return undefined;
            }
            if (
              this.tcgPath === 'dragonballzfw' &&
              'cardtype' in card &&
              (card.cardtype === 'LEADER' ||
                card.cardtype === 'LEADER | AWAKEN')
            ) {
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
          .filter((item): item is string => item !== undefined)
      ),
    ];
  }
  fetchCardList(): void {
    if (this.isLoading) return;

    this.isLoading = true;

    this.geekstackService
      .getCardlistOfTcgOfBooster(this.tcgPath, this.booster)
      .subscribe({
        next: (response) => {
          // Replace the card list with the fetched data
          console.log(response);
          this.cardList = response || [];
          this.fetchFilterOptions();
          this.filterCards(); // Apply filters
        },
        error: (err) => {
          console.error('Failed to fetch card list:', err);
        },
        complete: () => {
          this.isLoading = false;
        },
      });
  }

  resetFilter(): void {
    this.selectedBooster = '';
    this.selectedColor = '';
    this.selectedRarity = '';
    this.filterCards();
  }

  filterCards(): void {
    this.filteredCards = this.cardList.filter((card) => {
      if (
        ('category' in card && card.category === 'leader') ||
        ('cardtype' in card &&
          (card.cardtype === 'LEADER' || card.cardtype === 'LEADER | AWAKEN'))
      ) {
        return false;
      }

      //selectedBooster is for category
      // One Piece specific filtering
      else if (this.tcgPath === TCGTYPE.ONEPIECE) {
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
      else if (this.tcgPath === TCGTYPE.DRAGONBALLZFW) {
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
      } else if ('energyType' in card && 'grade' in card) {
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
}
