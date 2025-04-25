import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { NavigationTabsComponent } from './shared/component/navigation-tabs/navigation-tabs.component';
import { BottomNavComponent } from './shared/component/bottom-nav/bottom-nav.component';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './shared/component/navbar/navbar.component';
import { ScreenSizeService } from './core/service/screen-size.service';
import { filter } from 'rxjs';
import { TcgStore } from './core/store/ctcg.store';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [
        RouterOutlet,
        NavbarComponent,
        NavigationTabsComponent,
        BottomNavComponent,
        CommonModule,
    ],
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'geekstack-app';

  isSmallScreen: boolean = false;
  isPwa: boolean = false;
  private screenSizeService = inject(ScreenSizeService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private tcgStore = inject(TcgStore);
  constructor() {
  }

  ngOnInit(): void {
    this.screenSizeService.isXSmallScreen$.subscribe((isSmall) => {
      this.isSmallScreen = isSmall;
    });

    this.router.events
    .pipe(filter((event) => event instanceof NavigationEnd))
    .subscribe(() => {
      const tcg = this.getTcgParam(this.route);
      console.log("loggin tcg path: ",tcg)
      if (tcg) {
        this.tcgStore.setCurrentTCG(tcg);
        console.log("logged tcg path", this.tcgStore.getCurrentTcg())
      }
    });

    this.isPwa = this.checkIfPwa();
  }

  private getTcgParam(route: ActivatedRoute): string | null {
    while (route.firstChild) {
      route = route.firstChild;
    }
    return route.snapshot.paramMap.get('tcg');
  }

  shouldHideNavbar(): boolean {
    if(this.router.url.includes('/stacks') && this.isSmallScreen){
      return true
    }
    return false;
  }

  isRegisterRoute(): boolean {
    return this.router.url === '/register';
  }
  
  private checkIfPwa(): boolean {
    return (
      window.matchMedia('(display-mode: standalone)').matches ||
      (navigator as any).standalone === true
    );
  }
}
