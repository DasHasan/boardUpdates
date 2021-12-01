import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GroupService } from '../service/group.service';

import { GroupComponent } from './group.component';

describe('Component Tests', () => {
  describe('Group Management Component', () => {
    let comp: GroupComponent;
    let fixture: ComponentFixture<GroupComponent>;
    let service: GroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GroupComponent],
      })
        .overrideTemplate(GroupComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GroupComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(GroupService);

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
      expect(comp.groups?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
