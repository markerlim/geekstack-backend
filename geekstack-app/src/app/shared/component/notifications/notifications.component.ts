import { Component, inject, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { GeekstackService } from '../../../core/service/geekstackdata.service';
import { Notifications } from '../../../core/model/notifications.model';
import { TimeAgoPipe } from '../../../core/pipe/time-ago.pipe';
import { Router } from '@angular/router';
import { FireCloudMessaging } from '../../../core/service/fcm.service';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { FormsModule } from '@angular/forms';

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
  private fireCloudMessaging = inject(FireCloudMessaging);
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
    this.notificationsEnabled = Notification.permission === 'granted';
  }

  async toggleNotification() {
    this.isLoading = true;
    try {
      if (this.notificationsEnabled) {
        await this.requestNotificationPermission();
      } else {
        await this.removeNotificationPermission();
      }
    } catch (err) {
      console.error('Error toggling notifications:', err);
    } finally {
      this.isLoading = false;
    }
  }

  async requestNotificationPermission(): Promise<void> {
    const token = await this.fireCloudMessaging.requestPermission();
    if (token) {
      console.log('Notifications enabled, token:', token);
      this.notificationsEnabled = true;
    } else {
      console.warn('Notifications are not enabled');
    }
  }

  async removeNotificationPermission(): Promise<void> {
    try {
      await this.fireCloudMessaging.deleteToken();
      console.log('Notifications disabled, token removed');
      this.notificationsEnabled = false;
    } catch (error) {
      console.error('Error removing notification token:', error);
    }
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
