jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FirmwareUpdateService } from '../service/firmware-update.service';
import { IFirmwareUpdate, FirmwareUpdate } from '../firmware-update.model';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';
import { ISoftware } from 'app/entities/software/software.model';
import { SoftwareService } from 'app/entities/software/service/software.service';

import { FirmwareUpdateUpdateComponent } from './firmware-update-update.component';

describe('Component Tests', () => {
  describe('FirmwareUpdate Management Update Component', () => {
    let comp: FirmwareUpdateUpdateComponent;
    let fixture: ComponentFixture<FirmwareUpdateUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let firmwareUpdateService: FirmwareUpdateService;
    let boardService: BoardService;
    let softwareService: SoftwareService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FirmwareUpdateUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FirmwareUpdateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FirmwareUpdateUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      firmwareUpdateService = TestBed.inject(FirmwareUpdateService);
      boardService = TestBed.inject(BoardService);
      softwareService = TestBed.inject(SoftwareService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Board query and add missing value', () => {
        const firmwareUpdate: IFirmwareUpdate = { id: 456 };
        const board: IBoard = { id: 65 };
        firmwareUpdate.board = board;

        const boardCollection: IBoard[] = [{ id: 18437 }];
        spyOn(boardService, 'query').and.returnValue(of(new HttpResponse({ body: boardCollection })));
        const additionalBoards = [board];
        const expectedCollection: IBoard[] = [...additionalBoards, ...boardCollection];
        spyOn(boardService, 'addBoardToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ firmwareUpdate });
        comp.ngOnInit();

        expect(boardService.query).toHaveBeenCalled();
        expect(boardService.addBoardToCollectionIfMissing).toHaveBeenCalledWith(boardCollection, ...additionalBoards);
        expect(comp.boardsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Software query and add missing value', () => {
        const firmwareUpdate: IFirmwareUpdate = { id: 456 };
        const from: ISoftware = { id: 30177 };
        firmwareUpdate.from = from;
        const to: ISoftware = { id: 57401 };
        firmwareUpdate.to = to;

        const softwareCollection: ISoftware[] = [{ id: 33948 }];
        spyOn(softwareService, 'query').and.returnValue(of(new HttpResponse({ body: softwareCollection })));
        const additionalSoftware = [from, to];
        const expectedCollection: ISoftware[] = [...additionalSoftware, ...softwareCollection];
        spyOn(softwareService, 'addSoftwareToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ firmwareUpdate });
        comp.ngOnInit();

        expect(softwareService.query).toHaveBeenCalled();
        expect(softwareService.addSoftwareToCollectionIfMissing).toHaveBeenCalledWith(softwareCollection, ...additionalSoftware);
        expect(comp.softwareSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const firmwareUpdate: IFirmwareUpdate = { id: 456 };
        const board: IBoard = { id: 64713 };
        firmwareUpdate.board = board;
        const from: ISoftware = { id: 41368 };
        firmwareUpdate.from = from;
        const to: ISoftware = { id: 49389 };
        firmwareUpdate.to = to;

        activatedRoute.data = of({ firmwareUpdate });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(firmwareUpdate));
        expect(comp.boardsSharedCollection).toContain(board);
        expect(comp.softwareSharedCollection).toContain(from);
        expect(comp.softwareSharedCollection).toContain(to);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const firmwareUpdate = { id: 123 };
        spyOn(firmwareUpdateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ firmwareUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: firmwareUpdate }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(firmwareUpdateService.update).toHaveBeenCalledWith(firmwareUpdate);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const firmwareUpdate = new FirmwareUpdate();
        spyOn(firmwareUpdateService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ firmwareUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: firmwareUpdate }));
        saveSubject.complete();

        // THEN
        expect(firmwareUpdateService.create).toHaveBeenCalledWith(firmwareUpdate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const firmwareUpdate = { id: 123 };
        spyOn(firmwareUpdateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ firmwareUpdate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(firmwareUpdateService.update).toHaveBeenCalledWith(firmwareUpdate);
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

      describe('trackSoftwareById', () => {
        it('Should return tracked Software primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSoftwareById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
