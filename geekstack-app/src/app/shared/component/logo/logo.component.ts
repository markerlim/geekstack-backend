import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-logo',
    standalone: true,
    imports: [],
    templateUrl: './logo.component.html',
    styleUrl: './logo.component.css'
})
export class LogoComponent {
  @Input() width: string = "379";  // Default width
  @Input() height: string = "418"; // Default height
  @Input() viewBox: string = '0 0 379 418'; // Default viewBox
}
