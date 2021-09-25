import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FirmwareUpdateDetailComponent } from './firmware-update-detail.component';

describe('Component Tests', () => {
  describe('FirmwareUpdate Management Detail Component', () => {
    let comp: FirmwareUpdateDetailComponent;
    let fixture: ComponentFixture<FirmwareUpdateDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FirmwareUpdateDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ firmwareUpdate: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FirmwareUpdateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FirmwareUpdateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load firmwareUpdate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.firmwareUpdate).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
