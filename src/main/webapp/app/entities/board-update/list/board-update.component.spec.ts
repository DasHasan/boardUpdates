import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BoardUpdateService } from '../service/board-update.service';

import { BoardUpdateComponent } from './board-update.component';

describe('Component Tests', () => {
  describe('BoardUpdate Management Component', () => {
    let comp: BoardUpdateComponent;
    let fixture: ComponentFixture<BoardUpdateComponent>;
    let service: BoardUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BoardUpdateComponent],
      })
        .overrideTemplate(BoardUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoardUpdateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BoardUpdateService);

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
      expect(comp.boardUpdates?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
