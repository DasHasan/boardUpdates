import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoardUpdateSuccessorDetailComponent } from './board-update-successor-detail.component';

describe('Component Tests', () => {
  describe('BoardUpdateSuccessor Management Detail Component', () => {
    let comp: BoardUpdateSuccessorDetailComponent;
    let fixture: ComponentFixture<BoardUpdateSuccessorDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BoardUpdateSuccessorDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ boardUpdateSuccessor: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BoardUpdateSuccessorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BoardUpdateSuccessorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load boardUpdateSuccessor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.boardUpdateSuccessor).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
