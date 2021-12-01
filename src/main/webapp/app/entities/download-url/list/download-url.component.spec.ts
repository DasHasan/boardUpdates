import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DownloadUrlService } from '../service/download-url.service';

import { DownloadUrlComponent } from './download-url.component';

describe('Component Tests', () => {
  describe('DownloadUrl Management Component', () => {
    let comp: DownloadUrlComponent;
    let fixture: ComponentFixture<DownloadUrlComponent>;
    let service: DownloadUrlService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DownloadUrlComponent],
      })
        .overrideTemplate(DownloadUrlComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DownloadUrlComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DownloadUrlService);

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
      expect(comp.downloadUrls?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
