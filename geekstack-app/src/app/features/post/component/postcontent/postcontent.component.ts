import { Component, inject } from '@angular/core';
import { ListOfDecks } from '../../../../core/model/listofdecks.model';
import { CardDeckService } from '../../../../core/service/card-deck.service';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserStore } from '../../../../core/store/user.store';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';
import { response } from 'express';
import { CardRecord } from '../../../../core/model/card-record.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-postcontent',
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './postcontent.component.html',
  styleUrl: './postcontent.component.css',
})
export class PostcontentComponent {
  decksOfUser: ListOfDecks[] = [];
  selectedDeck: ListOfDecks | null = null;
  tcg: string = 'unionarena';
  selectedDeckIndex: number | null = null;
  form!: FormGroup;

  private cardDeckService = inject(CardDeckService);
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
        Validators.maxLength(100)
      ]),
      content: new FormControl(''),
      deckName: new FormControl(''),
      isTournamentDeck: new FormControl(false),
      selectedCards: new FormControl(''),
      listofcards: new FormControl(null),
    });
  }

  ngOnInit() {
    this.cardDeckService
      .loadListOfDeckDirect(this.tcg)
      .then((mappedDecks) => {
        this.decksOfUser = mappedDecks;
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  }

  onTcgChange(): void {
    this.cardDeckService.loadListOfDeckDirect(this.tcg)
      .then((mappedDecks) => {
        this.decksOfUser = mappedDecks
        this.selectedDeck = null;
        this.selectedDeckIndex = null;
      })
      .catch((error) => {
        console.error('Error:', error);
        this.decksOfUser = [];
      });
  }
  

  selectDeck(index: number): void {
    this.selectedDeckIndex = this.selectedDeckIndex === index ? null : index;
    this.selectedDeck =
      this.selectedDeckIndex !== null
        ? this.decksOfUser[this.selectedDeckIndex]
        : null;

    const mappedCards: CardRecord[] = this.selectedDeck?.listofcards
      ? Object.values(this.selectedDeck.listofcards).map(({ card, count }) => ({
          _id: card?._id || '',
          imageSrc: card?.urlimage || '',
          cardName: card?.name || '',
          count: count || 1,
        }))
      : [];

    this.form.controls['selectedCards'].setValue([
      { imageSrc: this.selectedDeck?.deckcover },
    ]);
    this.form.controls['deckName'].setValue(this.selectedDeck?.deckname);
    this.form.controls['listofcards'].setValue(mappedCards);
  }

  onSubmit(): void {
    if (!this.form.controls['listofcards'].value || this.form.controls['listofcards'].value.length === 0) {
      alert("Cannot submit: Deck not selected");
      return; // Stop submission
    }

    if (this.form.controls['headline'].invalid) {
      alert("Your headline is invalid");
      return; // Stop submission
    }
    const user = this.userStore.getCurrentUser();
    this.form.controls['userId'].setValue(user.userId);
    this.form.controls['displaypic'].setValue(user.displaypic);
    this.form.controls['name'].setValue(user.name);
    this.form.controls['postType'].setValue(this.tcg);
    console.log(this.form.value);
    this.geekstackService.postByUser(this.form.value).subscribe({
      next: (response) => {
        console.log(response);
        this.router.navigate([`/stacks`])
      },
      error: (err) => {
        console.error(err);
      },
    });
  }
}
