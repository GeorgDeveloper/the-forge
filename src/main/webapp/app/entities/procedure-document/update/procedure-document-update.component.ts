import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfession } from 'app/entities/profession/profession.model';
import { ProfessionService } from 'app/entities/profession/service/profession.service';
import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';
import { ProcedureDocumentService } from '../service/procedure-document.service';
import { IProcedureDocument } from '../procedure-document.model';
import { ProcedureDocumentFormGroup, ProcedureDocumentFormService } from './procedure-document-form.service';

@Component({
  selector: 'jhi-procedure-document-update',
  templateUrl: './procedure-document-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProcedureDocumentUpdateComponent implements OnInit {
  isSaving = false;
  procedureDocument: IProcedureDocument | null = null;
  selectedFile: File | null = null;

  professionsSharedCollection: IProfession[] = [];
  positionsSharedCollection: IPosition[] = [];

  protected procedureDocumentService = inject(ProcedureDocumentService);
  protected procedureDocumentFormService = inject(ProcedureDocumentFormService);
  protected professionService = inject(ProfessionService);
  protected positionService = inject(PositionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProcedureDocumentFormGroup = this.procedureDocumentFormService.createProcedureDocumentFormGroup();

  compareProfession = (o1: IProfession | null, o2: IProfession | null): boolean => this.professionService.compareProfession(o1, o2);

  comparePosition = (o1: IPosition | null, o2: IPosition | null): boolean => this.positionService.comparePosition(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ procedureDocument }) => {
      this.procedureDocument = procedureDocument;
      if (procedureDocument) {
        this.updateForm(procedureDocument);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const procedureDocument = this.procedureDocumentFormService.getProcedureDocument(this.editForm);
    if (procedureDocument.id !== null) {
      this.subscribeToSaveResponse(this.procedureDocumentService.update(procedureDocument));
    } else {
      this.subscribeToSaveResponse(this.procedureDocumentService.create(procedureDocument));
    }
  }

  onFileSelected(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      const file = target.files[0];

      // Проверка типа файла
      if (file.type !== 'application/pdf') {
        alert('Пожалуйста, выберите PDF-файл');
        target.value = '';
        return;
      }

      // Проверка размера файла (50MB = 50 * 1024 * 1024 байт)
      const maxSize = 50 * 1024 * 1024;
      if (file.size > maxSize) {
        alert(`Размер файла не должен превышать 50MB. Текущий размер: ${this.formatFileSize(file.size)}`);
        target.value = '';
        return;
      }

      this.selectedFile = file;
    }
  }

  clearFile(): void {
    this.selectedFile = null;
    const fileInput = document.getElementById('field_pdfFile') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }

  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  downloadPdf(): void {
    if (this.procedureDocument?.id) {
      this.procedureDocumentService.downloadPdfFile(this.procedureDocument.id).subscribe({
        next: blob => {
          const url = window.URL.createObjectURL(blob);
          const link = window.document.createElement('a');
          link.href = url;
          link.download = this.procedureDocument?.pdfFileName || 'procedure-document.pdf';
          link.click();
          window.URL.revokeObjectURL(url);
        },
        error: () => {
          alert('Ошибка при скачивании файла');
        },
      });
    }
  }

  deletePdf(): void {
    if (this.procedureDocument?.id && confirm('Вы уверены, что хотите удалить PDF-файл?')) {
      this.procedureDocumentService.deletePdfFile(this.procedureDocument.id).subscribe({
        next: response => {
          this.procedureDocument = response.body;
          alert('PDF-файл успешно удален');
        },
        error: () => {
          alert('Ошибка при удалении файла');
        },
      });
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcedureDocument>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    if (this.selectedFile && this.procedureDocument?.id) {
      this.procedureDocumentService.uploadPdfFile(this.procedureDocument.id, this.selectedFile).subscribe({
        next: () => {
          this.previousState();
        },
        error: () => {
          alert('Ошибка при загрузке PDF-файла');
          this.previousState();
        },
      });
    } else {
      this.previousState();
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(procedureDocument: IProcedureDocument): void {
    this.procedureDocument = procedureDocument;
    this.procedureDocumentFormService.resetForm(this.editForm, procedureDocument);

    this.professionsSharedCollection = this.professionService.addProfessionToCollectionIfMissing<IProfession>(
      this.professionsSharedCollection,
      procedureDocument.profession,
    );
    this.positionsSharedCollection = this.positionService.addPositionToCollectionIfMissing<IPosition>(
      this.positionsSharedCollection,
      procedureDocument.position,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.professionService
      .query()
      .pipe(map((res: HttpResponse<IProfession[]>) => res.body ?? []))
      .pipe(
        map((professions: IProfession[]) =>
          this.professionService.addProfessionToCollectionIfMissing<IProfession>(professions, this.procedureDocument?.profession),
        ),
      )
      .subscribe((professions: IProfession[]) => (this.professionsSharedCollection = professions));

    this.positionService
      .query()
      .pipe(map((res: HttpResponse<IPosition[]>) => res.body ?? []))
      .pipe(
        map((positions: IPosition[]) =>
          this.positionService.addPositionToCollectionIfMissing<IPosition>(positions, this.procedureDocument?.position),
        ),
      )
      .subscribe((positions: IPosition[]) => (this.positionsSharedCollection = positions));
  }
}
