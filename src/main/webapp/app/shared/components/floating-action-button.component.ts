import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-floating-action-button',
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule],
  template: `
    <button class="btn-floating" [routerLink]="routerLink" [title]="title" (click)="onClick()">
      <fa-icon [icon]="icon"></fa-icon>
    </button>
  `,
  styles: [
    `
      .btn-floating {
        position: fixed;
        bottom: var(--spacing-lg);
        right: var(--spacing-lg);
        width: 56px;
        height: 56px;
        border-radius: 50%;
        background-color: var(--primary-color);
        color: var(--white);
        border: none;
        box-shadow: var(--shadow-lg);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24px;
        transition: all 0.2s ease;
        z-index: 1000;
        cursor: pointer;
      }

      .btn-floating:hover {
        background-color: #1d4ed8;
        transform: scale(1.1);
      }

      @media (max-width: 768px) {
        .btn-floating {
          bottom: var(--spacing-md);
          right: var(--spacing-md);
          width: 48px;
          height: 48px;
          font-size: 20px;
        }
      }
    `,
  ],
})
export class FloatingActionButtonComponent {
  @Input() routerLink: string = '';
  @Input() title: string = '';
  @Input() icon = faPlus;

  onClick(): void {
    // Дополнительная логика при клике
  }
}
