import { Component, HostListener, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { LoginModalComponent } from '../login-modal/login-modal.component';
import { CommonModule } from '@angular/common';
import { FireAuthService } from '../../../core/service/fireauth.service';
import { UserStore } from '../../../core/store/user.store';
import { GSSqlUser } from '../../../core/model/sql-user.model';
import { NotificationsComponent } from "../notifications/notifications.component";

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

  private router = inject(Router);
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
  }

  goBack(): void {
    this.router.navigate(['../']);
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
