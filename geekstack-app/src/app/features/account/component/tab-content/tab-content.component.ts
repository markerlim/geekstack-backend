import { CommonModule } from '@angular/common';
import { Component, inject, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Userpost } from '../../../../core/model/userpost.model';
import { SinglestackComponent } from "../../../post/component/singlestack/singlestack.component";
import { GSSqlUser } from '../../../../core/model/sql-user.model';
import { UserStore } from '../../../../core/store/user.store';
import { LeftstackComponent } from "../../../post/component/leftstack/leftstack.component";
import { RightstackComponent } from "../../../post/component/rightstack/rightstack.component";
import { GeekstackService } from '../../../../core/service/geekstackdata.service';

@Component({
    selector: 'app-tab-content',
    standalone: true,
    imports: [CommonModule, MatIconModule, LeftstackComponent, RightstackComponent],
    templateUrl: './tab-content.component.html',
    styleUrl: './tab-content.component.css'
})
export class TabContentComponent {
  isTab1: boolean = true;
  isTab2: boolean = false;

  listOfPostings: Userpost[] = [];
  listOfLikedPostings: Userpost[] = [];

  leftUserpost: Userpost[] = [];
  rightUserpost: Userpost[] = [];

  leftLiked: Userpost[] =[];
  rightLiked: Userpost[] = [];

  user!: GSSqlUser;
  private userStore = inject(UserStore);
  private geekstackService = inject(GeekstackService);

  constructor(){
    this.userStore.gsSqlUser$.subscribe({
      next: (user) =>{
        this.user = user ?? { userId: '', name: '', displaypic: '' };
        this.loadUserPosts();
        this.loadLikedUserPosts();
      }
    })
  }

  splitUserPosts(): void {
    if (!this.listOfPostings || this.listOfPostings.length === 0) {
      this.leftUserpost = [];
      this.rightUserpost = [];
      return;
    }
  
    this.leftUserpost = [];
    this.rightUserpost = [];
  
    this.listOfPostings.forEach((post, index) => {
      if (index % 2 === 0) {
        this.leftUserpost.push(post);
      } else {
        this.rightUserpost.push(post);
      }
    });
  }

  splitLikedUserPosts(): void {
    if (!this.listOfLikedPostings || this.listOfLikedPostings.length === 0) {
      this.leftLiked = [];
      this.rightLiked = [];
      return;
    }
  
    this.leftLiked = [];
    this.rightLiked = [];
  
    this.listOfLikedPostings.forEach((post, index) => {
      if (index % 2 === 0) {
        this.leftLiked.push(post);
      } else {
        this.rightLiked.push(post);
      }
    });
  }
  private loadUserPosts() {
    this.geekstackService.getUserPostByUserId(10, 0).subscribe({
      next: (list) => {
        this.listOfPostings = list;
        this.splitUserPosts()
      },
      error: (err) => {
        console.error('Failed to load posts:', err);
      },
    });
  }
  
  private loadLikedUserPosts() {
    this.geekstackService.getUserPostLikedById(10, 0).subscribe({
      next: (list) => {
        this.listOfLikedPostings = list;
        this.splitLikedUserPosts()
      },
      error: (err) => {
        console.error('Failed to load posts:', err);
      },
    });
  }
  onClickTab1(){
    this.isTab1 = true;
    this.isTab2 = false;
  }
  onClickTab2(){
    this.isTab2 = true;
    this.isTab1 = false;
  }
}
