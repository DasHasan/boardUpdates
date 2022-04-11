jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UpdateKeysService } from '../service/update-keys.service';
import { IUpdateKeys, UpdateKeys } from '../update-keys.model';
import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';
import { UpdatePreconditionService } from 'app/entities/update-precondition/service/update-precondition.service';

import { UpdateKeysUpdateComponent } from './update-keys-update.component';

describe('Component Tests', () => {
  describe('UpdateKeys Management Update Component', () => {
    let comp: UpdateKeysUpdateComponent;
    let fixture: ComponentFixture<UpdateKeysUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let updateKeysService: UpdateKeysService;
    let updatePreconditionService: UpdatePreconditionService;

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
      updatePreconditionService = TestBed.inject(UpdatePreconditionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call UpdatePrecondition query and add missing value', () => {
        const updateKeys: IUpdateKeys = { id: 456 };
        const updatePrecondition: IUpdatePrecondition = { id: 14589 };
        updateKeys.updatePrecondition = updatePrecondition;

        const updatePreconditionCollection: IUpdatePrecondition[] = [{ id: 68815 }];
        spyOn(updatePreconditionService, 'query').and.returnValue(of(new HttpResponse({ body: updatePreconditionCollection })));
        const additionalUpdatePreconditions = [updatePrecondition];
        const expectedCollection: IUpdatePrecondition[] = [...additionalUpdatePreconditions, ...updatePreconditionCollection];
        spyOn(updatePreconditionService, 'addUpdatePreconditionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ updateKeys });
        comp.ngOnInit();

        expect(updatePreconditionService.query).toHaveBeenCalled();
        expect(updatePreconditionService.addUpdatePreconditionToCollectionIfMissing).toHaveBeenCalledWith(
          updatePreconditionCollection,
          ...additionalUpdatePreconditions
        );
        expect(comp.updatePreconditionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const updateKeys: IUpdateKeys = { id: 456 };
        const updatePrecondition: IUpdatePrecondition = { id: 71193 };
        updateKeys.updatePrecondition = updatePrecondition;

        activatedRoute.data = of({ updateKeys });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(updateKeys));
        expect(comp.updatePreconditionsSharedCollection).toContain(updatePrecondition);
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
