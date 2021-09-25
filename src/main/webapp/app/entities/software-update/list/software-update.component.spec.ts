import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SoftwareUpdateService } from '../service/software-update.service';

import { SoftwareUpdateComponent } from './software-update.component';

describe('Component Tests', () => {
  describe('SoftwareUpdate Management Component', () => {
    let comp: SoftwareUpdateComponent;
    let fixture: ComponentFixture<SoftwareUpdateComponent>;
    let service: SoftwareUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SoftwareUpdateComponent],
      })
        .overrideTemplate(SoftwareUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SoftwareUpdateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SoftwareUpdateService);

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
      expect(comp.softwareUpdates?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
