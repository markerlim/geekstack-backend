import {
  Component,
  Input,
  Output,
  inject,
  OnInit,
} from '@angular/core';
import { Subject } from 'rxjs';
import { CardUnionArena } from '../../../../core/model/card-unionarena.model';
import { CardOnePiece } from '../../../../core/model/card-onepiece.model';
import { CardDragonBallZFW } from '../../../../core/model/card-dragonballzfw.model';
import { CookieRunCard } from '../../../../core/model/card-cookierunbraverse.model';
import { CommonModule } from '@angular/common';
import { TcgImageComponent } from '../../../../shared/component/tcg-image/tcg-image.component';
import { CardDeckService } from '../../../../core/service/card-deck.service';
import { DuelmastersCard } from '../../../../core/model/card-duelmaster.model';
import { HololiveCard } from '../../../../core/model/card-hololive.model';

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard
  | DuelmastersCard
  | HololiveCard;

@Component({
  selector: 'app-deckcover-select',
  standalone: true,
  imports: [CommonModule, TcgImageComponent],
  templateUrl: './deckcover-select.component.html',
  styleUrl: './deckcover-select.component.css',
})
export class DeckcoverSelectComponent implements OnInit{
  @Output()
  onCloseSelector = new Subject<boolean>();

  @Input()
  isSmallScreen: boolean = false;

  cardsInDeck: { card: GameCard }[] = [];

  @Output()
  onDeckCoverSelect = new Subject<GameCard>();

  private cardDeckService = inject(CardDeckService);
  constructor() {
  }
  ngOnInit():void {
    this.cardDeckService.cardsInDeck$.subscribe({
      next:(response) =>{
        console.log("card service init")
        this.cardsInDeck = response;
        console.log(this.cardsInDeck)
      }
    })
  }

  getCardImage(card: GameCard): string {
    if ('urlimage' in card) {
      return card.urlimage;
    }
    return '';
  }

  getCardName(card: GameCard): string {
    if ('cardName' in card) {
      return card.cardName;
    }
    return 'Unknown Card';
  }

  whenCardSelected(card: GameCard) {
    this.onDeckCoverSelect.next(card);
  }

  whenSelected(event: any) {
    this.onCloseSelector.next(event);
  }
}
