jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SoftwareService } from '../service/software.service';
import { ISoftware, Software } from '../software.model';

import { SoftwareUpdateComponent } from './software-update.component';

describe('Component Tests', () => {
  describe('Software Management Update Component', () => {
    let comp: SoftwareUpdateComponent;
    let fixture: ComponentFixture<SoftwareUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let softwareService: SoftwareService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SoftwareUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SoftwareUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SoftwareUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      softwareService = TestBed.inject(SoftwareService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const software: ISoftware = { id: 456 };

        activatedRoute.data = of({ software });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(software));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const software = { id: 123 };
        spyOn(softwareService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ software });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: software }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(softwareService.update).toHaveBeenCalledWith(software);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const software = new Software();
        spyOn(softwareService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ software });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: software }));
        saveSubject.complete();

        // THEN
        expect(softwareService.create).toHaveBeenCalledWith(software);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const software = { id: 123 };
        spyOn(softwareService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ software });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(softwareService.update).toHaveBeenCalledWith(software);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
