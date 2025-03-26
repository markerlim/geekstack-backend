import {
  Component,
  HostListener,
  inject,
  Input,
  Output,
  SimpleChanges,
} from '@angular/core';
import { debounceTime, filter, Subject } from 'rxjs';
import { TcgImageComponent } from '../../../../shared/component/tcg-image/tcg-image.component';
import { CardRecord } from '../../../../core/model/card-record.model';
import { Userpost } from '../../../../core/model/userpost.model';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { CommentRecord } from '../../../../core/model/comment-record.model';
import { TimeAgoPipe } from '../../../../core/pipe/time-ago.pipe';
import { FormsModule } from '@angular/forms';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';
import { User } from 'firebase/auth';
import { GSSqlUser } from '../../../../core/model/sql-user.model';
import { MatDialog } from '@angular/material/dialog';
import { DeleteModalComponent } from '../delete-modal/delete-modal.component';

@Component({
  selector: 'app-detailstack',
  standalone: true,
  imports: [
    TcgImageComponent,
    CommonModule,
    MatIconModule,
    MatSidenavModule,
    FormsModule,
    TimeAgoPipe,
  ],
  templateUrl: './detailstack.component.html',
  styleUrl: './detailstack.component.css',
})
export class DetailstackComponent {
  @Input() isOpen = false;

  @Input()
  post!: Userpost;

  @Input()
  useFallBack: boolean = false;

  @Input()
  isLiked: boolean = false;

  @Input()
  user!: GSSqlUser;

  @Output()
  onLikingPost = new Subject<boolean>();

  listofcards: CardRecord[] = [];
  listofcomments: CommentRecord[] = [];
  listoflikes: string[] = [];

  userId: string = '';

  commentUserFallBack: boolean = false;

  isCommenting: boolean = false;

  commentText = '';

  formattedTimestamp: string = '';

  emojiList: string[] = ['😀', '😂', '😍', '🔥', '👍', '💯', '🥳', '🤔', '🎉'];

  @Output()
  onCloseStack = new Subject<boolean>();

  private geekstackService = inject(GeekstackService);
  private dialog = inject(MatDialog);
  private likeStateChange$ = new Subject<boolean>();
  private lastKnownLikeState: boolean | null = null;

  constructor() {}

  ngOnInit() {
    this.formatTimestamp();
    this.listofcomments = this.post.listofcomments || [];
    this.listoflikes = this.post.listoflikes || [];
    this.userId = this.user.userId;
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
        if (this.userId && this.post?.postId) {
          const serviceCall = finalLikeState
            ? this.geekstackService.likeUserPost(
                this.post.postId,
                this.post.userId,
                this.user
              )
            : this.geekstackService.unlikeUserPost(
                this.post.postId,
                this.userId
              );

          serviceCall.subscribe((response) => {
            console.log('Like/Unlike response: ', response);
            if (response) {
              this.lastKnownLikeState = finalLikeState;
              this.onLikingPost.next(finalLikeState);
            }
          });
        }
      });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['post'] && this.post) {
      this.listofcards = this.post.listofcards;
    }
  }

  addEmoji(emoji: string, event: MouseEvent) {
    event.stopPropagation();
    const textarea = document.querySelector('textarea') as HTMLTextAreaElement;

    if (textarea) {
      const start = textarea.selectionStart;
      const end = textarea.selectionEnd;

      this.commentText =
        this.commentText.slice(0, start) + emoji + this.commentText.slice(end);

      setTimeout(() => {
        textarea.focus();
        textarea.setSelectionRange(start + emoji.length, start + emoji.length);
      }, 0);
    }
  }

  onImageError() {
    this.commentUserFallBack = true;
  }

  likePost(event: Event) {
    event.stopPropagation();
    this.isLiked = !this.isLiked;

    if (this.isLiked) {
      if (!this.listoflikes.includes(this.userId)) {
        this.listoflikes.push(this.userId);
      }
    } else {
      const index = this.listoflikes.indexOf(this.userId);
      if (index !== -1) {
        this.listoflikes.splice(index, 1);
      }
    }
    this.onLikingPost.next(this.isLiked);
    this.likeStateChange$.next(this.isLiked);
  }

  commentPost(comment: string) {
    this.geekstackService
      .commentUserPost(this.post.postId, this.post.userId, this.user, comment)
      .subscribe((response) => {
        const newComment = {
          commentId: (response as { commentId: string }).commentId, // Type assertion
          comment: comment,
          userId: this.userId,
          timestamp: new Date().toISOString(),
          name: this.user.name ?? 'error',
          displaypic: this.user.displaypic ?? '',
        };

        // Add the new comment to the list to keep it in sync
        this.listofcomments.push(newComment);
        this.isCommenting = false;
      });
  }

  formatTimestamp() {
    if (!this.post.timestamp || !Array.isArray(this.post.timestamp)) {
      console.error('Invalid timestamp format:', this.post.timestamp);
      return;
    }
    console.log(this.post.timestamp);
    const [year, month, day, hour, minute, second] = this.post.timestamp;
    const date = new Date(year, month - 1, day, hour, minute, second);

    if (isNaN(date.getTime())) {
      console.error('Invalid date:', this.post.timestamp);
      return;
    }

    this.formattedTimestamp = date.toLocaleString();
  }

  scrollToSection() {
    document
      .getElementById('comment-section')
      ?.scrollIntoView({ behavior: 'smooth' });
  }

  openCommentDrawer() {
    console.log("trigger open")
    this.isCommenting = true;
  }
  onClose() {
    this.onCloseStack.next(false);
  }

  onOpenDeleteDialog(commentId: string) {
    const dialogRef = this.dialog.open(DeleteModalComponent, {
      width: '300px',
      data: { message: 'Are you sure you want to delete this item?' },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.geekstackService.deleteCommentUserPost(this.post.postId,commentId).subscribe({
          next: (res) => {
            console.log(res);    
            const index = this.listofcomments.findIndex(item => item.commentId === commentId);
            if (index !== -1) {
              this.listofcomments.splice(index, 1);
            }
          },
          error: (err) => {
            console.error(err);
          },
        });
      } else {
        console.log('User canceled the delete');
      }
    });
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    const commentDrawer = document.querySelector('.posting-comment');
    const commentButton = document.querySelector('.func-comment');
    const entireDrawer = document.querySelector('.detail-main');

    if (
      this.isCommenting &&
      commentDrawer &&
      !commentDrawer.contains(event.target as Node) &&
      !commentButton?.contains(event.target as Node)
    ) {
      this.isCommenting = false;
    }

    if (entireDrawer && !entireDrawer.contains(event.target as Node)) {
      this.isOpen = false;
      this.isCommenting = false;
      this.onClose();
    }
  }

  ngOnDestroy() {
    this.likeStateChange$.complete();
    this.onLikingPost.complete();
    this.onCloseStack.complete();
  }
}
