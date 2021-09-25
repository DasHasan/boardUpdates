import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoftwareDetailComponent } from './software-detail.component';

describe('Component Tests', () => {
  describe('Software Management Detail Component', () => {
    let comp: SoftwareDetailComponent;
    let fixture: ComponentFixture<SoftwareDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SoftwareDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ software: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SoftwareDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SoftwareDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load software on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.software).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
