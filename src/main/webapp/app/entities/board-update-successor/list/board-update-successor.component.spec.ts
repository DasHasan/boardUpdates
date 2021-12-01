import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BoardUpdateSuccessorService } from '../service/board-update-successor.service';

import { BoardUpdateSuccessorComponent } from './board-update-successor.component';

describe('Component Tests', () => {
  describe('BoardUpdateSuccessor Management Component', () => {
    let comp: BoardUpdateSuccessorComponent;
    let fixture: ComponentFixture<BoardUpdateSuccessorComponent>;
    let service: BoardUpdateSuccessorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BoardUpdateSuccessorComponent],
      })
        .overrideTemplate(BoardUpdateSuccessorComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoardUpdateSuccessorComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BoardUpdateSuccessorService);

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
      expect(comp.boardUpdateSuccessors?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
