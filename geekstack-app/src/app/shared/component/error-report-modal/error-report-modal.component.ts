import { Component, inject, Inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { GeekstackService } from '../../../core/service/geekstackdata.service';

@Component({
  selector: 'app-error-report-modal',
  imports: [MatDialogModule,ReactiveFormsModule],
  templateUrl: './error-report-modal.component.html',
  styleUrl: './error-report-modal.component.css'
})
export class ErrorReportModalComponent {

  errorForm!: FormGroup;

  private fb = inject(FormBuilder);
  private geekstackService = inject(GeekstackService);
  dialogRef = inject(MatDialogRef<ErrorReportModalComponent>);
  data = inject(MAT_DIALOG_DATA);
  constructor() {
    this.errorForm = this.fb.group({
      errorMessage: ['', Validators.required]
    });
  }

  onCancel():void{
    this.dialogRef.close(false);
  }
  onSubmit(): void {
    if (this.errorForm.valid) {
      let userId = this.data.userId;
      if(this.data.userId === "error"){
        userId = "anonymous"
      }
      this.geekstackService.sendReportError(userId,this.data.cardUid,this.errorForm.value.errorMessage).subscribe({
        next: (response) =>{
          this.dialogRef.close({
            response
          });
        },
        error: (err) =>{
          console.error(err)
        }
      })
    }
  }
}

