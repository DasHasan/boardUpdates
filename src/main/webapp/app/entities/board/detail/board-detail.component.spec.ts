import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoardDetailComponent } from './board-detail.component';

describe('Component Tests', () => {
  describe('Board Management Detail Component', () => {
    let comp: BoardDetailComponent;
    let fixture: ComponentFixture<BoardDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BoardDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ board: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BoardDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BoardDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load board on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.board).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
