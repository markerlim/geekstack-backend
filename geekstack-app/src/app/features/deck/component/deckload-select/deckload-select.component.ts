import {
  Component,
  inject,
  Input,
  Output,
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

type GameCard =
  | CardUnionArena
  | CardOnePiece
  | CardDragonBallZFW
  | CookieRunCard;

@Component({
  selector: 'app-deckload-select',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './deckload-select.component.html',
  styleUrl: './deckload-select.component.css',
})
export class DeckloadSelectComponent {
  @Input()
  isSmallScreen: boolean = false;

  @Output()
  onCloseSelector = new Subject<boolean>();

  @Output()
  selectedDeck = new Subject<{ card: GameCard; count: number }[]>();

  decksOfUser: ListOfDecks[] = [];

  @Input()
  userId!: string;

  @Input()
  tcg: string ='';

  private cardDeckService = inject(CardDeckService);
  private geekstackService = inject(GeekstackService);
  constructor() {
  }

  ngOnInit(): void {
    this.cardDeckService.loadListOfDeckDirect(this.tcg).then(mappedDecks => {
      this.decksOfUser = mappedDecks;
    }).catch(error => {
      console.error('Error:', error);
    });
  }

  onClickDeck(deckData: ListOfDecks ){
    let listofcards: { card: GameCard; count: number }[] = [];
    listofcards = deckData.listofcards;
    this.cardDeckService.setDeckDetails(deckData.deckuid,deckData.deckname,deckData.deckcover)
    this.selectedDeck.next(listofcards);
    this.onCloseSelector.next(false);
  }

  deleteDeck(deckId: string) {
    this.geekstackService.userDeleteDeck(this.userId, deckId,this.tcg).subscribe({
      next: () => {
        console.log('Deck deleted successfully');
        this.decksOfUser = this.decksOfUser.filter(deck => deck.deckuid !== deckId);
      },
      error: (error) => {
        console.error('Error deleting deck:', error);
      },
    });
  }
  
}
