jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SoftwareService } from '../service/software.service';
import { ISoftware, Software } from '../software.model';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';

import { SoftwareUpdateComponent } from './software-update.component';

describe('Component Tests', () => {
  describe('Software Management Update Component', () => {
    let comp: SoftwareUpdateComponent;
    let fixture: ComponentFixture<SoftwareUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let softwareService: SoftwareService;
    let boardService: BoardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SoftwareUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SoftwareUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SoftwareUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      softwareService = TestBed.inject(SoftwareService);
      boardService = TestBed.inject(BoardService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Board query and add missing value', () => {
        const software: ISoftware = { id: 456 };
        const board: IBoard = { id: 13481 };
        software.board = board;

        const boardCollection: IBoard[] = [{ id: 62973 }];
        spyOn(boardService, 'query').and.returnValue(of(new HttpResponse({ body: boardCollection })));
        const additionalBoards = [board];
        const expectedCollection: IBoard[] = [...additionalBoards, ...boardCollection];
        spyOn(boardService, 'addBoardToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ software });
        comp.ngOnInit();

        expect(boardService.query).toHaveBeenCalled();
        expect(boardService.addBoardToCollectionIfMissing).toHaveBeenCalledWith(boardCollection, ...additionalBoards);
        expect(comp.boardsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const software: ISoftware = { id: 456 };
        const board: IBoard = { id: 23363 };
        software.board = board;

        activatedRoute.data = of({ software });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(software));
        expect(comp.boardsSharedCollection).toContain(board);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const software = { id: 123 };
        spyOn(softwareService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ software });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: software }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(softwareService.update).toHaveBeenCalledWith(software);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const software = new Software();
        spyOn(softwareService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ software });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: software }));
        saveSubject.complete();

        // THEN
        expect(softwareService.create).toHaveBeenCalledWith(software);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const software = { id: 123 };
        spyOn(softwareService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ software });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(softwareService.update).toHaveBeenCalledWith(software);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBoardById', () => {
        it('Should return tracked Board primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBoardById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
