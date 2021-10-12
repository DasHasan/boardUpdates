jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UpdateKeysService } from '../service/update-keys.service';
import { IUpdateKeys, UpdateKeys } from '../update-keys.model';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';

import { UpdateKeysUpdateComponent } from './update-keys-update.component';

describe('Component Tests', () => {
  describe('UpdateKeys Management Update Component', () => {
    let comp: UpdateKeysUpdateComponent;
    let fixture: ComponentFixture<UpdateKeysUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let updateKeysService: UpdateKeysService;
    let boardUpdateService: BoardUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UpdateKeysUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UpdateKeysUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UpdateKeysUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      updateKeysService = TestBed.inject(UpdateKeysService);
      boardUpdateService = TestBed.inject(BoardUpdateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call BoardUpdate query and add missing value', () => {
        const updateKeys: IUpdateKeys = { id: 456 };
        const boardUpdate: IBoardUpdate = { id: 64326 };
        updateKeys.boardUpdate = boardUpdate;

        const boardUpdateCollection: IBoardUpdate[] = [{ id: 42511 }];
        spyOn(boardUpdateService, 'query').and.returnValue(of(new HttpResponse({ body: boardUpdateCollection })));
        const additionalBoardUpdates = [boardUpdate];
        const expectedCollection: IBoardUpdate[] = [...additionalBoardUpdates, ...boardUpdateCollection];
        spyOn(boardUpdateService, 'addBoardUpdateToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ updateKeys });
        comp.ngOnInit();

        expect(boardUpdateService.query).toHaveBeenCalled();
        expect(boardUpdateService.addBoardUpdateToCollectionIfMissing).toHaveBeenCalledWith(
          boardUpdateCollection,
          ...additionalBoardUpdates
        );
        expect(comp.boardUpdatesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const updateKeys: IUpdateKeys = { id: 456 };
        const boardUpdate: IBoardUpdate = { id: 85517 };
        updateKeys.boardUpdate = boardUpdate;

        activatedRoute.data = of({ updateKeys });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(updateKeys));
        expect(comp.boardUpdatesSharedCollection).toContain(boardUpdate);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const updateKeys = { id: 123 };
        spyOn(updateKeysService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ updateKeys });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: updateKeys }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(updateKeysService.update).toHaveBeenCalledWith(updateKeys);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const updateKeys = new UpdateKeys();
        spyOn(updateKeysService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ updateKeys });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: updateKeys }));
        saveSubject.complete();

        // THEN
        expect(updateKeysService.create).toHaveBeenCalledWith(updateKeys);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const updateKeys = { id: 123 };
        spyOn(updateKeysService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ updateKeys });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(updateKeysService.update).toHaveBeenCalledWith(updateKeys);
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
