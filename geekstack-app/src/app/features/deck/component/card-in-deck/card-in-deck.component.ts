import { Component, inject, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { map, Observable } from 'rxjs';
import { CardUnionArena } from '../../../../core/model/card-unionarena.model';
import { CardOnePiece } from '../../../../core/model/card-onepiece.model';
import { CardDragonBallZFW } from '../../../../core/model/card-dragonballzfw.model';
import { CookieRunCard } from '../../../../core/model/card-cookierunbraverse.model';
import { CardDeckService } from '../../../../core/service/card-deck.service';
import { TcgImageComponent } from '../../../../shared/component/tcg-image/tcg-image.component';
import { MatIconModule } from '@angular/material/icon';

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard;

@Component({
  selector: 'app-card-in-deck',
  standalone: true,
  imports: [CommonModule, TcgImageComponent, MatIconModule],
  templateUrl: './card-in-deck.component.html',
  styleUrls: ['./card-in-deck.component.css'],
})
export class CardInDeckComponent {
  cardsInDeck$!: Observable<{ card: GameCard; count: number }[]>;

  private cardDeckService = inject(CardDeckService);
  constructor() {
    this.cardsInDeck$ = this.cardDeckService.cardsInDeck$.pipe(
      map((cards) => {
        const sortedCards = cards.slice().sort((a, b) => {
          if (this.isUnionArenaCard(a.card) && this.isUnionArenaCard(b.card)) {
            const categoryA = a.card.category || '';
            const categoryB = b.card.category || '';

            if (categoryA === categoryB) {
              const energyCostA = Number(a.card.energycost) || 0;
              const energyCostB = Number(b.card.energycost) || 0;
              return energyCostA - energyCostB;
            }

            return categoryA.localeCompare(categoryB);
          }

          if (this.isOnePieceCard(a.card) && this.isOnePieceCard(b.card)) {
            const costLifeA = a.card.costlife ?? '';
            const costLifeB = b.card.costlife ?? '';
            return costLifeA.localeCompare(costLifeB);
          }

          if (
            this.isDragonBallZFWCard(a.card) &&
            this.isDragonBallZFWCard(b.card)
          ) {
            const costA = a.card.cost ?? '';
            const costB = b.card.cost ?? '';
            return costA.localeCompare(costB);
          }

          if (this.isCookieRunCard(a.card) && this.isCookieRunCard(b.card)) {
            const cardLevelA = a.card.cardLevel ?? '';
            const cardLevelB = b.card.cardLevel ?? '';
            return cardLevelA.localeCompare(cardLevelB);
          }
          return a.card.cardUid
            .toString()
            .localeCompare(b.card.cardUid.toString());
        });
        console.log('Sorted Cards:', sortedCards); // Log the sorted data
        return sortedCards;
      })
    );
  }

  getCardImage(card: GameCard): string {
    if ('urlimage' in card) return card.urlimage;
    return '';
  }

  getCardName(card: GameCard): string {
    // Handle different card types
    if ('cardName' in card) return card.cardName;
    // Add additional checks for other card types
    return 'Unknown Card';
  }

  getCardCount(card: GameCard): number {
    return this.cardDeckService.getCardCount(card);
  }

  addToDeck(card: GameCard) {
    this.cardDeckService.addCard(card);
  }

  removeFromDeck(card: GameCard) {
    this.cardDeckService.removeCard(card);
  }

  protected isUnionArenaCard(card: GameCard): card is CardUnionArena {
    return (card as CardUnionArena).energycost !== undefined;
  }

  protected isOnePieceCard(card: GameCard): card is CardOnePiece {
    return (card as CardOnePiece).costlife !== undefined;
  }

  protected isDragonBallZFWCard(card: GameCard): card is CardDragonBallZFW {
    return (card as CardDragonBallZFW).cost !== undefined;
  }

  protected isCookieRunCard(card: GameCard): card is CookieRunCard {
    return (card as CookieRunCard).cardLevel !== undefined;
  }
}
