import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FirmwareDetailComponent } from './firmware-detail.component';

describe('Component Tests', () => {
  describe('Firmware Management Detail Component', () => {
    let comp: FirmwareDetailComponent;
    let fixture: ComponentFixture<FirmwareDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FirmwareDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ firmware: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FirmwareDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FirmwareDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load firmware on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.firmware).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
