import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DownloadService } from '../service/download.service';

import { DownloadComponent } from './download.component';

describe('Component Tests', () => {
  describe('Download Management Component', () => {
    let comp: DownloadComponent;
    let fixture: ComponentFixture<DownloadComponent>;
    let service: DownloadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DownloadComponent],
      })
        .overrideTemplate(DownloadComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DownloadComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DownloadService);

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
      expect(comp.downloads?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
