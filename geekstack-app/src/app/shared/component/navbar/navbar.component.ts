import { Component, HostListener, inject, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { LoginModalComponent } from '../login-modal/login-modal.component';
import { CommonModule, Location } from '@angular/common';
import { FireAuthService } from '../../../core/service/fireauth.service';
import { UserStore } from '../../../core/store/user.store';
import { GSSqlUser } from '../../../core/model/sql-user.model';
import { filter } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  user!: GSSqlUser | null;
  isSmallScreen: boolean = false;
  menuOpen = false;
  notificationClicked = false;
  showBackInstead = false;

  private router = inject(Router);
  private location = inject(Location);
  private dialog = inject(MatDialog);
  private authService = inject(FireAuthService);
  private userStore = inject(UserStore);
  private breakpointObserver = inject(BreakpointObserver);

  constructor() {
    this.userStore.gsSqlUser$.subscribe((user) => {
      this.user = user;
    });
  }

  ngOnInit(): void {
    this.breakpointObserver
      .observe([Breakpoints.XSmall])
      .subscribe((result) => {
        this.isSmallScreen = result.matches;
      });

      this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        const currentUrl = event.urlAfterRedirects;
        if(this.isSmallScreen){
          this.showBackInstead = !['/', '/home', '/notifications'].includes(currentUrl);
        }
      });
  }

  goBack(): void {
    this.location.back();
  }

  async handleLogout(): Promise<void> {
    this.menuOpen = false;
    await this.authService.signOut();
    this.router.navigate(['/']).then(() => {
      window.location.reload();
    });
  }

  openLoginModal(): void {
    this.dialog.open(LoginModalComponent, {
      width: 'fit-content',
    });
  }

  openNotifications(): void {
    this.notificationClicked = !this.notificationClicked;
  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  navigateTo(route: string) {
    this.router.navigate([`/${route}`]);
    console.log(`Navigating to ${route}`);
    this.menuOpen = false; // Close the menu
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.avatar-container')) {
      this.menuOpen = false;
    }
  }
}
