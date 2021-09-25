jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GroupService } from '../service/group.service';
import { IGroup, Group } from '../group.model';

import { GroupUpdateComponent } from './group-update.component';

describe('Component Tests', () => {
  describe('Group Management Update Component', () => {
    let comp: GroupUpdateComponent;
    let fixture: ComponentFixture<GroupUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let groupService: GroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GroupUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GroupUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      groupService = TestBed.inject(GroupService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const group: IGroup = { id: 456 };

        activatedRoute.data = of({ group });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(group));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const group = { id: 123 };
        spyOn(groupService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ group });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: group }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(groupService.update).toHaveBeenCalledWith(group);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const group = new Group();
        spyOn(groupService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ group });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: group }));
        saveSubject.complete();

        // THEN
        expect(groupService.create).toHaveBeenCalledWith(group);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const group = { id: 123 };
        spyOn(groupService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ group });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(groupService.update).toHaveBeenCalledWith(group);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
