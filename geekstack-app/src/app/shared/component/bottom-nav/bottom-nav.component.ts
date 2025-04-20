import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TcgStore } from '../../../core/store/ctcg.store';
import { TCGTYPE } from '../../../core/utils/constants';

@Component({
  selector: 'app-bottom-nav',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './bottom-nav.component.html',
  styleUrl: './bottom-nav.component.css',
})
export class BottomNavComponent {
  showDeckBuilderOptions: boolean = false;

  private router = inject(Router);
  constructor() {}

  bottoms = [
    { src: '/icons/bottomnav/HomeSelected.svg', alt: 'home', path: '/home' },
    {
      src: '/icons/bottomnav/DecklibrarySelected.svg',
      alt: 'library',
      path: '/poststacks',
    },
    {
      src: '/icons/bottomnav/DeckcreateSelected.svg',
      alt: 'deckbuilder',
      path: '/deckbuilder',
    },
    {
      src: '/icons/bottomnav/NewsSelected.svg',
      alt: 'stacks',
      path: '/stacks',
    },
    {
      src: '/icons/bottomnav/BellSelected.svg',
      alt: 'notitfication',
      path: '/notifications',
    },
  ];

  games = [
    {value:TCGTYPE.UNIONARENA,label:'Union Arena', icon:"/images/ua.jpg"},
    {value:TCGTYPE.ONEPIECE, label:'One Piece',icon:'/images/opc.jpg'},
    {value:TCGTYPE.HOLOLIVE, label:'Hololive',icon:'/images/opc.jpg'},
    {value:TCGTYPE.DUELMASTERS, label:'Duel Masters',icon:'/images/opc.jpg'},
    {value:TCGTYPE.COOKIERUN, label:'Cookie Run',icon:'/images/crb.jpg'},
    {value:TCGTYPE.DRAGONBALLZFW, label:'Dragonballz',icon:'/images/dbzfw.jpg'},

  ]

  isActive(path: string): boolean {
    return this.router.url.includes(path);
  }

  onBottomNavClick(bottom: any): void {
    if (bottom.alt === 'deckbuilder') {
      this.showDeckBuilderOptions = !this.showDeckBuilderOptions;
    }
  }

  toggleDeckBuilder() {
    this.showDeckBuilderOptions = !this.showDeckBuilderOptions;
  }

  selectCardGame(cardGame: string): void {
    console.log(`Selected card game: ${cardGame}`);
    this.showDeckBuilderOptions = false;
    this.router.navigate([`/deckbuilder/${cardGame.toLowerCase()}`]);
  }
}
