import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BoardService } from '../service/board.service';

import { BoardComponent } from './board.component';

describe('Component Tests', () => {
  describe('Board Management Component', () => {
    let comp: BoardComponent;
    let fixture: ComponentFixture<BoardComponent>;
    let service: BoardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BoardComponent],
      })
        .overrideTemplate(BoardComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoardComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BoardService);

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
      expect(comp.boards?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
