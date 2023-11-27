import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyFavouritesPageComponent } from './my-favourites-page.component';

describe('MyFavouritesPageComponent', () => {
  let component: MyFavouritesPageComponent;
  let fixture: ComponentFixture<MyFavouritesPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MyFavouritesPageComponent]
    });
    fixture = TestBed.createComponent(MyFavouritesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
