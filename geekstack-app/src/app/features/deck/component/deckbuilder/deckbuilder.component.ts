import { Component, inject, OnDestroy, OnInit } from '@angular/core';
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
import { DuelmastersCard } from '../../../../core/model/card-duelmaster.model';
import { ListOfDecks } from '../../../../core/model/listofdecks.model';
import { Subject, takeUntil } from 'rxjs';
import { HololiveCard } from '../../../../core/model/card-hololive.model';

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard
  | DuelmastersCard
  | HololiveCard;

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
    MatIconModule,
  ],
  providers: [TcgStore],
  templateUrl: './deckbuilder.component.html',
  styleUrls: ['./deckbuilder.component.css'],
})
export class DeckbuilderComponent implements OnInit, OnDestroy {
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
  user!: GSSqlUser;
  tcg: string = '';
  private destroy$ = new Subject<void>();

  private route = inject(ActivatedRoute);
  private cardDeckService = inject(CardDeckService);
  private screenSizeService = inject(ScreenSizeService);
  private userStore = inject(UserStore);
  private tcgStore = inject(TcgStore);

  constructor() {}

  ngOnInit(): void {
    // Safe subscription that auto-unsubscribes
    this.screenSizeService.isXSmallScreen$
      .pipe(takeUntil(this.destroy$))
      .subscribe((isSmall) => {
        this.isSmallScreen = isSmall;
      });

    // Safe route params subscription
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const tcg = params['tcg'];
      this.tcgStore.setTcg(tcg);
      this.tcg = this.tcgStore.getCurrentTcg();
    });

    this.userStore.gsSqlUser$.pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this.user = res
          ? res
          : { userId: 'noUserId', name: 'error', displaypic: 'string' };
      },
    });

    this.isPwa = this.checkIfPwa();
  }

  ngOnDestroy() {
    this.destroy$.next(); // Triggers unsubscription
    this.destroy$.complete(); // Cleans up the Subject
    this.cardDeckService.clearList(); // Your existing cleanup
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
    console.log('COVER: ', result);
  }

  isDeckLoadClicked(result: boolean) {
    this.isDeckLoadSelect = result;
  }

  isDeckSelected(event: ListOfDecks) {
    this.cardDeckService.loadDeckFromList(
      event.listofcards,
      this.tcgStore.getCurrentTcg()
    );
    const object = this.cardDeckService.getDeckDetails();
    this.deckcover = object.deckcover;
    this.deckname = object.deckname;
    this.deckuid = object.deckuid;
  }

  deckCoverUrl(card: GameCard) {
    this.deckcover = card.urlimage;
  }

  openModal(result: boolean) {
    this.isCardListActive = result;
    this.isDeckLoadSelect = result;
    this.isDeckcoverSelect = result;
    this.toggleModalOpen(result);
  }
  openCardList(result: boolean) {
    this.isCardListActive = result;
    this.toggleModalOpen(result);
  }
  toggleModalOpen(result: boolean) {
    this.isOverLayActive = result;
  }
  handleCardlistActive(result: boolean) {
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
