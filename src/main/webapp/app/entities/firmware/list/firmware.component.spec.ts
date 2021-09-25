import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FirmwareService } from '../service/firmware.service';

import { FirmwareComponent } from './firmware.component';

describe('Component Tests', () => {
  describe('Firmware Management Component', () => {
    let comp: FirmwareComponent;
    let fixture: ComponentFixture<FirmwareComponent>;
    let service: FirmwareService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FirmwareComponent],
      })
        .overrideTemplate(FirmwareComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FirmwareComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FirmwareService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.firmware?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
