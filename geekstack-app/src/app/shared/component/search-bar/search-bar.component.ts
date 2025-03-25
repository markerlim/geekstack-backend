import { CommonModule } from '@angular/common';
import { Component, HostListener, Output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Subject } from 'rxjs';

@Component({
    selector: 'app-search-bar',
    standalone: true,
    imports: [CommonModule, MatIconModule],
    templateUrl: './search-bar.component.html',
    styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent {
  games = [
    { name: 'unionarena', icon: '/icons/unionarenaicon.ico' },
    { name: 'onepiece', icon: '/icons/onepieceicon.png' },
    { name: 'dragonballzfw', icon: '/icons/dragonballz.ico' },
    { name: 'cookierunbraverse', icon: '/icons/cookierun.png' }
  ];

  selectedGame = this.games[0];
  isDropdownOpen = false;
  
  @Output()
  onSearchValue = new Subject<{name:string,icon:string,term:string}>;

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  selectGame(game: { name: string; icon: string }) {
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
    this.onSearchValue.next({ name: this.selectedGame.name, icon: this.selectedGame.icon, term: event.target.value });
  }  
}