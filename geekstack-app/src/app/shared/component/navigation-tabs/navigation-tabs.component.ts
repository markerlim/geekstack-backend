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
    { icon: '/icons/unionarenaicon.ico', alt: 'Decks', label: 'Decks', path: '/deckbuilder/unionarena' },
    { icon: '/icons/onepieceicon.png', alt: 'Decks', label: 'Decks', path: '/deckbuilder/onepiece' },
    { icon: '/icons/cookierun.png', alt: 'Decks', label: 'Decks', path: '/deckbuilder/cookierunbraverse' },
    { icon: '/icons/dragonballz.ico', alt: 'Decks', label: 'Decks', path: '/deckbuilder/dragonballzfw' },
    { icon: '/icons/bottomnav/NewsSelected.svg', alt: 'Stacks', label: 'Stacks', path: '/stacks' },
  ];
}
