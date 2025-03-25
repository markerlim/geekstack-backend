import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { LoginModalComponent } from '../../shared/component/login-modal/login-modal.component';
import { Auth, authState } from '@angular/fire/auth';

@Injectable({
  providedIn: 'root',
})
export class RouteGuardService implements CanActivate {
  private auth = inject(Auth);
  private dialog = inject(MatDialog);

  constructor() {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {

    return authState(this.auth).pipe(
      take(1), // Get the first emitted value
      map((user) => {
        if (user) {
          console.log('User logged in:', user);
          return true;
        } else {
          localStorage.setItem('redirectUrl', state.url);
          this.dialog.open(LoginModalComponent, { width: 'fit-content' });
          return false;
        }
      })
    );
  }
}
