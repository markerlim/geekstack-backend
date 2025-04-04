import { Component, inject, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { GeekstackService } from '../../../core/service/geekstackdata.service';
import { Notifications } from '../../../core/model/notifications.model';
import { TimeAgoPipe } from '../../../core/pipe/time-ago.pipe';
import { Router } from '@angular/router';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { FormsModule } from '@angular/forms';
import { UserStore } from '../../../core/store/user.store';
import { FireCloudMessaging } from '../../../core/service/fcm.service';

@Component({
  selector: 'app-notifications',
  imports: [FormsModule, TimeAgoPipe, MatSlideToggleModule],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css',
})
export class NotificationsComponent {
  @Output()
  onCloseNotification = new Subject<boolean>();

  listofnotif: Notifications[] = [];

  isLoading = false;
  notificationsEnabled = false;

  private geekstackService = inject(GeekstackService);
  private fcm = inject(FireCloudMessaging);
  private userStore = inject(UserStore);
  private router = inject(Router);

  constructor() {
    this.geekstackService.getNotificationsForUser().subscribe({
      next: (response) => {
        this.listofnotif = response;
        console.log('TEST', response);
      },
      error: (error) => {
        console.error('Error loading notifications:', error);
      },
    });
  }

  ngOnInit() {
    this.checkNotificationStatus();
  }

  private checkNotificationStatus() {
    this.notificationsEnabled = true;
  }

  async toggleNotification() {
    this.isLoading = true;
    try {
      if (this.notificationsEnabled) {
        await this.enableNotificationsBackend();
      } else {
        await this.disableNotificationsBackend();
      }
    } catch (err) {
      console.error('Error toggling notifications:', err);
    } finally {
      this.isLoading = false;
    }
  }

  async enableNotificationsBackend(): Promise<void> {
    try {
      const token = await this.fcm.requestPermission();
      if (token) {
        this.geekstackService
          .updateFCMToken(this.userStore.getCurrentUser().userId, token)
          .subscribe({
            next: (response) => {
              console.log('Notifications enabled on the backend:', response);
              this.notificationsEnabled = true;
            },
            error: (err) => {
              console.error('Error enabling notifications:', err);
            },
          });
      } else {
        console.log('No token found');
      }
    } catch (err) {
      console.error('Error enabling notifications:', err);
    }
  }

  async disableNotificationsBackend(): Promise<void> {
    this.geekstackService.removeFCMToken(this.userStore.getCurrentUser().userId).subscribe({
      next: (response) => {
        console.log('Notifications disabled on the backend:', response);
        this.notificationsEnabled = false;
      },
      error: (err) => {
        console.error('Error disabling notifications:', err);
      },
    });
  }

  groupNotifications(notifications: any[]): any[] {
    const grouped = notifications.reduce((acc, notif) => {
      const { postId, user, timestamp } = notif;

      if (!acc[postId]) {
        acc[postId] = {
          postId,
          users: [],
          timestamp,
        };
      }

      acc[postId].users.push(user);
      return acc;
    }, {});

    return Object.values(grouped);
  }

  redirectToPost(postId: string) {
    this.router.navigateByUrl(`/stacks/${postId}`);
  }

  onClickClose() {
    this.onCloseNotification.next(false);
  }
}
