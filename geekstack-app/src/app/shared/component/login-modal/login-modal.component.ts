import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { FireAuthService } from '../../../core/service/fireauth.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { GeekstackService } from '../../../core/service/geekstackdata.service';
import { UserStore } from '../../../core/store/user.store';
import { MatDialog } from '@angular/material/dialog';
import { CreateUserResponse } from '../../../core/model/create-user-response.model';
import { response } from 'express';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-modal',
  standalone: true,
  imports: [CommonModule, MatIconModule, ReactiveFormsModule],
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.css'],
})
export class LoginModalComponent implements OnInit {
  form!: FormGroup;

  private authService = inject(FireAuthService);
  private geekstackService = inject(GeekstackService);
  private dialog = inject(MatDialog);
  private userStore = inject(UserStore);
  private router = inject(Router);
  constructor() {}

  ngOnInit(): void {
    this.form = new FormGroup({
      email: new FormControl(''),
      password: new FormControl(''),
    });
  }

  stopPropagation(event: MouseEvent): void {
    event.stopPropagation();
  }

  async loginWithEmailAndPassword() {
    const { email, password } = this.form.value;
    try {
      const userCredential = await this.authService.signInWithEmailAndPassword(
        email,
        password
      );
      userCredential.user.getIdToken(true).then((idToken) => {
        this.geekstackService.createUser(idToken).subscribe({
          next: (response: CreateUserResponse) => {
            if (response.userObject) {
              this.userStore.login(response);
              const redirectUrl =
                localStorage.getItem('redirectUrl') || '/home';
              localStorage.removeItem('redirectUrl');
              this.router.navigateByUrl(redirectUrl);
            } else {
              console.warn('userObject not found in response');
            }

            this.dialog.closeAll();
          },
          error: (error) => {
            console.error('Error creating/checking user:', error);
          },
        });
      });
      this.dialog.closeAll();
    } catch (error) {
      console.error('Error during Google sign-in:', error);
    }
  }

  async signInWithGoogle() {
    try {
      const userCredential = await this.authService.signInWithGoogle();
      userCredential.user.getIdToken(true).then((idToken) => {
        this.geekstackService.createUser(idToken).subscribe({
          next: (response: CreateUserResponse) => {
            if (response.userObject) {
              this.userStore.login(response);
              const redirectUrl =
                localStorage.getItem('redirectUrl') || '/home';
              localStorage.removeItem('redirectUrl');
              this.router.navigateByUrl(redirectUrl);
            } else {
              console.warn('userObject not found in response');
            }

            this.dialog.closeAll();
          },
          error: (error) => {
            console.error('Error creating/checking user:', error);
          },
        });
      });
      this.dialog.closeAll();
    } catch (error) {
      console.error('Error during Google sign-in:', error);
    }
  }
  
}
