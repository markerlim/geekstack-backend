import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { FireAuthService } from '../../../core/service/fireauth.service';
import { ScreenSizeService } from '../../../core/service/screen-size.service';
import { GeekstackService } from '../../../core/service/geekstackdata.service';
import { CreateUserResponse } from '../../../core/model/create-user-response.model';
import { UserStore } from '../../../core/store/user.store';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  form!: FormGroup;
  backgroundImage: string = '/images/signupbg.jpg';

  private fb = inject(FormBuilder);
  private authService = inject(FireAuthService);
  private screenSizeService = inject(ScreenSizeService);
  private geekstackService = inject(GeekstackService);
  private userStore = inject(UserStore);
  private dialog = inject(MatDialog);
  private router = inject(Router);
  constructor() {
    this.createForm();
    this.screenSizeService.isXSmallScreen$.subscribe((isSmall) => {
      if (isSmall) {
        this.backgroundImage = '/images/signupbg-portrait.jpg';
      }
    });
  }

  private createForm() {
    this.form = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(7)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: [
        '',
        [Validators.required, this.passwordMatchValidator.bind(this)],
      ],
    });
  }

  private passwordMatchValidator() {
    if (this.form) {
      const password = this.form.controls['password'].value;
      const confirmPassword = this.form.controls['confirmPassword'].value;
      return password === confirmPassword ? null : { mismatch: true };
    }
    return null;
  }

  onSubmit() {
    if (this.form.valid) {
      this.signUpWithEmailAndPassword();
    } else {
      alert('Please fix the errors before proceeding');
    }
  }

  async signUpWithEmailAndPassword() {
    const { username, email, password } = this.form.value;
    try {
      const userCredential = await this.authService.signUpWithEmailAndPassword(
        email,
        password,
        username
      );
      userCredential.user.getIdToken(true).then((idToken) => {
        this.geekstackService.createUser(idToken).subscribe({
          next: (response: CreateUserResponse) => {
            if (response.userObject) {
              this.userStore.login(response);
              this.router.navigateByUrl('/');
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
    } catch {}
  }
}
