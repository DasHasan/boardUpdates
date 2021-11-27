import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DownloadDetailComponent } from './download-detail.component';

describe('Component Tests', () => {
  describe('Download Management Detail Component', () => {
    let comp: DownloadDetailComponent;
    let fixture: ComponentFixture<DownloadDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DownloadDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ download: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DownloadDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DownloadDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load download on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.download).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
