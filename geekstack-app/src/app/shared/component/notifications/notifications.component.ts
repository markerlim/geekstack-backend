import { Component, inject, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { GeekstackService } from '../../../core/service/geekstackdata.service';
import { Notifications } from '../../../core/model/notifications.model';
import { TimeAgoPipe } from '../../../core/pipe/time-ago.pipe';
import { Router } from '@angular/router';

@Component({
  selector: 'app-notifications',
  imports: [TimeAgoPipe],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css',
})
export class NotificationsComponent {
  @Output()
  onCloseNotification = new Subject<boolean>();

  listofnotif: Notifications[] = [];

  private geekstackService = inject(GeekstackService);
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
