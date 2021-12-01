import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DownloadUrlDetailComponent } from './download-url-detail.component';

describe('Component Tests', () => {
  describe('DownloadUrl Management Detail Component', () => {
    let comp: DownloadUrlDetailComponent;
    let fixture: ComponentFixture<DownloadUrlDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DownloadUrlDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ downloadUrl: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DownloadUrlDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DownloadUrlDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load downloadUrl on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.downloadUrl).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
