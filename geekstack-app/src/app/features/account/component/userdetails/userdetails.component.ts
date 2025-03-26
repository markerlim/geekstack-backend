import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { UserStore } from '../../../../core/store/user.store';
import { MatIconModule } from '@angular/material/icon';
import { TabContentComponent } from '../tab-content/tab-content.component';
import { GSSqlUser } from '../../../../core/model/sql-user.model';
import { FormGroup, FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { Userpost } from '../../../../core/model/userpost.model';
import { GeekstackService } from '../../../../core/service/geekstackdata.service';

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

  imageSrc!: string;
  selectedFile: File | null = null;

  listOfPostings!: Userpost[];

  private destroy$ = new Subject<void>();

  private userStore = inject(UserStore);
  private geekstackService = inject(GeekstackService);
  constructor() {}

  ngOnInit() {
    this.userStore.gsSqlUser$.pipe(takeUntil(this.destroy$)).subscribe({
      next: (user) => {
        this.user = user ?? { userId: '', name: '', displaypic: '' };
        this.nameValue = this.user.name;
        this.imageSrc = this.user.displaypic;
      },
    });
  }

  ngOnDestroy() {
    this.destroy$.next(); // Triggers unsubscription
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

  upload() {
    if (!this.selectedFile) {
      return;
    }

    this.geekstackService
      .editDisplayPicOfUser(this.user.userId, this.selectedFile)
      .subscribe({
        next: (result) => {
          const resultObject = JSON.parse(JSON.stringify(result));
          this.imageSrc = resultObject.fileUrl; // Assuming the fileUrl is returned
          this.user.displaypic = this.imageSrc;
          this.userStore.setGSSqlUser(this.user);
        },
        error: (err) => {
          console.error('Upload failed:', err);
        },
      });
  }

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0]; // Store the File object directly
      console.log(
        'Selected file:',
        this.selectedFile.name,
        this.selectedFile.size,
        this.selectedFile.type
      );

      // For preview only
      const reader = new FileReader();
      reader.onload = () => {
        this.imageSrc = reader.result as string;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onSave(confirmation: boolean) {
    if (confirmation) {
      if (this.user.name != this.nameValue) {
        this.user.name = this.nameValue;
        this.geekstackService.editNameOfUser(this.nameValue, this.user.userId).subscribe({
          next: (response) =>{
            console.log(response);
            this.userStore.setGSSqlUser(this.user);
          }
        });
      }
      if (this.imageSrc != this.user.displaypic) {
        this.upload();
      }
    } else {
      this.nameValue = this.user.name;
    }
    this.isNameEdit = false;
  }
}
