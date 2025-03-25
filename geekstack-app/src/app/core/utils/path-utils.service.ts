import { inject, Injectable } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { filter } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class PathUtilsService {
  private allowedPaths: string[] = ['/home', '/dashboard'];
  private isGrayscaleSubject = new BehaviorSubject<boolean>(false);

  isGrayscale$ = this.isGrayscaleSubject.asObservable();
  private router = inject(Router)
  constructor() {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        const currentPath = this.router.url;
        const isGrayscale = !this.allowedPaths.includes(currentPath);
        this.isGrayscaleSubject.next(isGrayscale);
      });
  }
}
