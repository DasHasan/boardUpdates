import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UpdatePreconditionService } from '../service/update-precondition.service';

import { UpdatePreconditionComponent } from './update-precondition.component';

describe('Component Tests', () => {
  describe('UpdatePrecondition Management Component', () => {
    let comp: UpdatePreconditionComponent;
    let fixture: ComponentFixture<UpdatePreconditionComponent>;
    let service: UpdatePreconditionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UpdatePreconditionComponent],
      })
        .overrideTemplate(UpdatePreconditionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UpdatePreconditionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(UpdatePreconditionService);

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
      expect(comp.updatePreconditions?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
