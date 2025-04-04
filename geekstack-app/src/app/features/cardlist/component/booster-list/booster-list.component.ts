import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';

@Component({
  selector: 'app-booster-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './booster-list.component.html',
  styleUrls: ['./booster-list.component.css'],
})
export class BoosterListComponent implements OnInit {
  boosterList: Array<{
    pathname: string;
    alt: string;
    imageSrc: string;
    imgWidth: number;
  }> = [];
  duelmasterlist: any[] = [];
  tcgPath: string = '';
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private geekstackService = inject(GeekstackService);
  constructor() {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.tcgPath = params.get('tcg') || '';
      this.fetchBoosterList();
    });
  }

  fetchBoosterList(): void {
    if (this.tcgPath == 'duelmasters') {
      this.geekstackService.getDuelmasterBtn().subscribe({
        next: (response) =>{
          this.duelmasterlist = response
         },
        error:(err) =>{
          console.error(err);
        } 
      })
    } else {
      this.geekstackService.getBoosterOfTcg(this.tcgPath).subscribe({
        next: (data) => {
          this.boosterList = data;
          console.log(data);
        },
        error: (err) => {
          console.error('Failed to fetch booster list:', err);
        },
      });
    }
  }

  onBoosterClick(pathname: string): void {
    this.router.navigate(['tcg', this.tcgPath, pathname]); // Navigate to the 'pathname' of the clicked booster
  }
}
