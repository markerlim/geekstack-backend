import { Component, inject, OnInit } from '@angular/core';
import { BoosterListDeckbuilderComponent } from '../booster-list-deckbuilder/booster-list-deckbuilder.component';
import { CommonModule } from '@angular/common';
import { CardUnionArena } from '../../../../core/model/card-unionarena.model';
import { CardOnePiece } from '../../../../core/model/card-onepiece.model';
import { CardDragonBallZFW } from '../../../../core/model/card-dragonballzfw.model';
import { CookieRunCard } from '../../../../core/model/card-cookierunbraverse.model';
import { CardInDeckComponent } from '../card-in-deck/card-in-deck.component';
import { DeckbuilderActionsComponent } from '../deckbuilder-actions/deckbuilder-actions.component';
import { CardDeckService } from '../../../../core/service/card-deck.service';
import { DeckcoverSelectComponent } from '../deckcover-select/deckcover-select.component';
import { ScreenSizeService } from '../../../../core/service/screen-size.service';
import { DeckloadSelectComponent } from '../deckload-select/deckload-select.component';
import { MatIconModule } from '@angular/material/icon';
import { UserStore } from '../../../../core/store/user.store';
import { TcgStore } from '../../../../core/store/ctcg.store';
import { ActivatedRoute } from '@angular/router';
import { GSSqlUser } from '../../../../core/model/sql-user.model';

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard;

@Component({
    selector: 'app-deckbuilder',
    standalone: true,
    imports: [
        BoosterListDeckbuilderComponent,
        CardInDeckComponent,
        DeckbuilderActionsComponent,
        CommonModule,
        DeckcoverSelectComponent,
        DeckloadSelectComponent,
        MatIconModule
    ],
    providers:[TcgStore],
    templateUrl: './deckbuilder.component.html',
    styleUrls: ['./deckbuilder.component.css']
})
export class DeckbuilderComponent implements OnInit {
  title = 'geekstack-app';
  isSmallScreen: boolean = false;
  isPwa: boolean = false;
  isCardListActive: boolean = false;
  isOverLayActive: boolean = false;
  isDeckcoverSelect: boolean = false;
  isDeckLoadSelect: boolean = false;
  deckcover: string = '/images/gsdeckimage.jpg';
  deckuid: string = '';
  deckname: string = 'Deck Name';
  user: GSSqlUser;
  tcg: string = '';
  
  private route = inject(ActivatedRoute);
  private cardDeckService = inject(CardDeckService);
  private screenSizeService = inject(ScreenSizeService);
  private userStore = inject(UserStore);
  private tcgStore = inject(TcgStore);
  
  constructor() {
    this.user = this.userStore.getCurrentUser();
  }

  ngOnInit(): void {
    this.screenSizeService.isXSmallScreen$.subscribe((isSmall) => {
      this.isSmallScreen = isSmall;
    });
    this.route.params.subscribe((params) => {
      const tcg = params['tcg'];
      this.tcgStore.setTcg(tcg);
      this.tcg = this.tcgStore.getCurrentTcg();
      console.log("changed to: ",this.tcg);
    });
    this.isPwa = this.checkIfPwa();
  }

  // Update methods to handle any card type
  addCardToDeck(card: GameCard): void {
    this.cardDeckService.addCard(card);
  }

  removeCardFromDeck(card: GameCard): void {
    this.cardDeckService.removeCard(card);
  }


  isDeckCoverClicked(result: boolean) {
    this.isDeckcoverSelect = result;
    console.log("COVER: ",result)
  }

  isDeckLoadClicked(result: boolean) {
    this.isDeckLoadSelect = result;
  }

  isDeckSelected(event:{ card: GameCard; count: number }[]){
    this.cardDeckService.loadDeckFromList(event,"unionarena");
    const object = this.cardDeckService.getDeckDetails()
    this.deckcover = object.deckcover;
    this.deckname = object.deckname;
    this.deckuid = object.deckuid;
  }

  deckCoverUrl(card: GameCard) {
    this.deckcover = card.urlimage;
  }

  openModal(result:boolean) {
    this.isCardListActive = result;
    this.isDeckLoadSelect = result;
    this.isDeckcoverSelect = result;
    this.toggleModalOpen(result);
  }
  openCardList(result:boolean){
    this.isCardListActive = result;
    this.toggleModalOpen(result);
  }
  toggleModalOpen(result:boolean) {
    this.isOverLayActive = result;
  }
  handleCardlistActive(result:boolean){
    this.isCardListActive = result;
    this.isOverLayActive = result;
  }

  private checkIfPwa(): boolean {
    return (
      window.matchMedia('(display-mode: standalone)').matches ||
      (navigator as any).standalone === true
    );
  }
}
