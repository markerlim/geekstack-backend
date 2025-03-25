import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

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
      path: '/library',
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
