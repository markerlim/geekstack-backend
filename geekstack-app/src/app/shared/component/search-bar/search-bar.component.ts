import { CommonModule } from '@angular/common';
import { Component, HostListener, Output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Subject } from 'rxjs';
import { TCGTYPE } from '../../../core/utils/constants';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, MatIconModule,FormsModule],
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css'],
})
export class SearchBarComponent {
  games = [
    {
      name: TCGTYPE.UNIONARENA,
      icon: '/icons/unionarenaicon.ico',
      label: 'Union Arena',
    },
    {
      name: TCGTYPE.ONEPIECE,
      icon: '/icons/onepieceicon.png',
      label: 'One Piece',
    },
    {
      name: TCGTYPE.DRAGONBALLZFW,
      icon: '/icons/dragonballz.ico',
      label: 'DBZ Fusion World',
    },
    {
      name: TCGTYPE.COOKIERUN,
      icon: '/icons/cookierun.png',
      label: 'Cookie Run',
    },
    {
      name: TCGTYPE.DUELMASTERS,
      icon: '/icons/duelmastericon.ico',
      label: 'Duel Masters',
    },
    {
      name: TCGTYPE.HOLOLIVE,
      icon: '/icons/hololiveicon.png',
      label: 'Hololive OCG',
    },
    {
      name: TCGTYPE.PKMNPOCKET,
      icon: '/icons/ptcgicon.png',
      label: 'PTCG Pocket',
    },
  ];

  searchTerm = '';
  selectedGame = this.games[0];
  isDropdownOpen = false;

  @Output()
  onSearchValue = new Subject<{ name: string; icon: string; term: string }>();

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  selectGame(game: { name: string; icon: string; label: string }) {
    this.selectedGame = game;
    this.toggleDropdown();
  }

  @HostListener('document:click', ['$event'])
  onClick(event: MouseEvent) {
    const dropdownElement = document.querySelector('.custom-dropdown');
    if (dropdownElement && !dropdownElement.contains(event.target as Node)) {
      this.isDropdownOpen = false;
    }
  }

  onSearch(event: any) {
    this.searchTerm = event.target.value;
    this.onSearchValue.next({
      name: this.selectedGame.name,
      icon: this.selectedGame.icon,
      term: this.searchTerm,
    });
  }
  

  handleClearSearch() {
    this.searchTerm = '';
    this.onSearchValue.next({
      name: this.selectedGame.name,
      icon: this.selectedGame.icon,
      term: this.searchTerm,
    });
  }
  
}
