import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SoftwareService } from '../service/software.service';

import { SoftwareComponent } from './software.component';

describe('Component Tests', () => {
  describe('Software Management Component', () => {
    let comp: SoftwareComponent;
    let fixture: ComponentFixture<SoftwareComponent>;
    let service: SoftwareService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SoftwareComponent],
      })
        .overrideTemplate(SoftwareComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SoftwareComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SoftwareService);

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
      expect(comp.software?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
