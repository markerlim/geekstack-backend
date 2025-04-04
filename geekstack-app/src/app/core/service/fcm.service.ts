import { Injectable } from '@angular/core';
import { getToken, Messaging } from '@angular/fire/messaging';
import { inject } from '@angular/core';
import { GeekstackService } from './geekstackdata.service';
import { UserStore } from '../store/user.store';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FireCloudMessaging {
  private messaging = inject(Messaging);

  constructor() {}

  // Request FCM token from Firebase
  async requestPermission(): Promise<string | null> {
    try {
      // Request Notification Permission
      const permission = await Notification.requestPermission();
      if (permission !== 'granted') {
        console.log('Notification permission not granted');
        return null;
      }

      // Get FCM Token
      const token = await getToken(this.messaging, {
        vapidKey: environment.vapidkey,
      });

      if (token) {
        return token;
      } else {
        console.error('No FCM token received');
        return null;
      }
    } catch (err) {
      console.error('Error getting FCM token:', err);
      return null;
    }
  }
}
