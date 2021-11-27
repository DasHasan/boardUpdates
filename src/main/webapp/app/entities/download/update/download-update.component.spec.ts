jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DownloadService } from '../service/download.service';
import { IDownload, Download } from '../download.model';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';

import { DownloadUpdateComponent } from './download-update.component';

describe('Component Tests', () => {
  describe('Download Management Update Component', () => {
    let comp: DownloadUpdateComponent;
    let fixture: ComponentFixture<DownloadUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let downloadService: DownloadService;
    let boardUpdateService: BoardUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DownloadUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DownloadUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DownloadUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      downloadService = TestBed.inject(DownloadService);
      boardUpdateService = TestBed.inject(BoardUpdateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call boardUpdate query and add missing value', () => {
        const download: IDownload = { id: 456 };
        const boardUpdate: IBoardUpdate = { id: 79972 };
        download.boardUpdate = boardUpdate;

        const boardUpdateCollection: IBoardUpdate[] = [{ id: 69101 }];
        spyOn(boardUpdateService, 'query').and.returnValue(of(new HttpResponse({ body: boardUpdateCollection })));
        const expectedCollection: IBoardUpdate[] = [boardUpdate, ...boardUpdateCollection];
        spyOn(boardUpdateService, 'addBoardUpdateToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ download });
        comp.ngOnInit();

        expect(boardUpdateService.query).toHaveBeenCalled();
        expect(boardUpdateService.addBoardUpdateToCollectionIfMissing).toHaveBeenCalledWith(boardUpdateCollection, boardUpdate);
        expect(comp.boardUpdatesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const download: IDownload = { id: 456 };
        const boardUpdate: IBoardUpdate = { id: 62250 };
        download.boardUpdate = boardUpdate;

        activatedRoute.data = of({ download });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(download));
        expect(comp.boardUpdatesCollection).toContain(boardUpdate);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const download = { id: 123 };
        spyOn(downloadService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ download });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: download }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(downloadService.update).toHaveBeenCalledWith(download);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const download = new Download();
        spyOn(downloadService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ download });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: download }));
        saveSubject.complete();

        // THEN
        expect(downloadService.create).toHaveBeenCalledWith(download);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const download = { id: 123 };
        spyOn(downloadService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ download });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(downloadService.update).toHaveBeenCalledWith(download);
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
