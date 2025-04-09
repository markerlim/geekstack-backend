import {
  Component,
  ElementRef,
  QueryList,
  ViewChild,
  ViewChildren,
  AfterViewInit,
  OnDestroy,
  inject,
  OnInit,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { LeftstackComponent } from '../leftstack/leftstack.component';
import { RightstackComponent } from '../rightstack/rightstack.component';
import { LoaderComponent } from '../../../../shared/component/loader/loader.component';
import { LogoComponent } from '../../../../shared/component/logo/logo.component';
import { Userpost } from '../../../../core/model/userpost.model';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';
import { MatIconModule } from '@angular/material/icon';
import { UserStore } from '../../../../core/store/user.store';
import { ActivatedRoute, Router } from '@angular/router';
import { GSSqlUser } from '../../../../core/model/sql-user.model';
import { debounceTime, Subject, switchMap, takeUntil, tap } from 'rxjs';
import { SinglestackComponent } from '../singlestack/singlestack.component';

@Component({
  selector: 'app-deckstacks',
  standalone: true,
  imports: [
    CommonModule,
    LeftstackComponent,
    RightstackComponent,
    LoaderComponent,
    LogoComponent,
    MatIconModule,
    SinglestackComponent,
  ],
  templateUrl: './deckstacks.component.html',
  styleUrl: './deckstacks.component.css',
})
export class DeckstacksComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('scrollContainer') scrollContainer!: ElementRef;
  @ViewChild('contentSlide') contentSlide!: ElementRef;
  @ViewChild('stacksContent') stacksContent!: ElementRef;
  @ViewChildren('contentItem') contentItems!: QueryList<ElementRef>;

  isSlideVisible = true;
  touchY: number | null = null;

  listofcontent: string[] = [
    'ALL',
    'UNIONARENA',
    'ONEPIECE',
    'COOKIERUNBRAVERSE',
    'DRAGONBALLZ',
  ];
  selectedContent: string = 'ALL';
  userpost: Userpost[] = [];
  leftUserpost: Userpost[] = [];
  rightUserpost: Userpost[] = [];
  isLoading: boolean = false;
  postIdExist: boolean = false;
  postIdContent!: Userpost;
  postIdUser!: GSSqlUser;
  initPostId!: string;
  user!: GSSqlUser;
  limit: number = 20;
  page: number = 0;
  private destroy$ = new Subject<void>();

  private geekstackService = inject(GeekstackService);
  private userStore = inject(UserStore);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private scrollSubject = new Subject<void>();

  constructor() {
    this.initPostId = this.route.snapshot.paramMap.get('postId') ?? '';

    this.route.paramMap.subscribe((params) => {
      const postId = params.get('postId');
      if (postId) {
        this.geekstackService.getUserPostByPostId(postId).subscribe({
          next: (response) => {
            this.postIdContent = response;
            this.postIdUser = {
              userId: this.postIdContent.userId,
              name: this.postIdContent.name,
              displaypic: this.postIdContent.displaypic,
            };
            this.postIdExist = true;
          },
          error: (err) => {
            console.error(err);
            this.postIdExist = false;
          },
        });
      } else {
        this.postIdExist = false;
        this.postIdContent = {} as Userpost;
      }
    });
  }
  ngOnInit() {
    this.fetchUserpost();
    this.userStore.gsSqlUser$.pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this.user = res
          ? res
          : { userId: 'noUserId', name: 'error', displaypic: 'string' };
      },
    });  }

  ngAfterViewInit() {
    setTimeout(() => this.centerSelectedItem(), 0);

    const scrollContainer = this.stacksContent.nativeElement;
    if (scrollContainer) {
      scrollContainer.addEventListener(
        'touchmove',
        this.handleTouchMove.bind(this)
      );
      scrollContainer.addEventListener(
        'touchend',
        this.handleTouchEnd.bind(this)
      );

      scrollContainer.addEventListener('scroll', this.onScroll.bind(this));
    }

    this.scrollSubject
      .pipe(
        tap(() => {
          if (!this.isLoading) {
            this.isLoading = true;
            console.log(this.isLoading);
          }
        }),
        debounceTime(1000),
        switchMap(async () => this.fetchUserpost())
      )
      .subscribe();
  }

  navigateToPostStacks() {
    this.router.navigate(['/poststacks']);
  }

  onPostExit(result: boolean) {
    console.log('POST EXIT>>>>>:' + result);
    console.log('INIT: ');
    this.postIdExist = result;
    this.postIdContent = {} as Userpost;
  }

  fetchUserpost(): void {
    if (this.selectedContent === 'ALL') {
      this.geekstackService.getUserPost(this.limit, this.page).subscribe({
        next: (data) => {
          this.userpost = [...this.userpost, ...data];
          this.splitUserPosts();
          this.page += 1;
        },
        error: (err) => {
          console.error('Failed to fetch user list:', err);
        },
        complete: () => {
          this.isLoading = false;
        },
      });
    } else {
      this.geekstackService.getUserPostByType(this.limit, this.page, this.selectedContent.toLowerCase()).subscribe({
        next: (data) => {
          this.userpost = [...this.userpost, ...data];
          this.splitUserPosts();
          this.page += 1;
        },
        error: (err) => {
          console.error('Failed to fetch user list:', err);
        },
        complete: () => {
          this.isLoading = false;
        },
      });
    }
  }

  splitUserPosts(): void {
    this.leftUserpost = [];
    this.rightUserpost = [];

    this.userpost.forEach((post, index) => {
      if (index % 2 === 0) {
        this.leftUserpost.push(post);
      } else {
        this.rightUserpost.push(post);
      }
    });
  }

  clickContent(value: string) {
    this.selectedContent = value;
    this.page = 0;
    this.userpost = [];
    this.fetchUserpost();
    this.centerSelectedItem();
  }

  centerSelectedItem() {
    const container = this.scrollContainer.nativeElement;
    const items = this.contentItems.toArray();

    const selectedIndex = this.listofcontent.indexOf(this.selectedContent);

    if (selectedIndex >= 0 && items[selectedIndex]) {
      const selectedElement = items[selectedIndex].nativeElement;
      const containerWidth = container.offsetWidth;
      const itemWidth = selectedElement.offsetWidth;
      const itemLeft = selectedElement.offsetLeft;

      const scrollPosition = itemLeft - containerWidth / 2 + itemWidth / 2;

      container.scrollTo({
        left: scrollPosition,
        behavior: 'smooth',
      });
    }
  }

  handleTouchMove(event: TouchEvent) {
    const currentTouchY = event.touches[0].clientY;

    if (this.touchY !== null) {
      const deltaY = this.touchY - currentTouchY;

      if (deltaY > 0) {
        this.isSlideVisible = false; // Hide on scroll down
      } else if (deltaY < 0) {
        this.isSlideVisible = true; // Show on scroll up
      }
    }

    this.touchY = currentTouchY;
  }

  handleTouchEnd() {
    this.touchY = null;
  }

  onScroll() {
    const scrollContainer = this.stacksContent.nativeElement;
    const nearBottom =
      scrollContainer.scrollHeight - scrollContainer.scrollTop <=
      scrollContainer.clientHeight + 200; // 100px before the bottom

    if (nearBottom && !this.isLoading) {
      this.scrollSubject.next();
    }
  }

  onDeleteDataReceived(data: string) {
    const index = this.userpost.findIndex((item) => item.postId === data);
    if (index !== -1) {
      this.userpost.splice(index, 1);
    }
    this.splitUserPosts();
  }

  ngOnDestroy() {
    const scrollContainer = this.stacksContent.nativeElement;
    if (scrollContainer) {
      scrollContainer.removeEventListener(
        'touchmove',
        this.handleTouchMove.bind(this)
      );
      scrollContainer.removeEventListener(
        'touchend',
        this.handleTouchEnd.bind(this)
      );
      scrollContainer.removeEventListener('scroll', this.onScroll.bind(this));
    }
    this.destroy$.next();
    this.destroy$.complete();
  }
}
