import { CommonModule, Location } from '@angular/common';
import { Component, inject, Input, Output, ViewChild } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { DetailstackComponent } from '../detailstack/detailstack.component';
import { Userpost } from '../../../../core/model/userpost.model';
import { GSSqlUser } from '../../../../core/model/sql-user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { DeleteModalComponent } from '../delete-modal/delete-modal.component';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';
import { debounceTime, filter, Subject } from 'rxjs';
import { LoginModalComponent } from '../../../../shared/component/login-modal/login-modal.component';

@Component({
  selector: 'app-singlestack',
  standalone: true,
  imports: [CommonModule, MatIconModule, DetailstackComponent],
  templateUrl: './singlestack.component.html',
  styleUrl: './singlestack.component.css',
})
export class SinglestackComponent {
  @ViewChild('detailStack') detailStack!: DetailstackComponent;

  isLiked: boolean = false;
  isDetailStackOpen: boolean = false;
  isSlideReady: boolean = false;
  imageSrc?: string = '';
  useFallBack: boolean = false;

  @Input() post!: Userpost;

  @Input() user!: GSSqlUser;

  @Output() removeDeck = new Subject<string>();

  @Output() postClosed = new Subject<boolean>();

  private likeStateChange$ = new Subject<boolean>();
  private lastKnownLikeState: boolean | null = null;

  private location = inject(Location);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private dialog = inject(MatDialog);
  private geekstackService = inject(GeekstackService);

  constructor() {}

  ngOnInit() {
    this.imageSrc = this.post.selectedCards.at(0)?.imageSrc;
    this.isLiked = this.post.listoflikes?.includes(this.user.userId) ?? false;

    this.route.paramMap.subscribe((params) => {
      const postId = params.get('postId');
      if (postId && this.post.postId === postId) {
        this.onClickPost(postId);
      }
    });

    this.lastKnownLikeState = this.isLiked;

    this.likeStateChange$
      .pipe(
        debounceTime(2000),
        filter(
          () =>
            this.lastKnownLikeState !== null &&
            this.lastKnownLikeState !== undefined
        ),
        filter((currentState) => currentState !== this.lastKnownLikeState)
      )
      .subscribe((finalLikeState) => {
        if (this.user.userId && this.post?.postId) {
          const serviceCall = finalLikeState
            ? this.geekstackService.likeUserPost(
                this.post.postId,
                this.post.userId,
                this.user
              )
            : this.geekstackService.unlikeUserPost(
                this.post.postId,
                this.user.userId
              );

          serviceCall.subscribe((response) => {
            console.log('Like/Unlike response: ', response);
            if (response) {
              this.lastKnownLikeState = finalLikeState;
            }
          });
        }
      });
  }

  commentPost(event: Event) {
    event.stopPropagation();

    if (this.user.userId == 'error') {
      alert('You are not signed in!')
      this.dialog.open(LoginModalComponent, { width: 'fit-content' });
      return;
    }

    this.onClickPost(this.post.postId);
    setTimeout(() => {
      if (this.detailStack) {
        this.detailStack.openCommentDrawer();
        this.detailStack.scrollToSection();
      } else {
        console.error('Detail stack not available');
      }
    }, 100);
  }

  likePost(event: Event) {
    event.stopPropagation();
    if (this.user.userId == 'error') {
      alert('You are not signed in!')
      this.dialog.open(LoginModalComponent, { width: 'fit-content' });
      return;
    }
    this.isLiked = !this.isLiked;

    if (this.isLiked) {
      if (!this.post.listoflikes.includes(this.user.userId)) {
        this.post.listoflikes.push(this.user.userId);
      }
    } else {
      const index = this.post.listoflikes.indexOf(this.user.userId);
      if (index !== -1) {
        this.post.listoflikes.splice(index, 1);
      }
    }
    this.likeStateChange$.next(this.isLiked);
  }

  onLikingPost(bool: boolean) {
    this.isLiked = bool;
  }

  onSharePost(event: Event) {
    event.stopPropagation();
    const url = window.location.href + '/' + this.post.postId;
    navigator.clipboard
      .writeText(url)
      .then(() => {
        console.log('URL copied to clipboard!');
        alert('URL copied to clipboard!');
      })
      .catch((err) => {
        console.error('Failed to copy URL: ', err);
      });
  }

  onClickPost(postId: string) {
    this.location.replaceState(`/stacks/${postId}`);
    this.isDetailStackOpen = true;
    setTimeout(() => {
      this.isSlideReady = true;
    }, 50);
  }

  onImageError() {
    this.useFallBack = true;
  }

  onClosePost() {
    this.router.navigate(['/stacks']).then(() => {
      this.isSlideReady = false;
      this.postClosed.next(false);
      setTimeout(() => {
        this.isDetailStackOpen = false;
      }, 500);
    });
  }
  onOpenDeleteDialog(event: any) {
    event.stopPropagation();
    const dialogRef = this.dialog.open(DeleteModalComponent, {
      width: '300px',
      data: { message: 'Are you sure you want to delete this item?' },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.geekstackService.deletePostByUser(this.post.postId).subscribe({
          next: (res) => {
            console.log(res);
            this.removeDeck.next(this.post.postId);
          },
          error: (err) => {
            console.error(err);
          },
        });
      } else {
        console.log('User canceled the delete');
        // Handle the cancellation logic
      }
    });
  }
}
