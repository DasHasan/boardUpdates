jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FirmwareService } from '../service/firmware.service';
import { IFirmware, Firmware } from '../firmware.model';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';

import { FirmwareUpdateComponent } from './firmware-update.component';

describe('Component Tests', () => {
  describe('Firmware Management Update Component', () => {
    let comp: FirmwareUpdateComponent;
    let fixture: ComponentFixture<FirmwareUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let firmwareService: FirmwareService;
    let boardService: BoardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FirmwareUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FirmwareUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FirmwareUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      firmwareService = TestBed.inject(FirmwareService);
      boardService = TestBed.inject(BoardService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Board query and add missing value', () => {
        const firmware: IFirmware = { id: 456 };
        const board: IBoard = { id: 65 };
        firmware.board = board;

        const boardCollection: IBoard[] = [{ id: 18437 }];
        spyOn(boardService, 'query').and.returnValue(of(new HttpResponse({ body: boardCollection })));
        const additionalBoards = [board];
        const expectedCollection: IBoard[] = [...additionalBoards, ...boardCollection];
        spyOn(boardService, 'addBoardToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ firmware });
        comp.ngOnInit();

        expect(boardService.query).toHaveBeenCalled();
        expect(boardService.addBoardToCollectionIfMissing).toHaveBeenCalledWith(boardCollection, ...additionalBoards);
        expect(comp.boardsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const firmware: IFirmware = { id: 456 };
        const board: IBoard = { id: 64713 };
        firmware.board = board;

        activatedRoute.data = of({ firmware });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(firmware));
        expect(comp.boardsSharedCollection).toContain(board);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const firmware = { id: 123 };
        spyOn(firmwareService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ firmware });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: firmware }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(firmwareService.update).toHaveBeenCalledWith(firmware);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const firmware = new Firmware();
        spyOn(firmwareService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ firmware });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: firmware }));
        saveSubject.complete();

        // THEN
        expect(firmwareService.create).toHaveBeenCalledWith(firmware);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const firmware = { id: 123 };
        spyOn(firmwareService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ firmware });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(firmwareService.update).toHaveBeenCalledWith(firmware);
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
