import { Component, Input, Output } from '@angular/core';
import { SinglestackComponent } from '../singlestack/singlestack.component';
import { Userpost } from '../../../../core/model/userpost.model';
import { GSSqlUser } from '../../../../core/model/sql-user.model';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-rightstack',
  standalone: true,
  imports: [SinglestackComponent],
  templateUrl: './rightstack.component.html',
  styleUrl: './rightstack.component.css',
})
export class RightstackComponent {
  @Input()
  userpost: Userpost[] = [];

  @Input()
  user!: GSSqlUser;

  @Output() removeDeck = new Subject<string>();

  onDataReceivedFromSinglestacks(data: string){
    this.removeDeck.next(data);
  }
}
