import { HttpInterceptorFn } from '@angular/common/http';
import { getAuth, User } from '@angular/fire/auth';
import { from, of, switchMap } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  console.log('Interceptor called for URL:', req.url);

  const auth = getAuth();
  const user: User | null = auth.currentUser;
  if (req.url.includes('/api/user/update/image')) {
    console.log('Skipping authentication for:', req.url);
    return next(req);
  }

  if (!user) {
    console.warn('No authenticated user found.');
    return next(req);
  }

  return from(user.getIdToken(false)).pipe(
    switchMap((cachedToken) => {
      if (cachedToken) {
        console.log('Using cached token');
        return of(cachedToken);
      }
      console.log('Token expired, requesting new one');
      return from(user.getIdToken(true));
    }),
    switchMap((token) => {
      const clonedRequest = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'X-Requested-With': 'XMLHttpRequest',
        },
      });
      console.log("Cloned Request Headers: ", clonedRequest.headers);
      return next(clonedRequest);
    })
  );
};
