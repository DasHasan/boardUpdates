jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BoardUpdateService } from '../service/board-update.service';
import { IBoardUpdate, BoardUpdate } from '../board-update.model';

import { BoardUpdateUpdateComponent } from './board-update-update.component';

describe('Component Tests', () => {
  describe('BoardUpdate Management Update Component', () => {
    let comp: BoardUpdateUpdateComponent;
    let fixture: ComponentFixture<BoardUpdateUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let boardUpdateService: BoardUpdateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BoardUpdateUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BoardUpdateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoardUpdateUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      boardUpdateService = TestBed.inject(BoardUpdateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const boardUpdate: IBoardUpdate = { id: 456 };

        activatedRoute.data = of({ boardUpdate });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(boardUpdate));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUpdate = { id: 123 };
        spyOn(boardUpdateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: boardUpdate }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(boardUpdateService.update).toHaveBeenCalledWith(boardUpdate);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUpdate = new BoardUpdate();
        spyOn(boardUpdateService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: boardUpdate }));
        saveSubject.complete();

        // THEN
        expect(boardUpdateService.create).toHaveBeenCalledWith(boardUpdate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const boardUpdate = { id: 123 };
        spyOn(boardUpdateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ boardUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(boardUpdateService.update).toHaveBeenCalledWith(boardUpdate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
