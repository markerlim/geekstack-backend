import { Component, HostListener, inject, OnDestroy, OnInit } from '@angular/core';
import { ListOfDecks } from '../../../../core/model/listofdecks.model';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserStore } from '../../../../core/store/user.store';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';
import { response } from 'express';
import { CardRecord } from '../../../../core/model/card-record.model';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { GSSqlUser } from '../../../../core/model/sql-user.model';
import { TCGTYPE } from '../../../../core/utils/constants';
import { CardDeckService } from '../../../../core/service/card-deck.service';

@Component({
  selector: 'app-postcontent',
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './postcontent.component.html',
  styleUrl: './postcontent.component.css',
})
export class PostcontentComponent implements OnInit, OnDestroy {
  decksOfUser: ListOfDecks[] = [];
  selectedDeck: ListOfDecks | null = null;
  user!: GSSqlUser;
  selectedDeckIndex: number | null = null;
  form!: FormGroup;
  selectTcg = [
    {
      value: TCGTYPE.UNIONARENA,
      label: 'Union Arena',
      icon: '/icons/unionarenaicon.ico',
    },
    {
      value: TCGTYPE.ONEPIECE,
      label: 'One Piece',
      icon: '/icons/onepieceicon.png',
    },
    {
      value: TCGTYPE.COOKIERUN,
      label: 'Cookie Run',
      icon: '/icons/cookierun.png',
    },
    {
      value: TCGTYPE.HOLOLIVE,
      label: 'Hololive OCG',
      icon: '/icons/hololiveicon.png',
    },
    {
      value: TCGTYPE.DUELMASTERS,
      label: 'Duelmasters',
      icon: '/icons/duelmastericon.ico',
    },
    {
      value: TCGTYPE.DRAGONBALLZFW,
      label: 'Dragon Ball Z',
      icon: '/icons/dragonballz.ico',
    },
  ];
  dropdownOpen = false;
  selectedTcg = this.selectTcg[0];
  deckSearchQuery: string = '';
filteredDecks: any[] = [];

  private destroy$ = new Subject<void>();

  private geekstackService = inject(GeekstackService);
  private router = inject(Router);
  private userStore = inject(UserStore);
  constructor() {
    
    this.form = new FormGroup({
      postType: new FormControl(''),
      userId: new FormControl(''),
      name: new FormControl(''),
      displaypic: new FormControl(''),
      headline: new FormControl('', [
        Validators.required,
        Validators.maxLength(100),
      ]),
      content: new FormControl(''),
      deckName: new FormControl(''),
      isTournamentDeck: new FormControl(false),
      selectedCards: new FormControl(''),
      listofcards: new FormControl(null),
    });
  }

  ngOnInit() {
    this.userStore.gsSqlUser$.pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this.user = res || { userId: '', name: '', displaypic: '' };
      },
      error: (err) => {
        console.error('Error loading user:', err);
      },
    });

    this.userStore.getDecks(this.selectedTcg.value).subscribe({
      next: (res) => {
        this.decksOfUser = res;
        this.filteredDecks = this.decksOfUser;
      },
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  selectTcgOption(item: any, event:Event) {
    event.stopPropagation();
    this.dropdownOpen = false;
    this.selectedTcg = item;
    this.onTcgChange();
  }
  onTcgChange(): void {
    this.userStore.getDecks(this.selectedTcg.value).subscribe({
      next: (res) => {
        this.decksOfUser = res;
        this.filteredDecks = this.decksOfUser;
      },
    });
  }

  handleSelectTcg(){
    this.dropdownOpen = !this.dropdownOpen;
  }

  searchDeck(): void {
    const query = this.deckSearchQuery.toLowerCase().trim();
    if (!query) {
      this.filteredDecks = this.decksOfUser;
      return;
    }
  
    this.filteredDecks = this.decksOfUser.filter(deck =>
      deck.deckname.toLowerCase().includes(query)
    );
  }

  selectDeck(index: number): void {
    this.selectedDeckIndex = this.selectedDeckIndex === index ? null : index;
    this.selectedDeck =
      this.selectedDeckIndex !== null
        ? this.decksOfUser[this.selectedDeckIndex]
        : null;

    console.log(this.selectedDeck);

    const mappedCards: CardRecord[] = this.selectedDeck?.listofcards
      ? this.selectedDeck.listofcards.map(
          ({ _id, urlimage, cardName, title, count }) => {
            return {
              _id: _id || '',
              imageSrc: urlimage || '',
              cardName: cardName || title || '',
              count: count || 1,
            };
          }
        )
      : [];

    console.log('CHECKING MAPPED CARDS', mappedCards);

    // Set form values
    this.form.controls['selectedCards'].setValue([
      { imageSrc: this.selectedDeck?.deckcover },
    ]);
    this.form.controls['deckName'].setValue(this.selectedDeck?.deckname);
    this.form.controls['listofcards'].setValue(mappedCards);
  }

  onSubmit(): void {
    if (
      !this.form.controls['listofcards'].value ||
      this.form.controls['listofcards'].value.length === 0
    ) {
      alert('Cannot submit: Deck not selected');
      return; // Stop submission
    }

    if (this.form.controls['headline'].invalid) {
      alert('Your headline is invalid');
      return; // Stop submission
    }
    const user = this.user;
    this.form.controls['userId'].setValue(user.userId);
    this.form.controls['displaypic'].setValue(user.displaypic);
    this.form.controls['name'].setValue(user.name);
    this.form.controls['postType'].setValue(this.selectedTcg.value);
    console.log(this.form.value);
    this.geekstackService.postByUser(this.form.value).subscribe({
      next: (response) => {
        console.log(response);
        this.router.navigate([`/stacks`]);
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

    @HostListener('document:click', ['$event'])
    onClick(event: MouseEvent) {
      const dropdownElement = document.querySelector('.custom-select');
      if (dropdownElement && !dropdownElement.contains(event.target as Node)) {
        this.dropdownOpen = false;
      }
    }
}
