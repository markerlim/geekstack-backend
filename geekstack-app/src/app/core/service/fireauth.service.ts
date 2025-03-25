import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Capacitor } from '@capacitor/core';
import { FirebaseAuthentication } from '@capacitor-firebase/authentication';
import {
  Auth,
  authState,
  createUserWithEmailAndPassword,
  GoogleAuthProvider,
  signInWithCredential,
  signInWithEmailAndPassword,
  signInWithPopup,
  signOut,
  updateProfile,
  User,
  UserCredential,
} from '@angular/fire/auth';

@Injectable({
  providedIn: 'root',
})
export class FireAuthService {
  private currentUserSubject: BehaviorSubject<User | null> =
    new BehaviorSubject<User | null>(null);
  public currentUser: Observable<User | null> =
    this.currentUserSubject.asObservable();
  private auth = inject(Auth);
  constructor() {
    authState(this.auth).subscribe((user: User | null) => {
      this.currentUserSubject.next(user);
    });
  }

  getCurrentUser(): Observable<User | null> {
    return this.currentUserSubject.asObservable();
  }

  public async signUpWithEmailAndPassword(
    email: string,
    password: string,
    displayName: string
  ): Promise<UserCredential> {
    const cred = await createUserWithEmailAndPassword(
      this.auth,
      email,
      password
    );
    const photoURL = '/images/gsdp.png';
    await updateProfile(cred.user, { displayName, photoURL });
    this.currentUserSubject.next(cred.user);
    return cred;
  }

  public async signInWithEmailAndPassword(
    email: string,
    password: string
  ): Promise<UserCredential> {
    try {
      const cred = await signInWithEmailAndPassword(this.auth, email, password);
      this.currentUserSubject.next(cred.user);
      return cred;
    } catch (error) {
      const errorCode = (error as any).code;
      const errorMessage = (error as any).message;
      console.error('Authentication error:', errorCode, errorMessage);
      throw error;
    }
  }
  
  public async signInWithGoogle(): Promise<UserCredential> {
    try {
      if (Capacitor.isNativePlatform()) {
        // Sign in with Google using Capacitor Firebase Auth
        const result = await FirebaseAuthentication.signInWithGoogle();
  
        // Verify we got the required credential
        if (!result.credential?.idToken) {
          throw new Error('Google login failed - missing idToken');
        }
  
        // Create Firebase credential
        const credential = GoogleAuthProvider.credential(
          result.credential.idToken
        );
  
        // Sign in to Firebase
        return await signInWithCredential(this.auth, credential);
      } else {
        // Web implementation fallback
        const provider = new GoogleAuthProvider();
        return await signInWithPopup(this.auth, provider);
      }
    } catch (error) {
      console.error('Google sign-in error:', error);
      throw error;
    }
  }
  public async signOut(): Promise<void> {
    await signOut(this.auth);
    this.currentUserSubject.next(null);
  }
}
