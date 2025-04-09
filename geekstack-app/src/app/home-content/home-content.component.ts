import { CommonModule } from '@angular/common';
import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';
import { SearchBarComponent } from '../shared/component/search-bar/search-bar.component';
import { GeekstackService } from '../core/service/geekstackdata.service';
import { CardUnionArena } from '../core/model/card-unionarena.model';
import { CardOnePiece } from '../core/model/card-onepiece.model';
import { CardDragonBallZFW } from '../core/model/card-dragonballzfw.model';
import { CookieRunCard } from '../core/model/card-cookierunbraverse.model';
import { TcgImageComponent } from "../shared/component/tcg-image/tcg-image.component";
import { DuelmastersCard } from '../core/model/card-duelmaster.model';
import { TCGTYPE } from '../core/utils/constants';
import { HololiveCard } from '../core/model/card-hololive.model';

@Component({
    selector: 'app-home-content',
    standalone: true,
    imports: [RouterLink, CommonModule, SearchBarComponent, TcgImageComponent],
    templateUrl: './home-content.component.html',
    styleUrl: './home-content.component.css'
})
export class HomeContentComponent {
  @ViewChild('tcgList') tcgList!: ElementRef<HTMLDivElement>;

  tcglist = [
    {
      img: '/images/homeUAbtn.jpg',
      alt: 'Union Arena',
      path: `/tcg/${TCGTYPE.UNIONARENA}`,
    },
    { img: '/images/homeOPbtn.jpg', alt: 'One piece', path: `/tcg/${TCGTYPE.ONEPIECE}` },
    {
      img: '/images/homeHOCGbtn.webp',
      alt: 'Hololive Official Card Game',
      path: `/tcg/${TCGTYPE.HOLOLIVE}`,
    },
    {
      img: '/images/homeCRBbtn.jpg',
      alt: 'Cookie Run Braverse',
      path: `/tcg/${TCGTYPE.COOKIERUN}`,
    },
    {
      img: '/images/homeDMbtn.jpg',
      alt: 'Duelmasters',
      path: `/tcg/${TCGTYPE.DUELMASTERS}`,
    },
    {
      img: '/images/homeDBZbtn.jpg',
      alt: 'Dragonballz Fusion World',
      path: `/tcg/${TCGTYPE.DRAGONBALLZFW}`,
    },
    {
      img: '/images/homePTCGPbtn.jpg',
      alt: 'Pokemon',
      path: `/tcg/${TCGTYPE.PKMNPOCKET}`,
    },
  ];

  searchValue: string = '';
  exchangeRate: string = '';
  cardList: Array<
    CardUnionArena | CardOnePiece | CardDragonBallZFW | CookieRunCard | DuelmastersCard | HololiveCard
  > = [];

  private geekstackService = inject(GeekstackService);
  constructor() {}

  handleSearchValue(value: {name:string,icon:string,term:string}) {
    this.searchValue = value.term;
    this.geekstackService.searchCard(value.name,value.term).subscribe({
      next: (response) => {
        this.cardList = response || [];
        this.updateScrollableState();
      },
      error: (err) => {
        console.error('Failed to fetch cards:', err);
        this.cardList = err || [];
        this.updateScrollableState();
      },
    });
  }


  ngAfterViewInit() {
    this.updateScrollableState();
    this.geekstackService.getExchangeRate().subscribe({
      next: (response) => {
        this.exchangeRate = response;
      }
    })
  }

  private updateScrollableState(): void {
    if (this.searchValue) {
      return;
    }

    const tcgListEl = this.tcgList.nativeElement;

    if (tcgListEl.scrollWidth > tcgListEl.clientWidth) {
      tcgListEl.classList.add('scrollable');
    } else {
      tcgListEl.classList.remove('scrollable');
    }
  }
}
