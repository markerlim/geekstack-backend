import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CardUnionArena } from '../model/card-unionarena.model';
import { CardOnePiece } from '../model/card-onepiece.model';
import { CardDragonBallZFW } from '../model/card-dragonballzfw.model';
import { CookieRunCard } from '../model/card-cookierunbraverse.model';
import { Userpost } from '../model/userpost.model';
import { CreateUserResponse } from '../model/create-user-response.model';
import { GSSqlUser } from '../model/sql-user.model';
import { Notifications } from '../model/notifications.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class GeekstackService {
  private http = inject(HttpClient);
  constructor() {}

  private baseURL = environment.baseUrl;
  private baseURLdata = `${this.baseURL}/data`;
  private baseURLuser = `${this.baseURL}/user`;
  private baseURLboosterlist = `${this.baseURL}/boosterlist`;
  private baseURLuserpost = `${this.baseURL}/userpost`;

  ///${this.tcgPath}/filter
  getBoosterOfTcg(
    tcgPath: string
  ): Observable<
    Array<{ pathname: string; alt: string; imageSrc: string; imgWidth: number }>
  > {
    return this.http.get<
      Array<{
        pathname: string;
        alt: string;
        imageSrc: string;
        imgWidth: number;
      }>
    >(this.baseURLboosterlist + '/' + tcgPath);
  }

  getCardlistOfTcgOfBooster(
    tcgPath: string,
    booster: string
  ): Observable<
    Array<CardUnionArena | CardOnePiece | CardDragonBallZFW | CookieRunCard>
  > {
    return this.http.get<
      Array<CardUnionArena | CardOnePiece | CardDragonBallZFW | CookieRunCard>
    >(this.baseURLdata + '/' + tcgPath + '/' + booster);
  }

  getTcgSetFilter(
    tcgPath: string,
    filterType: string,
    booster: string
  ): Observable<string[]> {
    return this.http.get<string[]>(
      this.baseURLdata + '/' + tcgPath + '/filter/' + filterType + '/' + booster
    );
  }

  searchCard(tcg: string, term: string): Observable<CardUnionArena[]> {
    return this.http.get<CardUnionArena[]>(
      this.baseURLdata + '/' + tcg + '/search/' + term
    );
  }

  getUserPost(limit: number, page: number) {
    return this.http.get<Userpost[]>(
      this.baseURLuserpost + '?' + 'limit=' + limit + '&' + 'page=' + page
    );
  }

  getUserPostById(limit: number, page: number) {
    return this.http.get<Userpost[]>(
      this.baseURLuserpost +
        '/listuserpostings?limit=' +
        limit +
        '&' +
        'page=' +
        page
    );
  }

  getUserPostLikedById(limit: number, page: number) {
    return this.http.get<Userpost[]>(
      this.baseURLuserpost +
        '/listoflikedpost?limit=' +
        limit +
        '&' +
        'page=' +
        page
    );
  }

  postByUser(payload: any) {
    return this.http.post(this.baseURLuserpost + '/post', payload);
  }

  deletePostByUser(postId: string) {
    return this.http.delete(this.baseURLuserpost + '/delete/' + postId);
  }

  commentUserPost(
    postId: string,
    posterId: string,
    user: GSSqlUser,
    comment: string
  ) {
    return this.http.post(this.baseURLuserpost + '/comment', {
      postId: postId,
      posterId: posterId,
      user: user,
      comment: comment,
    });
  }

  deleteCommentUserPost(postId: string, commentId: string) {
    return this.http.delete(
      this.baseURLuserpost +
        '/comment/' +
         postId +
        '/delete/' +
        commentId
    );
  }

  likeUserPost(postId: string, posterId: string, user: GSSqlUser) {
    return this.http.post(this.baseURLuserpost + '/like', {
      postId: postId,
      posterId: posterId,
      user: user,
    });
  }

  unlikeUserPost(postId: string, userId: string) {
    return this.http.delete(
      this.baseURLuserpost + '/unlike/' + postId + '/by/' + userId
    );
  }

  userLoadListOfDeck(userId: string, tcg: string): Observable<any[]> {
    return this.http.get<any[]>(
      this.baseURLuser + '/load/' + tcg + '/' + userId + '/deck'
    );
  }

  userSaveDeck(userId: string, decklist: any, tcg: string, deckuid: string) {
    console.log(deckuid);
    if (deckuid) {
      return this.http.post(
        this.baseURLuser +
          '/save/' +
          tcg +
          '/' +
          userId +
          '/deck?deckuid=' +
          deckuid,
        decklist
      );
    }
    return this.http.post(
      this.baseURLuser + '/save/' + tcg + '/' + userId + '/deck',
      decklist
    );
  }

  userDeleteDeck(
    userId: string,
    deckId: string,
    tcg: string
  ): Observable<Object> {
    return this.http.delete(
      this.baseURLuser + '/delete/' + tcg + '/' + userId + '/deck/' + deckId
    );
  }

  createUser(payload: any): Observable<CreateUserResponse> {
    return this.http.post<CreateUserResponse>(
      this.baseURLuser + '/create',
      payload
    );
  }

  verfiyUser(token: string): Observable<string> {
    return this.http.post<string>(this.baseURLuser + '/verify-token', token);
  }

  getUser(userId: string): Observable<CreateUserResponse> {
    return this.http.post<CreateUserResponse>(
      this.baseURLuser + '/get',
      userId
    );
  }

  getNotificationsForUser(): Observable<Notifications[]> {
    return this.http.get<Notifications[]>(this.baseURLuser + '/notifications');
  }

  sendReportError(userId: string, cardUid: string, errorMsg: string){
    return this.http.post(this.baseURLuser+"/report-error",{
      userId: userId,
      cardUid: cardUid,
      errorMsg: errorMsg
    })
  }

  getExchangeRate() : Observable<string>{
    return this.http.get<string>(this.baseURLuser+"/getExcRate");
  }
}
