import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { NavigationTabsComponent } from './shared/component/navigation-tabs/navigation-tabs.component';
import { BottomNavComponent } from './shared/component/bottom-nav/bottom-nav.component';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './shared/component/navbar/navbar.component';
import { ScreenSizeService } from './core/service/screen-size.service';

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

  constructor() {
  }

  ngOnInit(): void {
    this.screenSizeService.isXSmallScreen$.subscribe((isSmall) => {
      this.isSmallScreen = isSmall;
    });

    this.isPwa = this.checkIfPwa();
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
