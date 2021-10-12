import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UpdateKeysDetailComponent } from './update-keys-detail.component';

describe('Component Tests', () => {
  describe('UpdateKeys Management Detail Component', () => {
    let comp: UpdateKeysDetailComponent;
    let fixture: ComponentFixture<UpdateKeysDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UpdateKeysDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ updateKeys: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UpdateKeysDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UpdateKeysDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load updateKeys on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.updateKeys).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
