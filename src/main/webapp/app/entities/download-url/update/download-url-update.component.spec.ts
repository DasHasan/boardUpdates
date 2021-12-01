jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DownloadUrlService } from '../service/download-url.service';
import { IDownloadUrl, DownloadUrl } from '../download-url.model';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';

import { DownloadUrlUpdateComponent } from './download-url-update.component';

describe('Component Tests', () => {
  describe('DownloadUrl Management Update Component', () => {
    let comp: DownloadUrlUpdateComponent;
    let fixture: ComponentFixture<DownloadUrlUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let downloadUrlService: DownloadUrlService;
    let boardUpdateService: BoardUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DownloadUrlUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DownloadUrlUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DownloadUrlUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      downloadUrlService = TestBed.inject(DownloadUrlService);
      boardUpdateService = TestBed.inject(BoardUpdateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call boardUpdate query and add missing value', () => {
        const downloadUrl: IDownloadUrl = { id: 456 };
        const boardUpdate: IBoardUpdate = { id: 60600 };
        downloadUrl.boardUpdate = boardUpdate;

        const boardUpdateCollection: IBoardUpdate[] = [{ id: 58035 }];
        spyOn(boardUpdateService, 'query').and.returnValue(of(new HttpResponse({ body: boardUpdateCollection })));
        const expectedCollection: IBoardUpdate[] = [boardUpdate, ...boardUpdateCollection];
        spyOn(boardUpdateService, 'addBoardUpdateToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ downloadUrl });
        comp.ngOnInit();

        expect(boardUpdateService.query).toHaveBeenCalled();
        expect(boardUpdateService.addBoardUpdateToCollectionIfMissing).toHaveBeenCalledWith(boardUpdateCollection, boardUpdate);
        expect(comp.boardUpdatesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const downloadUrl: IDownloadUrl = { id: 456 };
        const boardUpdate: IBoardUpdate = { id: 77834 };
        downloadUrl.boardUpdate = boardUpdate;

        activatedRoute.data = of({ downloadUrl });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(downloadUrl));
        expect(comp.boardUpdatesCollection).toContain(boardUpdate);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const downloadUrl = { id: 123 };
        spyOn(downloadUrlService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ downloadUrl });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: downloadUrl }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(downloadUrlService.update).toHaveBeenCalledWith(downloadUrl);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const downloadUrl = new DownloadUrl();
        spyOn(downloadUrlService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ downloadUrl });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: downloadUrl }));
        saveSubject.complete();

        // THEN
        expect(downloadUrlService.create).toHaveBeenCalledWith(downloadUrl);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const downloadUrl = { id: 123 };
        spyOn(downloadUrlService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ downloadUrl });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(downloadUrlService.update).toHaveBeenCalledWith(downloadUrl);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBoardUpdateById', () => {
        it('Should return tracked BoardUpdate primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBoardUpdateById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
