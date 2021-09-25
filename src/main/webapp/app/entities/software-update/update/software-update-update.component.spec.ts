jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SoftwareUpdateService } from '../service/software-update.service';
import { ISoftwareUpdate, SoftwareUpdate } from '../software-update.model';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';
import { ISoftware } from 'app/entities/software/software.model';
import { SoftwareService } from 'app/entities/software/service/software.service';

import { SoftwareUpdateUpdateComponent } from './software-update-update.component';

describe('Component Tests', () => {
  describe('SoftwareUpdate Management Update Component', () => {
    let comp: SoftwareUpdateUpdateComponent;
    let fixture: ComponentFixture<SoftwareUpdateUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let softwareUpdateService: SoftwareUpdateService;
    let boardService: BoardService;
    let softwareService: SoftwareService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SoftwareUpdateUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SoftwareUpdateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SoftwareUpdateUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      softwareUpdateService = TestBed.inject(SoftwareUpdateService);
      boardService = TestBed.inject(BoardService);
      softwareService = TestBed.inject(SoftwareService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Board query and add missing value', () => {
        const softwareUpdate: ISoftwareUpdate = { id: 456 };
        const board: IBoard = { id: 28338 };
        softwareUpdate.board = board;

        const boardCollection: IBoard[] = [{ id: 64987 }];
        spyOn(boardService, 'query').and.returnValue(of(new HttpResponse({ body: boardCollection })));
        const additionalBoards = [board];
        const expectedCollection: IBoard[] = [...additionalBoards, ...boardCollection];
        spyOn(boardService, 'addBoardToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ softwareUpdate });
        comp.ngOnInit();

        expect(boardService.query).toHaveBeenCalled();
        expect(boardService.addBoardToCollectionIfMissing).toHaveBeenCalledWith(boardCollection, ...additionalBoards);
        expect(comp.boardsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Software query and add missing value', () => {
        const softwareUpdate: ISoftwareUpdate = { id: 456 };
        const from: ISoftware = { id: 46650 };
        softwareUpdate.from = from;
        const to: ISoftware = { id: 90555 };
        softwareUpdate.to = to;

        const softwareCollection: ISoftware[] = [{ id: 42738 }];
        spyOn(softwareService, 'query').and.returnValue(of(new HttpResponse({ body: softwareCollection })));
        const additionalSoftware = [from, to];
        const expectedCollection: ISoftware[] = [...additionalSoftware, ...softwareCollection];
        spyOn(softwareService, 'addSoftwareToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ softwareUpdate });
        comp.ngOnInit();

        expect(softwareService.query).toHaveBeenCalled();
        expect(softwareService.addSoftwareToCollectionIfMissing).toHaveBeenCalledWith(softwareCollection, ...additionalSoftware);
        expect(comp.softwareSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const softwareUpdate: ISoftwareUpdate = { id: 456 };
        const board: IBoard = { id: 55418 };
        softwareUpdate.board = board;
        const from: ISoftware = { id: 10302 };
        softwareUpdate.from = from;
        const to: ISoftware = { id: 7671 };
        softwareUpdate.to = to;

        activatedRoute.data = of({ softwareUpdate });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(softwareUpdate));
        expect(comp.boardsSharedCollection).toContain(board);
        expect(comp.softwareSharedCollection).toContain(from);
        expect(comp.softwareSharedCollection).toContain(to);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const softwareUpdate = { id: 123 };
        spyOn(softwareUpdateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ softwareUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: softwareUpdate }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(softwareUpdateService.update).toHaveBeenCalledWith(softwareUpdate);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const softwareUpdate = new SoftwareUpdate();
        spyOn(softwareUpdateService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ softwareUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: softwareUpdate }));
        saveSubject.complete();

        // THEN
        expect(softwareUpdateService.create).toHaveBeenCalledWith(softwareUpdate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const softwareUpdate = { id: 123 };
        spyOn(softwareUpdateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ softwareUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(softwareUpdateService.update).toHaveBeenCalledWith(softwareUpdate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBoardById', () => {
        it('Should return tracked Board primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBoardById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSoftwareById', () => {
        it('Should return tracked Software primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSoftwareById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
