import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { UserStore } from '../../../../core/store/user.store';
import { MatIconModule } from '@angular/material/icon';
import { TabContentComponent } from '../tab-content/tab-content.component';
import { GSSqlUser } from '../../../../core/model/sql-user.model';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { Userpost } from '../../../../core/model/userpost.model';

@Component({
  selector: 'app-userdetails',
  standalone: true,
  imports: [MatIconModule, TabContentComponent, FormsModule],
  templateUrl: './userdetails.component.html',
  styleUrl: './userdetails.component.css',
})
export class UserdetailsComponent {
  @ViewChild('inputField') inputField!: ElementRef;

  isNameEdit: boolean = false;

  nameValue!: string;

  user!: GSSqlUser;

  listOfPostings!: Userpost[];

  private destroy$ = new Subject<void>();

  private userStore = inject(UserStore);

  constructor() {}

  ngOnInit() {
    this.userStore.gsSqlUser$.pipe(takeUntil(this.destroy$)).subscribe({
      next: (user) => {
        this.user = user ?? { userId: '', name: '', displaypic: '' };
        this.nameValue = this.user.name;
      },
    });
  }

  ngOnDestroy() {
    this.destroy$.next();  // Triggers unsubscription
    this.destroy$.complete();
  }

  onEdit() {
    this.isNameEdit = true;

    setTimeout(() => {
      const input = this.inputField.nativeElement;

      input.removeAttribute('readonly');
      input.setAttribute('autocorrect', 'off');
      input.setAttribute('autocapitalize', 'none');

      input.focus();
      input.select();
    }, 0);
  }

  onSave(confirmation: boolean) {
    if (confirmation) {
      // run save if fail return original name
      this.user.name = this.nameValue;
    } else {
      this.nameValue = this.user.name;
    }
    this.isNameEdit = false;
    
  }  
}
