jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BoardService } from '../service/board.service';
import { IBoard, Board } from '../board.model';
import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';
import { UpdatePreconditionService } from 'app/entities/update-precondition/service/update-precondition.service';

import { BoardUpdateComponent } from './board-update.component';

describe('Component Tests', () => {
  describe('Board Management Update Component', () => {
    let comp: BoardUpdateComponent;
    let fixture: ComponentFixture<BoardUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let boardService: BoardService;
    let updatePreconditionService: UpdatePreconditionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BoardUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BoardUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoardUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      boardService = TestBed.inject(BoardService);
      updatePreconditionService = TestBed.inject(UpdatePreconditionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call UpdatePrecondition query and add missing value', () => {
        const board: IBoard = { id: 456 };
        const updatePrecondition: IUpdatePrecondition = { id: 1056 };
        board.updatePrecondition = updatePrecondition;

        const updatePreconditionCollection: IUpdatePrecondition[] = [{ id: 47947 }];
        spyOn(updatePreconditionService, 'query').and.returnValue(of(new HttpResponse({ body: updatePreconditionCollection })));
        const additionalUpdatePreconditions = [updatePrecondition];
        const expectedCollection: IUpdatePrecondition[] = [...additionalUpdatePreconditions, ...updatePreconditionCollection];
        spyOn(updatePreconditionService, 'addUpdatePreconditionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ board });
        comp.ngOnInit();

        expect(updatePreconditionService.query).toHaveBeenCalled();
        expect(updatePreconditionService.addUpdatePreconditionToCollectionIfMissing).toHaveBeenCalledWith(
          updatePreconditionCollection,
          ...additionalUpdatePreconditions
        );
        expect(comp.updatePreconditionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const board: IBoard = { id: 456 };
        const updatePrecondition: IUpdatePrecondition = { id: 18278 };
        board.updatePrecondition = updatePrecondition;

        activatedRoute.data = of({ board });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(board));
        expect(comp.updatePreconditionsSharedCollection).toContain(updatePrecondition);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const board = { id: 123 };
        spyOn(boardService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ board });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: board }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(boardService.update).toHaveBeenCalledWith(board);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const board = new Board();
        spyOn(boardService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ board });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: board }));
        saveSubject.complete();

        // THEN
        expect(boardService.create).toHaveBeenCalledWith(board);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const board = { id: 123 };
        spyOn(boardService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ board });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(boardService.update).toHaveBeenCalledWith(board);
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
    });
  });
});
