import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { AuthComponent } from './auth.component';
import { AuthService } from '../../core/services/auth.service';
import { of, throwError } from 'rxjs';

describe('AuthComponent', () => {
  let component: AuthComponent;
  let fixture: ComponentFixture<AuthComponent>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['login', 'signup']);

    await TestBed.configureTestingModule({
      imports: [
        AuthComponent,
        HttpClientTestingModule,
        RouterTestingModule,
        FormsModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AuthComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should start in login mode', () => {
    expect(component.isLoginMode).toBeTrue();
  });

  it('should toggle between login and signup modes', () => {
    expect(component.isLoginMode).toBeTrue();
    
    component.toggleMode();
    expect(component.isLoginMode).toBeFalse();
    
    component.toggleMode();
    expect(component.isLoginMode).toBeTrue();
  });

  it('should toggle password visibility', () => {
    expect(component.showPassword).toBeFalse();
    
    component.togglePasswordVisibility();
    expect(component.showPassword).toBeTrue();
    
    component.togglePasswordVisibility();
    expect(component.showPassword).toBeFalse();
  });

  describe('Login', () => {
    it('should show error when login fields are empty', () => {
      component.loginEmail = '';
      component.loginPassword = '';
      
      component.onLogin();
      
      expect(component.errorMessage).toBe('Please fill in all fields');
    });

    it('should call authService.login with correct credentials', fakeAsync(() => {
      authService.login.and.returnValue(of({ token: 'test-token' }));
      
      component.loginEmail = 'test@example.com';
      component.loginPassword = 'password123';
      
      component.onLogin();
      tick();
      
      expect(authService.login).toHaveBeenCalledWith({
        email: 'test@example.com',
        password: 'password123'
      });
    }));

    it('should show success message on successful login', fakeAsync(() => {
      authService.login.and.returnValue(of({ token: 'test-token' }));
      
      component.loginEmail = 'test@example.com';
      component.loginPassword = 'password123';
      
      component.onLogin();
      tick();
      
      expect(component.successMessage).toBe('Login successful!');
    }));

    it('should show error message on login failure', fakeAsync(() => {
      authService.login.and.returnValue(throwError(() => ({
        error: { message: 'Wrong Credentials' }
      })));
      
      component.loginEmail = 'test@example.com';
      component.loginPassword = 'wrongpassword';
      
      component.onLogin();
      tick();
      
      expect(component.errorMessage).toBe('Wrong Credentials');
    }));
  });

  describe('Signup', () => {
    beforeEach(() => {
      component.isLoginMode = false;
    });

    it('should show error when signup fields are empty', () => {
      component.signupName = '';
      component.signupEmail = '';
      component.signupContactNumber = '';
      component.signupPassword = '';
      component.signupConfirmPassword = '';
      
      component.onSignup();
      
      expect(component.errorMessage).toBe('Please fill in all fields');
    });

    it('should show error when passwords do not match', () => {
      component.signupName = 'Test User';
      component.signupEmail = 'test@example.com';
      component.signupContactNumber = '1234567890';
      component.signupPassword = 'password123';
      component.signupConfirmPassword = 'differentpassword';
      
      component.onSignup();
      
      expect(component.errorMessage).toBe('Passwords do not match');
    });

    it('should show error when password is too short', () => {
      component.signupName = 'Test User';
      component.signupEmail = 'test@example.com';
      component.signupContactNumber = '1234567890';
      component.signupPassword = '12345';
      component.signupConfirmPassword = '12345';
      
      component.onSignup();
      
      expect(component.errorMessage).toBe('Password must be at least 6 characters');
    });

    it('should call authService.signup with correct data', fakeAsync(() => {
      authService.signup.and.returnValue(of({ message: 'Successfully Registered' }));
      
      component.signupName = 'Test User';
      component.signupEmail = 'test@example.com';
      component.signupContactNumber = '1234567890';
      component.signupPassword = 'password123';
      component.signupConfirmPassword = 'password123';
      
      component.onSignup();
      tick();
      
      expect(authService.signup).toHaveBeenCalledWith({
        name: 'Test User',
        email: 'test@example.com',
        contactNumber: '1234567890',
        password: 'password123'
      });
    }));

    it('should show success message on successful signup', fakeAsync(() => {
      authService.signup.and.returnValue(of({ message: 'Successfully Registered' }));
      
      component.signupName = 'Test User';
      component.signupEmail = 'test@example.com';
      component.signupContactNumber = '1234567890';
      component.signupPassword = 'password123';
      component.signupConfirmPassword = 'password123';
      
      component.onSignup();
      tick();
      
      expect(component.successMessage).toBe('Account created successfully! Please login.');
    }));

    it('should show error message on signup failure', fakeAsync(() => {
      authService.signup.and.returnValue(throwError(() => ({
        error: { message: 'Email already exists' }
      })));
      
      component.signupName = 'Test User';
      component.signupEmail = 'existing@example.com';
      component.signupContactNumber = '1234567890';
      component.signupPassword = 'password123';
      component.signupConfirmPassword = 'password123';
      
      component.onSignup();
      tick();
      
      expect(component.errorMessage).toBe('Email already exists');
    }));
  });
});
