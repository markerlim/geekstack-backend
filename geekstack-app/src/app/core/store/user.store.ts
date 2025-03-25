import { inject, Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { Observable } from 'rxjs';
import { Auth, authState, User } from '@angular/fire/auth';
import { GSMongoUser } from '../model/mongo-user.model';
import { GSSqlUser } from '../model/sql-user.model';
import { CreateUserResponse } from '../model/create-user-response.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface UserState {
  gsMongoUser: GSMongoUser | null; // MongoDB user details
  gsSqlUser: GSSqlUser | null; // SQL user details
}

@Injectable({
  providedIn: 'root',
})
export class UserStore extends ComponentStore<UserState> {
  private http = inject(HttpClient);
  private auth = inject(Auth);
  private baseURL = environment.baseUrl;
  private baseURLuser = `${this.baseURL}/user`;

  readonly gsMongoUser$: Observable<GSMongoUser | null> = this.select(
    (state) => state.gsMongoUser
  );
  readonly gsSqlUser$: Observable<GSSqlUser | null> = this.select(
    (state) => state.gsSqlUser
  );

  constructor() {
    super({ gsMongoUser: null, gsSqlUser: null });

    authState(this.auth).subscribe((user: User | null) => {
      if (user) {
        console.log('User logged in:', user);
        this.loadUserData(user);
      } else {
        console.log('User logged out');
        this.clearAll();
      }
    });

    this.loadUserFromLocalStorage();
  }

  /** Updates MongoDB user details */
  readonly setGSMongoUser = this.updater(
    (state: UserState, gsMongoUser: GSMongoUser | null) => ({
      ...state,
      gsMongoUser,
    })
  );

  /** Updates SQL user details */
  readonly setGSSqlUser = this.updater(
    (state: UserState, gsSqlUser: GSSqlUser | null) => ({
      ...state,
      gsSqlUser,
    })
  );

  /** Fetch MongoDB & SQL user details from backend */
  private loadUserData(authUser: User) {
    console.log('activating load user:');
    authUser.getIdToken(true).then((idToken) => {
      this.http
        .post<CreateUserResponse>(this.baseURLuser + '/get', idToken)
        .subscribe({
          next: (response: CreateUserResponse) => {
            this.setGSMongoUser(response.userObject.gsMongoUser);
            this.setGSSqlUser(response.userObject.gsSQLUser);
            localStorage.setItem(
              'mongoUser',
              JSON.stringify(response.userObject.gsMongoUser)
            );
            localStorage.setItem(
              'sqlUser',
              JSON.stringify(response.userObject.gsSQLUser)
            );
          },
          error: (err: any) => console.error('Error fetching Mongo user:', err),
        });
    });
  }

  /** Loads user data from local storage on startup */
  private loadUserFromLocalStorage() {
    const storedMongoUser = localStorage.getItem('mongoUser');
    const storedSqlUser = localStorage.getItem('sqlUser');
    if (storedMongoUser) {
      this.setGSMongoUser(JSON.parse(storedMongoUser));
    }
    if (storedSqlUser) {
      this.setGSSqlUser(JSON.parse(storedSqlUser));
    }
  }

  login(response: CreateUserResponse) {
    this.setGSMongoUser(response.userObject.gsMongoUser);
    this.setGSSqlUser(response.userObject.gsSQLUser);
    localStorage.setItem(
      'mongoUser',
      JSON.stringify(response.userObject.gsMongoUser)
    );
    localStorage.setItem(
      'sqlUser',
      JSON.stringify(response.userObject.gsSQLUser)
    );
  }

  getCurrentUser(): GSSqlUser {
    return (
      this.get().gsSqlUser ?? {
        userId: 'error',
        name: 'error',
        displaypic: 'error',
      }
    );
  }

  private clearAll() {
    this.setGSMongoUser(null);
    this.setGSSqlUser(null);
    localStorage.removeItem('mongoUser');
    localStorage.removeItem('sqlUser');
    console.log('Signed out');
  }
}
