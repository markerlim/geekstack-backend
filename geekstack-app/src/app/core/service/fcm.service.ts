import { inject, Injectable } from '@angular/core';
import { GeekstackService } from './geekstackdata.service';
import { BehaviorSubject, firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import {
  Messaging,
  deleteToken,
  getToken,
  onMessage,
} from '@angular/fire/messaging';
import { UserStore } from '../store/user.store';

@Injectable({
  providedIn: 'root',
})
export class FireCloudMessaging {
  private messaging = inject(Messaging);
  private geekstackService = inject(GeekstackService);
  private router = inject(Router);
  private userStore = inject(UserStore);
  currentMessage = new BehaviorSubject<any>(null);

  userId: string = '';

  constructor() {
    if ('serviceWorker' in navigator) {
      navigator.serviceWorker
        .register('/firebase-messaging-sw.js')
        .then((registration) => {
          console.log('Service Worker registered successfully:', registration);
        })
        .catch((error) => {
          console.error('Service Worker registration failed:', error);
        });
    }
    this.userId = this.userStore.getCurrentUser().userId;
    this.listenForForegroundMessages();
  }
  async requestPermission() {
    try {
      const permission = await Notification.requestPermission();
      if (permission !== 'granted') {
        console.log('Notification permission not granted');
        return null;
      }

      const token = await getToken(this.messaging, {
        vapidKey:
          'BCBk0ejltkllnLeHrXCI7qBHziP0o0h9G_0m_me-hvW0iD1FziZHjqTvPax0BmaaMb5u83_TiSoALpmZ7mnjJ-c',
      });

      if (token) {
        this.geekstackService.updateFCMToken(this.userId, token).subscribe({
          next: (response) => {
            console.log('FCM Token:', response);
          },
          error: (err) => {
            console.error(err);
          },
        });
      } else {
        console.log('No FCM token received.');
      }

      return token;
    } catch (err) {
      console.error('Failed to get FCM token:', err);
      return null;
    }
  }

  async deleteToken(): Promise<boolean> {
    try {
      const token = await getToken(this.messaging);
      if (token) {
        await deleteToken(this.messaging);
        console.log('FCM Token deleted successfully');
        return true;
      } else {
        console.log('No FCM token found.');
        return false;
      }
    } catch (error) {
      console.error('Failed to delete FCM token:', error);
      return false;
    }
  }

  private listenForForegroundMessages() {
    onMessage(this.messaging, (payload) => {
      console.log('New foreground message received:', payload);
      this.currentMessage.next(payload);

      // Show browser notification
      if (Notification.permission === 'granted' && payload.notification) {
        const title = payload.notification.title || 'New Notification';
        const options = {
          body: payload.notification.body || '',
          icon: payload.notification.icon || '/assets/icons/icon-72x72.png',
          data: payload.data || {},
        };

        const notification = new Notification(title, options);

        notification.onclick = () => {
          console.log('Foreground notification clicked. Data:', options.data);
          window.focus();

          // Try to navigate based on data payload if available
          const postId = options.data['postId'];
          if (postId) {
            this.router.navigate(['/stacks/', postId]);
          }
          notification.close();
        };
      }
    });
  }
}
