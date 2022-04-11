import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UpdatePreconditionDetailComponent } from './update-precondition-detail.component';

describe('Component Tests', () => {
  describe('UpdatePrecondition Management Detail Component', () => {
    let comp: UpdatePreconditionDetailComponent;
    let fixture: ComponentFixture<UpdatePreconditionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UpdatePreconditionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ updatePrecondition: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UpdatePreconditionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UpdatePreconditionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load updatePrecondition on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.updatePrecondition).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
