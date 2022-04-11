jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BoardUpdateService } from '../service/board-update.service';
import { IBoardUpdate, BoardUpdate } from '../board-update.model';
import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';
import { UpdatePreconditionService } from 'app/entities/update-precondition/service/update-precondition.service';
import { IDownloadUrl } from 'app/entities/download-url/download-url.model';
import { DownloadUrlService } from 'app/entities/download-url/service/download-url.service';

import { BoardUpdateUpdateComponent } from './board-update-update.component';

describe('Component Tests', () => {
  describe('BoardUpdate Management Update Component', () => {
    let comp: BoardUpdateUpdateComponent;
    let fixture: ComponentFixture<BoardUpdateUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let boardUpdateService: BoardUpdateService;
    let updatePreconditionService: UpdatePreconditionService;
    let downloadUrlService: DownloadUrlService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BoardUpdateUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BoardUpdateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoardUpdateUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      boardUpdateService = TestBed.inject(BoardUpdateService);
      updatePreconditionService = TestBed.inject(UpdatePreconditionService);
      downloadUrlService = TestBed.inject(DownloadUrlService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call updatePrecondition query and add missing value', () => {
        const boardUpdate: IBoardUpdate = { id: 456 };
        const updatePrecondition: IUpdatePrecondition = { id: 1056 };
        boardUpdate.updatePrecondition = updatePrecondition;

        const updatePreconditionCollection: IUpdatePrecondition[] = [{ id: 47947 }];
        spyOn(updatePreconditionService, 'query').and.returnValue(of(new HttpResponse({ body: updatePreconditionCollection })));
        const expectedCollection: IUpdatePrecondition[] = [updatePrecondition, ...updatePreconditionCollection];
        spyOn(updatePreconditionService, 'addUpdatePreconditionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ boardUpdate });
        comp.ngOnInit();

        expect(updatePreconditionService.query).toHaveBeenCalled();
        expect(updatePreconditionService.addUpdatePreconditionToCollectionIfMissing).toHaveBeenCalledWith(
          updatePreconditionCollection,
          updatePrecondition
        );
        expect(comp.updatePreconditionsCollection).toEqual(expectedCollection);
      });

      it('Should call downloadUrl query and add missing value', () => {
        const boardUpdate: IBoardUpdate = { id: 456 };
        const downloadUrl: IDownloadUrl = { id: 24171 };
        boardUpdate.downloadUrl = downloadUrl;

        const downloadUrlCollection: IDownloadUrl[] = [{ id: 65876 }];
        spyOn(downloadUrlService, 'query').and.returnValue(of(new HttpResponse({ body: downloadUrlCollection })));
        const expectedCollection: IDownloadUrl[] = [downloadUrl, ...downloadUrlCollection];
        spyOn(downloadUrlService, 'addDownloadUrlToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ boardUpdate });
        comp.ngOnInit();

        expect(downloadUrlService.query).toHaveBeenCalled();
        expect(downloadUrlService.addDownloadUrlToCollectionIfMissing).toHaveBeenCalledWith(downloadUrlCollection, downloadUrl);
        expect(comp.downloadUrlsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const boardUpdate: IBoardUpdate = { id: 456 };
        const updatePrecondition: IUpdatePrecondition = { id: 18278 };
        boardUpdate.updatePrecondition = updatePrecondition;
        const downloadUrl: IDownloadUrl = { id: 58458 };
        boardUpdate.downloadUrl = downloadUrl;

        activatedRoute.data = of({ boardUpdate });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(boardUpdate));
        expect(comp.updatePreconditionsCollection).toContain(updatePrecondition);
        expect(comp.downloadUrlsCollection).toContain(downloadUrl);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUpdate = { id: 123 };
        spyOn(boardUpdateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: boardUpdate }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(boardUpdateService.update).toHaveBeenCalledWith(boardUpdate);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUpdate = new BoardUpdate();
        spyOn(boardUpdateService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: boardUpdate }));
        saveSubject.complete();

        // THEN
        expect(boardUpdateService.create).toHaveBeenCalledWith(boardUpdate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUpdate = { id: 123 };
        spyOn(boardUpdateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(boardUpdateService.update).toHaveBeenCalledWith(boardUpdate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUpdatePreconditionById', () => {
        it('Should return tracked UpdatePrecondition primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUpdatePreconditionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDownloadUrlById', () => {
        it('Should return tracked DownloadUrl primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDownloadUrlById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
