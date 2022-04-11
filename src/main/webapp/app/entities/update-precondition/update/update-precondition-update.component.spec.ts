jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UpdatePreconditionService } from '../service/update-precondition.service';
import { IUpdatePrecondition, UpdatePrecondition } from '../update-precondition.model';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';

import { UpdatePreconditionUpdateComponent } from './update-precondition-update.component';

describe('Component Tests', () => {
  describe('UpdatePrecondition Management Update Component', () => {
    let comp: UpdatePreconditionUpdateComponent;
    let fixture: ComponentFixture<UpdatePreconditionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let updatePreconditionService: UpdatePreconditionService;
    let boardUpdateService: BoardUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UpdatePreconditionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UpdatePreconditionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UpdatePreconditionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      updatePreconditionService = TestBed.inject(UpdatePreconditionService);
      boardUpdateService = TestBed.inject(BoardUpdateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call BoardUpdate query and add missing value', () => {
        const updatePrecondition: IUpdatePrecondition = { id: 456 };
        const boardUpdate: IBoardUpdate = { id: 79972 };
        updatePrecondition.boardUpdate = boardUpdate;

        const boardUpdateCollection: IBoardUpdate[] = [{ id: 69101 }];
        spyOn(boardUpdateService, 'query').and.returnValue(of(new HttpResponse({ body: boardUpdateCollection })));
        const additionalBoardUpdates = [boardUpdate];
        const expectedCollection: IBoardUpdate[] = [...additionalBoardUpdates, ...boardUpdateCollection];
        spyOn(boardUpdateService, 'addBoardUpdateToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ updatePrecondition });
        comp.ngOnInit();

        expect(boardUpdateService.query).toHaveBeenCalled();
        expect(boardUpdateService.addBoardUpdateToCollectionIfMissing).toHaveBeenCalledWith(
          boardUpdateCollection,
          ...additionalBoardUpdates
        );
        expect(comp.boardUpdatesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const updatePrecondition: IUpdatePrecondition = { id: 456 };
        const boardUpdate: IBoardUpdate = { id: 62250 };
        updatePrecondition.boardUpdate = boardUpdate;

        activatedRoute.data = of({ updatePrecondition });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(updatePrecondition));
        expect(comp.boardUpdatesSharedCollection).toContain(boardUpdate);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const updatePrecondition = { id: 123 };
        spyOn(updatePreconditionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ updatePrecondition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: updatePrecondition }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(updatePreconditionService.update).toHaveBeenCalledWith(updatePrecondition);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const updatePrecondition = new UpdatePrecondition();
        spyOn(updatePreconditionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ updatePrecondition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: updatePrecondition }));
        saveSubject.complete();

        // THEN
        expect(updatePreconditionService.create).toHaveBeenCalledWith(updatePrecondition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const updatePrecondition = { id: 123 };
        spyOn(updatePreconditionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ updatePrecondition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(updatePreconditionService.update).toHaveBeenCalledWith(updatePrecondition);
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
