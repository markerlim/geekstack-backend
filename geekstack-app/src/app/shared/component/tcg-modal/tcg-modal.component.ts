import { Component, inject, Input, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { CardUnionArena } from '../../../core/model/card-unionarena.model';
import { CardOnePiece } from '../../../core/model/card-onepiece.model';
import { CardDragonBallZFW } from '../../../core/model/card-dragonballzfw.model';
import { CookieRunCard } from '../../../core/model/card-cookierunbraverse.model';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { ErrorReportModalComponent } from '../error-report-modal/error-report-modal.component';
import { UserStore } from '../../../core/store/user.store';
import { DuelmastersCard } from '../../../core/model/card-duelmaster.model';

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard
  | DuelmastersCard;
  
@Component({
  selector: 'app-tcg-modal',
  imports: [CommonModule,MatIconModule],
  templateUrl: './tcg-modal.component.html',
  styleUrl: './tcg-modal.component.css',
})
export class TcgModalComponent {
  @Input()
  Card!: GameCard;

  @Output()
  onCloseTcgModal = new Subject<boolean>()

  isZoomIn: boolean = false;
  zoomImageUrl: string = '';

  tagsToIcons: { [key: string]: string } = {
    "[Impact 1]": "/icons/UAtags/CTImpact1.png",
    "[Impact 2]": "/icons/UAtags/CTImpact2.png",
    "[Impact]": "/icons/UAtags/CTImpact.png",
    "[Block x2]": "/icons/UAtags/CTBlkx2.png",
    "[Attack x2]": "/icons/UAtags/CTAtkx2.png",
    "[Snipe]": "/icons/UAtags/CTSnipe.png",
    "[Impact +1]": "/icons/UAtags/CTImpact+1.png",
    "[Step]": "/icons/UAtags/CTStep.png",
    "[Damage]": "/icons/UAtags/CTDmg.png",
    "[Damage +1]": "/icons/UAtags/CTDmg+1.png",
    "[Damage 2]": "/icons/UAtags/CTDmg2.png",
    "[Damage 3]": "/icons/UAtags/CTDmg3.png",
    "[Impact Negate]": "/icons/UAtags/CTImpactNegate.png",
    "[Once Per Turn]": "/icons/UAtags/CTOncePerTurn.png",
    "[Rest this card]": "/icons/UAtags/CTRestThisCard.png",
    "[Retire this card]": "/icons/UAtags/CTRetirethiscard.png",
    "[Place 1 card from hand to Outside Area]": "/icons/UAtags/CT1HandtoOA.png",
    "[Place 2 cards from hand to Outside Area]": "/icons/UAtags/CT2HandtoOA.png",
    "[When In Front Line]": "/icons/UAtags/CTWhenInFrontLine.png",
    "[When In Energy Line]": "/icons/UAtags/CTWhenInEnergyLine.png",
    "[Pay 1 AP]": "/icons/UAtags/CTPay1AP.png",
    "[Raid]": "/icons/UAtags/CTRaid.png",
    "[On Play]": "/icons/UAtags/CTOnPlay.png",
    "[On Retire]": "/icons/UAtags/CTOnRetire.png",
    "[On Block]": "/icons/UAtags/CTOnBlock.png",
    "[When Blocking]": "/icons/UAtags/CTWhenBlocking.png",
    "[Activate Main]": "/icons/UAtags/CTActivateMain.png",
    "[When Attacking]": "/icons/UAtags/CTWhenAttacking.png",
    "[Your Turn]": "/icons/UAtags/CTYourTurn.png",
    "[Opponent's Turn]": "/icons/UAtags/CTOppTurn.png",
    "[Trigger]": "/icons/UAtags/CTTrigger.png",
  };
  
  private dialog = inject(MatDialog);
  private userStore = inject(UserStore);
  constructor(){}

  onCloseModal() {
    this.onCloseTcgModal.next(false);
  }

  onClickZoomIn(){
    this.isZoomIn = !this.isZoomIn;
  }

  getCardType(): string {
    if ('costlife' in this.Card) return 'onepiece';
    if ('civilization' in this.Card) return 'duelmasters';
    if ('elementId' in this.Card) return 'cookierunbraverse';
    if ('cardName' in this.Card) {
      if ('anime' in this.Card) return 'unionarena';
      if ('saga' in this.Card) return 'dragonballzfw';
    }

    return 'Unknown';
  }

  openErrorDialog(): void {
    const dialogRef = this.dialog.open(ErrorReportModalComponent, {
      width: '300px',
      data: { 
        message: 'Describe the issue with this card',
        cardUid: this.Card.cardUid,
        userId: this.userStore.getCurrentUser().userId ?? "anonymous"
      }
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Error reported:', result.errorMessage);
      }
    });
  }
  

  replaceTagsWithIcons(line: string): string {
    let replacedLine = line;    
    Object.keys(this.tagsToIcons).forEach(tag => {
      const escapedTag = tag.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
      
      const iconTag = `<img class="tcg-icon" src="${this.tagsToIcons[tag]}" alt="${tag}">`;
      replacedLine = replacedLine.replace(new RegExp(escapedTag, 'g'), iconTag);
    });
    return replacedLine;
  }

  getUnionArenaCard(): CardUnionArena | null {
    return this.getCardType() === 'unionarena' ? (this.Card as CardUnionArena) : null;
  }
  
  getOnePieceCard(): CardOnePiece | null {
    return this.getCardType() === 'onepiece' ? (this.Card as CardOnePiece) : null;
  }
  
  getDuelmastersCard(): DuelmastersCard | null {
    return this.getCardType() === 'duelmasters' ? (this.Card as DuelmastersCard) : null;
  }
  getDragonBallZCard(): CardDragonBallZFW | null {
    return this.getCardType() === 'dragonballzfw' ? (this.Card as CardDragonBallZFW) : null;
  }
  
  getCookieRunCard(): CookieRunCard | null {
    return this.getCardType() === 'cookierunbraverse' ? (this.Card as CookieRunCard) : null;
  }
  
}
