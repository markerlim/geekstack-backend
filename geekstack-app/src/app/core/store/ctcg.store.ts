import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { Observable } from 'rxjs';

export interface TcgState {
  currentTcg: string;
}

@Injectable({
  providedIn: 'root',
})
export class TcgStore extends ComponentStore<TcgState> {
  readonly currentTcg$: Observable<string> = this.select(
    (state) => state.currentTcg
  );

  constructor() {
    super({ currentTcg: '' });
  }

  readonly setTcg = this.updater((state: TcgState, newTcg: string) => ({
    ...state,
    currentTcg: newTcg,
  }));


  setCurrentTCG(tcg: string) {
    this.setTcg(tcg);
  }

  getCurrentTcg() {
    return this.get().currentTcg;
  }
}
