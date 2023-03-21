import { TestBed } from '@angular/core/testing';

import { LoginPageGuard } from './login-page.guard';
import { HttpClientModule } from '@angular/common/http';

describe('LoginPageGuard', () => {
  let guard: LoginPageGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientModule]
    });
    guard = TestBed.inject(LoginPageGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
