jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FirmwareService } from '../service/firmware.service';
import { IFirmware, Firmware } from '../firmware.model';

import { FirmwareUpdateComponent } from './firmware-update.component';

describe('Component Tests', () => {
  describe('Firmware Management Update Component', () => {
    let comp: FirmwareUpdateComponent;
    let fixture: ComponentFixture<FirmwareUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let firmwareService: FirmwareService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FirmwareUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FirmwareUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FirmwareUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      firmwareService = TestBed.inject(FirmwareService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const firmware: IFirmware = { id: 456 };

        activatedRoute.data = of({ firmware });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(firmware));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const firmware = { id: 123 };
        spyOn(firmwareService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ firmware });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: firmware }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(firmwareService.update).toHaveBeenCalledWith(firmware);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const firmware = new Firmware();
        spyOn(firmwareService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ firmware });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: firmware }));
        saveSubject.complete();

        // THEN
        expect(firmwareService.create).toHaveBeenCalledWith(firmware);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const firmware = { id: 123 };
        spyOn(firmwareService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ firmware });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(firmwareService.update).toHaveBeenCalledWith(firmware);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
