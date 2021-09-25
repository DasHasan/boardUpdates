import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FirmwareUpdateService } from '../service/firmware-update.service';

import { FirmwareUpdateComponent } from './firmware-update.component';

describe('Component Tests', () => {
  describe('FirmwareUpdate Management Component', () => {
    let comp: FirmwareUpdateComponent;
    let fixture: ComponentFixture<FirmwareUpdateComponent>;
    let service: FirmwareUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FirmwareUpdateComponent],
      })
        .overrideTemplate(FirmwareUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FirmwareUpdateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FirmwareUpdateService);

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
      expect(comp.firmwareUpdates?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
