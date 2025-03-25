import { Component, OnInit, ElementRef, ViewChild, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CardUnionArena } from '../../../../core/model/card-unionarena.model';
import { CardOnePiece } from '../../../../core/model/card-onepiece.model';
import { CardDragonBallZFW } from '../../../../core/model/card-dragonballzfw.model';
import { CookieRunCard } from '../../../../core/model/card-cookierunbraverse.model';
import { TcgImageComponent } from '../../../../shared/component/tcg-image/tcg-image.component';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';

@Component({
    selector: 'app-card-list-display',
    standalone: true,
    imports: [CommonModule, FormsModule, TcgImageComponent],
    templateUrl: './card-list-display.component.html',
    styleUrls: ['./card-list-display.component.css']
})
export class CardListDisplayComponent implements OnInit {
  @ViewChild('cardListContainer', { static: true })
  cardListContainer!: ElementRef;

  cardList: Array<
    CardUnionArena | CardOnePiece | CardDragonBallZFW | CookieRunCard
  > = [];
  tcgPath: string = '';
  booster: string = '';
  colors: string[] = [];
  boosters: string[] = [];
  rarities: string[] = [];
  selectedColor = '';
  selectedBooster = '';
  selectedRarity = '';
  filteredCards: Array<
    CardUnionArena | CardOnePiece | CardDragonBallZFW | CookieRunCard
  > = [];
  isLoading = false;

  private geekstackService =  inject(GeekstackService);
  private route = inject(ActivatedRoute);

  constructor() {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.cardList = [];
      this.filteredCards = [];

      this.tcgPath = params.get('tcg')?.replace('/', '') || '';
      this.booster = params.get('booster') || '';
      this.fetchFilterOptions();
      this.fetchCardList();
      console.log(this.filterCards);
    });
  }

  fetchFilterOptions(): void {
    if (!this.tcgPath || !this.booster) {
      console.error('TCG path or booster is not set!');
      return;
    }

    this.geekstackService
      .getTcgSetFilter(this.tcgPath, 'color', this.booster)
      .subscribe({
        next: (colors) => {
          console.log('Colors:', colors); // Debugging
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
          console.log('Boosters:', boosters); // Debugging
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
          console.log('Rarities:', rarities); // Debugging
          this.rarities = rarities || [];
        },
        error: (err) => {
          console.error('Failed to fetch rarities:', err);
          this.rarities = [];
        },
      });
  }

  fetchCardList(): void {
    if (this.isLoading) return;

    this.isLoading = true;

    this.geekstackService
      .getCardlistOfTcgOfBooster(this.tcgPath, this.booster)
      .subscribe({
        next: (response) => {
          // Replace the card list with the fetched data
          this.cardList = response || [];
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
}
