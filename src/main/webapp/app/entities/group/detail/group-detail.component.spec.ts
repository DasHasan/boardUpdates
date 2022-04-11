import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GroupDetailComponent } from './group-detail.component';

describe('Component Tests', () => {
  describe('Group Management Detail Component', () => {
    let comp: GroupDetailComponent;
    let fixture: ComponentFixture<GroupDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GroupDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ group: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load group on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.group).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
