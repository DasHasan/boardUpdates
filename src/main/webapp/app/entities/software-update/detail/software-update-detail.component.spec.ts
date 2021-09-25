import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoftwareUpdateDetailComponent } from './software-update-detail.component';

describe('Component Tests', () => {
  describe('SoftwareUpdate Management Detail Component', () => {
    let comp: SoftwareUpdateDetailComponent;
    let fixture: ComponentFixture<SoftwareUpdateDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SoftwareUpdateDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ softwareUpdate: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SoftwareUpdateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SoftwareUpdateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load softwareUpdate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.softwareUpdate).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
