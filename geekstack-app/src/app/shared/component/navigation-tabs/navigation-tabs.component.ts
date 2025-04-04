import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'app-navigation-tabs',
    standalone: true,
    imports: [RouterLink, CommonModule],
    templateUrl: './navigation-tabs.component.html',
    styleUrls: ['./navigation-tabs.component.css']
})
export class NavigationTabsComponent {
  tabs = [
    { icon: '/icons/bottomnav/HomeSelected.svg', alt: 'home', label: 'Home', path: '/home' },
    { icon: '/icons/unionarenaicon.ico', alt: 'Decks', label: 'Deckbuilder', path: '/deckbuilder/unionarena' },
    { icon: '/icons/onepieceicon.png', alt: 'Decks', label: 'Deckbuilder', path: '/deckbuilder/onepiece' },
    { icon: '/icons/cookierun.png', alt: 'Decks', label: 'Deckbuilder', path: '/deckbuilder/cookierunbraverse' },
    { icon: '/icons/duelmastericon.ico', alt: 'Decks', label: 'Deckbuilder', path: '/deckbuilder/duelmasters' },
    { icon: '/icons/dragonballz.ico', alt: 'Decks', label: 'Deckbuilder', path: '/deckbuilder/dragonballzfw' },
    { icon: '/icons/bottomnav/NewsSelected.svg', alt: 'Stacks', label: 'Stacks', path: '/stacks' },
  ];
}
