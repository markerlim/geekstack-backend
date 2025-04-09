import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { TcgModalComponent } from '../tcg-modal/tcg-modal.component';
import { CardUnionArena } from '../../../core/model/card-unionarena.model';
import { CardOnePiece } from '../../../core/model/card-onepiece.model';
import { CardDragonBallZFW } from '../../../core/model/card-dragonballzfw.model';
import { CookieRunCard } from '../../../core/model/card-cookierunbraverse.model';
import { CardRecord } from '../../../core/model/card-record.model';
import { DuelmastersCard } from '../../../core/model/card-duelmaster.model';
import { HololiveCard } from '../../../core/model/card-hololive.model';

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard
  | DuelmastersCard
  | HololiveCard;

@Component({
  selector: 'app-tcg-image',
  standalone: true,
  imports: [CommonModule, TcgModalComponent],
  templateUrl: './tcg-image.component.html',
  styleUrl: './tcg-image.component.css',
})
export class TcgImageComponent {
  @Input()
  isMini: boolean = false;

  @Input()
  isPost: boolean = false;

  @Input()
  card!: GameCard;

  @Input()
  cardRecord!: CardRecord;

  @Input()
  isClickable: boolean = true;

  imageSrc: string = '';
  imageAlt: string = '';

  isTcgImageClicked: boolean = false;

  constructor() {}

  ngOnInit() {
    if (this.card) {
      this.imageSrc = this.card.urlimage;
      this.imageAlt = this.getCardName(this.card) || 'TCG Card';
    }

    if (this.cardRecord) {
      this.imageSrc = this.cardRecord.imageSrc;
      this.imageAlt = this.cardRecord.cardName;
      this.isClickable = false;
    }
  }
  getCardName(card: GameCard): string {
    return (card as any).cardName || (card as any).cardname || '';
  }

  onClickImage() {
    if (!this.isClickable) {
      return;
    }
    this.isTcgImageClicked = true;
  }

  onCloseImage(event: boolean) {
    this.isTcgImageClicked = event;
  }
}
