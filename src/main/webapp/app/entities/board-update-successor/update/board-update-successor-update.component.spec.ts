jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BoardUpdateSuccessorService } from '../service/board-update-successor.service';
import { IBoardUpdateSuccessor, BoardUpdateSuccessor } from '../board-update-successor.model';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';

import { BoardUpdateSuccessorUpdateComponent } from './board-update-successor-update.component';

describe('Component Tests', () => {
  describe('BoardUpdateSuccessor Management Update Component', () => {
    let comp: BoardUpdateSuccessorUpdateComponent;
    let fixture: ComponentFixture<BoardUpdateSuccessorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let boardUpdateSuccessorService: BoardUpdateSuccessorService;
    let boardUpdateService: BoardUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BoardUpdateSuccessorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BoardUpdateSuccessorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoardUpdateSuccessorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      boardUpdateSuccessorService = TestBed.inject(BoardUpdateSuccessorService);
      boardUpdateService = TestBed.inject(BoardUpdateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call from query and add missing value', () => {
        const boardUpdateSuccessor: IBoardUpdateSuccessor = { id: 456 };
        const from: IBoardUpdate = { id: 79972 };
        boardUpdateSuccessor.from = from;

        const fromCollection: IBoardUpdate[] = [{ id: 69101 }];
        spyOn(boardUpdateService, 'query').and.returnValue(of(new HttpResponse({ body: fromCollection })));
        const expectedCollection: IBoardUpdate[] = [from, ...fromCollection];
        spyOn(boardUpdateService, 'addBoardUpdateToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ boardUpdateSuccessor });
        comp.ngOnInit();

        expect(boardUpdateService.query).toHaveBeenCalled();
        expect(boardUpdateService.addBoardUpdateToCollectionIfMissing).toHaveBeenCalledWith(fromCollection, from);
        expect(comp.fromsCollection).toEqual(expectedCollection);
      });

      it('Should call to query and add missing value', () => {
        const boardUpdateSuccessor: IBoardUpdateSuccessor = { id: 456 };
        const to: IBoardUpdate = { id: 62250 };
        boardUpdateSuccessor.to = to;

        const toCollection: IBoardUpdate[] = [{ id: 12066 }];
        spyOn(boardUpdateService, 'query').and.returnValue(of(new HttpResponse({ body: toCollection })));
        const expectedCollection: IBoardUpdate[] = [to, ...toCollection];
        spyOn(boardUpdateService, 'addBoardUpdateToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ boardUpdateSuccessor });
        comp.ngOnInit();

        expect(boardUpdateService.query).toHaveBeenCalled();
        expect(boardUpdateService.addBoardUpdateToCollectionIfMissing).toHaveBeenCalledWith(toCollection, to);
        expect(comp.tosCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const boardUpdateSuccessor: IBoardUpdateSuccessor = { id: 456 };
        const from: IBoardUpdate = { id: 1506 };
        boardUpdateSuccessor.from = from;
        const to: IBoardUpdate = { id: 74155 };
        boardUpdateSuccessor.to = to;

        activatedRoute.data = of({ boardUpdateSuccessor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(boardUpdateSuccessor));
        expect(comp.fromsCollection).toContain(from);
        expect(comp.tosCollection).toContain(to);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUpdateSuccessor = { id: 123 };
        spyOn(boardUpdateSuccessorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUpdateSuccessor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: boardUpdateSuccessor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(boardUpdateSuccessorService.update).toHaveBeenCalledWith(boardUpdateSuccessor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUpdateSuccessor = new BoardUpdateSuccessor();
        spyOn(boardUpdateSuccessorService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUpdateSuccessor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: boardUpdateSuccessor }));
        saveSubject.complete();

        // THEN
        expect(boardUpdateSuccessorService.create).toHaveBeenCalledWith(boardUpdateSuccessor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUpdateSuccessor = { id: 123 };
        spyOn(boardUpdateSuccessorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUpdateSuccessor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(boardUpdateSuccessorService.update).toHaveBeenCalledWith(boardUpdateSuccessor);
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
