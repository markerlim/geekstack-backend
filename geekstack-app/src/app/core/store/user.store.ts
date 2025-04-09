import { inject, Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { Observable, of } from 'rxjs';
import { Auth, authState, User } from '@angular/fire/auth';
import { GSMongoUser } from '../model/mongo-user.model';
import { GSSqlUser } from '../model/sql-user.model';
import { CreateUserResponse } from '../model/create-user-response.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ListOfDecks } from '../model/listofdecks.model';
import { TCGTYPE } from '../utils/constants';

export interface UserState {
  gsMongoUser: GSMongoUser | null; // MongoDB user details
  gsSqlUser: GSSqlUser | null; // SQL user details
  uaDecks: ListOfDecks[]; // Union Arena (derived from mongoUser.uadecks)
  opDecks: ListOfDecks[]; // One Piece (derived from mongoUser.opdecks)
  crbDecks: ListOfDecks[];
  dmDecks: ListOfDecks[];
  dbzfwDecks: ListOfDecks[];
  hocgDecks: ListOfDecks[];
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
  readonly uaDecks$ = this.select((state) => state.uaDecks);
  readonly opDecks$ = this.select((state) => state.opDecks);
  readonly crbDecks$ = this.select((state) => state.crbDecks);
  readonly dmDecks$ = this.select((state) => state.dmDecks);
  readonly dbzfwDecks$ = this.select((state) => state.dbzfwDecks);
  readonly hocgDecks$ = this.select((state) => state.hocgDecks);

  constructor() {
    super({
      gsMongoUser: null,
      gsSqlUser: null,
      uaDecks: [],
      opDecks: [],
      crbDecks: [],
      dmDecks: [],
      dbzfwDecks: [],
      hocgDecks: [],
    });

    authState(this.auth).subscribe((user: User | null) => {
      if (user) {
        console.log('User logged in:', user);
        this.loadUserData(user); // always load fresh from backend
      } else {
        console.log('User logged out');
        this.clearAll();
      }
    });
  }

  /** Updates MongoDB user details */
  readonly setGSMongoUser = this.updater(
    (state: UserState, gsMongoUser: GSMongoUser | null) => ({
      ...state,
      gsMongoUser,
      uaDecks: gsMongoUser?.uadecks || [],
      opDecks: gsMongoUser?.opdecks || [],
      crbDecks: gsMongoUser?.crbdecks || [],
      dmDecks: gsMongoUser?.dmdecks || [],
      dbzfwDecks: gsMongoUser?.dbzfwdecks || [],
      hocgDecks: gsMongoUser?.hocgdecks || [],
    })
  );

  /** Updates SQL user details */
  readonly setGSSqlUser = this.updater(
    (state: UserState, gsSqlUser: GSSqlUser | null) => ({
      ...state,
      gsSqlUser,
    })
  );

  readonly saveDeck = this.updater<{ deck: ListOfDecks; tcg: string }>(
    (state, { deck, tcg }) => {
      if (!state.gsMongoUser) {
        console.error('No user data in store');
        return state;
      }
      const clonedDeck = JSON.parse(JSON.stringify(deck));
      const tcgLower = tcg.toLowerCase();

      switch (tcgLower) {
        case TCGTYPE.UNIONARENA:
          state.gsMongoUser.uadecks = [
            ...(state.gsMongoUser.uadecks || []),
            clonedDeck,
          ];
          break;
        case TCGTYPE.ONEPIECE:
          state.gsMongoUser.opdecks = [
            ...(state.gsMongoUser.opdecks || []),
            clonedDeck,
          ];
          break;
        case TCGTYPE.COOKIERUN:
          state.gsMongoUser.crbdecks = [
            ...(state.gsMongoUser.crbdecks || []),
            clonedDeck,
          ];
          break;
        case TCGTYPE.DUELMASTERS:
          state.gsMongoUser.dmdecks = [
            ...(state.gsMongoUser.dmdecks || []),
            clonedDeck,
          ];
          break;
        case TCGTYPE.DRAGONBALLZFW:
          state.gsMongoUser.dbzfwdecks = [
            ...(state.gsMongoUser.dbzfwdecks || []),
            clonedDeck,
          ];
          break;
        case TCGTYPE.HOLOLIVE:
          state.gsMongoUser.hocgdecks = [
            ...(state.gsMongoUser.hocgdecks || []),
            clonedDeck,
          ];
          break;
        default:
          console.error('Unsupported TCG:', tcg);
      }

      return {
        ...state,
        gsMongoUser: state.gsMongoUser,
        ...(tcgLower === TCGTYPE.UNIONARENA && {
          uaDecks: state.gsMongoUser.uadecks,
        }),
        ...(tcgLower === TCGTYPE.ONEPIECE && {
          opDecks: state.gsMongoUser.opdecks,
        }),
        ...(tcgLower === TCGTYPE.COOKIERUN && {
          crbDecks: state.gsMongoUser.crbdecks,
        }),
        ...(tcgLower === TCGTYPE.DRAGONBALLZFW && {
          dbzfwDecks: state.gsMongoUser.dbzfwdecks,
        }),
        ...(tcgLower === TCGTYPE.DUELMASTERS && {
          dmDecks: state.gsMongoUser.dmdecks,
        }),
        ...(tcgLower === TCGTYPE.HOLOLIVE && {
          dmDecks: state.gsMongoUser.hocgdecks,
        }),
      };
    }
  );

  readonly deleteDeck = this.updater<{ deckuid: string; tcg: string }>(
    (state, { deckuid, tcg }) => {
      if (!state.gsMongoUser) {
        console.error('No user data in store');
        return state;
      }

      const tcgLower = tcg.toLowerCase();

      switch (tcgLower) {
        case TCGTYPE.UNIONARENA:
          state.gsMongoUser.uadecks =
            state.gsMongoUser.uadecks?.filter((d) => d.deckuid !== deckuid) ||
            [];
          break;
        case TCGTYPE.ONEPIECE:
          state.gsMongoUser.opdecks =
            state.gsMongoUser.opdecks?.filter((d) => d.deckuid !== deckuid) ||
            [];
          break;
        case TCGTYPE.COOKIERUN:
          state.gsMongoUser.crbdecks =
            state.gsMongoUser.crbdecks?.filter((d) => d.deckuid !== deckuid) ||
            [];
          break;
        case TCGTYPE.DUELMASTERS:
          state.gsMongoUser.dmdecks =
            state.gsMongoUser.dmdecks?.filter((d) => d.deckuid !== deckuid) ||
            [];
          break;
        case TCGTYPE.DRAGONBALLZFW:
          state.gsMongoUser.dbzfwdecks =
            state.gsMongoUser.dbzfwdecks?.filter(
              (d) => d.deckuid !== deckuid
            ) || [];
          break;
        case TCGTYPE.HOLOLIVE:
          state.gsMongoUser.hocgdecks =
            state.gsMongoUser.hocgdecks?.filter((d) => d.deckuid !== deckuid) ||
            [];
          break;
        default:
          console.error('Unsupported TCG:', tcg);
      }

      return {
        ...state,
        gsMongoUser: state.gsMongoUser,
        ...(tcgLower === TCGTYPE.UNIONARENA && {
          uaDecks: state.gsMongoUser.uadecks,
        }),
        ...(tcgLower === TCGTYPE.ONEPIECE && {
          opDecks: state.gsMongoUser.opdecks,
        }),
        ...(tcgLower === TCGTYPE.COOKIERUN && {
          crbDecks: state.gsMongoUser.crbdecks,
        }),
        ...(tcgLower === TCGTYPE.DRAGONBALLZFW && {
          dbzfwDecks: state.gsMongoUser.dbzfwdecks,
        }),
        ...(tcgLower === TCGTYPE.DUELMASTERS && {
          dmDecks: state.gsMongoUser.dmdecks,
        }),
        ...(tcgLower === TCGTYPE.HOLOLIVE && {
          dmDecks: state.gsMongoUser.hocgdecks,
        }),
      };
    }
  );

  /** Load fresh Mongo + SQL user data */
  private loadUserData(authUser: User) {
    console.log('Activating loadUserData...');
    authUser.getIdToken(true).then((idToken) => {
      this.http
        .post<CreateUserResponse>(this.baseURLuser + '/get', idToken)
        .subscribe({
          next: (response: CreateUserResponse) => {
            this.setGSMongoUser(response.userObject.gsMongoUser);
            this.setGSSqlUser(response.userObject.gsSQLUser);
          },
          error: (err: any) => console.error('Error fetching user:', err),
        });
    });
  }

  login(response: CreateUserResponse) {
    this.setGSMongoUser(response.userObject.gsMongoUser);
    this.setGSSqlUser(response.userObject.gsSQLUser);
  }

  getDecks(tcg: string): Observable<ListOfDecks[]> {
    switch (tcg) {
      case TCGTYPE.UNIONARENA:
        return this.uaDecks$;
      case TCGTYPE.ONEPIECE:
        return this.opDecks$;
      case TCGTYPE.COOKIERUN:
        return this.crbDecks$;
      case TCGTYPE.DUELMASTERS:
        return this.dmDecks$;
      case TCGTYPE.DRAGONBALLZFW:
        return this.dbzfwDecks$;
      case TCGTYPE.HOLOLIVE:
        return this.hocgDecks$;
      default:
        return of([]);
    }
  }

  private clearAll() {
    this.setGSMongoUser(null);
    this.setGSSqlUser(null);
    console.log('Signed out');
  }
}
