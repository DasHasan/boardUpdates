import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoardUpdateDetailComponent } from './board-update-detail.component';

describe('Component Tests', () => {
  describe('BoardUpdate Management Detail Component', () => {
    let comp: BoardUpdateDetailComponent;
    let fixture: ComponentFixture<BoardUpdateDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BoardUpdateDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ boardUpdate: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BoardUpdateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BoardUpdateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load boardUpdate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.boardUpdate).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
