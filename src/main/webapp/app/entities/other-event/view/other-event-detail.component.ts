import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import { IOtherEvent } from '../other-event.model';
import { FormatMediumDatePipe } from 'app/shared/date';

@Component({
  selector: 'jhi-other-event-detail',
  templateUrl: './other-event-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class OtherEventDetailComponent {
  otherEvent = input<IOtherEvent | null>(null);

  previousState(): void {
    window.history.back();
  }
}
