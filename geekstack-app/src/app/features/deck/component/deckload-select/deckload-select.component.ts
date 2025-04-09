import {
  Component,
  inject,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
} from '@angular/core';
import { Subject } from 'rxjs';
import { ListOfDecks } from '../../../../core/model/listofdecks.model';
import { CommonModule } from '@angular/common';
import { CardDeckService } from '../../../../core/service/card-deck.service';
import { MatIconModule } from '@angular/material/icon';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';
import { CardUnionArena } from '../../../../core/model/card-unionarena.model';
import { CardOnePiece } from '../../../../core/model/card-onepiece.model';
import { CardDragonBallZFW } from '../../../../core/model/card-dragonballzfw.model';
import { CookieRunCard } from '../../../../core/model/card-cookierunbraverse.model';
import { Router } from '@angular/router';
import { UserStore } from '../../../../core/store/user.store';
import { DuelmastersCard } from '../../../../core/model/card-duelmaster.model';

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard
  | DuelmastersCard;

@Component({
  selector: 'app-deckload-select',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './deckload-select.component.html',
  styleUrl: './deckload-select.component.css',
})
export class DeckloadSelectComponent implements OnChanges {
  @Input()
  isSmallScreen: boolean = false;

  @Output()
  onCloseSelector = new Subject<boolean>();

  @Output()
  selectedDeck = new Subject<ListOfDecks>();

  decksOfUser: ListOfDecks[] = [];

  @Input()
  userId!: string;

  @Input()
  tcg: string = '';

  private cardDeckService = inject(CardDeckService);
  private geekstackService = inject(GeekstackService);
  private userStore = inject(UserStore);
  private router = inject(Router);
  constructor() {}
  ngOnInit(): void {
    this.loadDecks(this.tcg);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['tcg'] && !changes['tcg'].firstChange) {
      this.loadDecks(this.tcg);
    }
  }

  private loadDecks(tcg: string): void {
    this.userStore.getDecks(tcg).subscribe({
      next: (res) => {
        this.decksOfUser = res;
      },
    });
  }

  onClickDeck(deckData: ListOfDecks) {
    this.cardDeckService.setDeckDetails(
      deckData.deckuid,
      deckData.deckname,
      deckData.deckcover
    );
    this.selectedDeck.next(deckData);
    this.onCloseSelector.next(false);
  }

  shareDeck() {
    this.router.navigate(['/poststacks']);
  }
  deleteDeck(deckId: string, event: Event) {
    event.stopPropagation();
    this.geekstackService
      .userDeleteDeck(this.userId, deckId, this.tcg)
      .subscribe({
        next: () => {
          console.log('Deck deleted successfully');
          this.userStore.deleteDeck({ deckuid: deckId,tcg: this.tcg });
          this.decksOfUser = this.decksOfUser.filter(
            (deck) => deck.deckuid !== deckId
          );
        },
        error: (error) => {
          console.error('Error deleting deck:', error);
        },
      });
  }
}
