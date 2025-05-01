import { Component, input, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import SharedModule from 'app/shared/shared.module';
import { IPosition } from '../position.model';
import { IJobDescription } from '../../job-description/job-description.model';
import { JobDescriptionService } from '../../job-description/service/job-description.service';

@Component({
  selector: 'jhi-position-detail',
  templateUrl: './position-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PositionDetailComponent {
  position = input<IPosition | null>(null);
  loadedData = signal<{
    jobDescriptions: Record<number, IJobDescription | null>;
  }>({ jobDescriptions: {} });

  constructor(private jobDescriptionService: JobDescriptionService) {}

  ngOnInit() {
    const pos = this.position();
    if (pos?.jobDescription?.id) {
      this.jobDescriptionService.find(pos.jobDescription.id).subscribe({
        next: (response: HttpResponse<IJobDescription>) => {
          if (response.body) {
            this.loadedData.update(data => ({
              ...data,
              jobDescriptions: { ...data.jobDescriptions, [response.body!.id!]: response.body },
            }));
          }
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error loading job description:', error);
        },
      });
    }
  }

  get descriptionName(): string | null {
    const pos = this.position();
    if (!pos?.jobDescription?.id) return null;
    return this.loadedData().jobDescriptions[pos.jobDescription.id]?.descriptionName || `Job Description ${pos.jobDescription.id}`;
  }

  previousState(): void {
    window.history.back();
  }
}
