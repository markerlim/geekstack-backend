import { inject, Injectable } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class ScreenSizeService {
  isXSmallScreen$: Observable<boolean>;
  private breakpointObserver = inject(BreakpointObserver)
    constructor() {
    this.isXSmallScreen$ = this.breakpointObserver.observe([Breakpoints.XSmall])
      .pipe(map(result => result.matches));
  }
}
