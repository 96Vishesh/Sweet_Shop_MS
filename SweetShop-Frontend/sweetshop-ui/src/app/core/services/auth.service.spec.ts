import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { LoginRequest, SignupRequest } from '../models/auth.model';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('signup', () => {
    it('should send signup request with correct payload', () => {
      const signupRequest: SignupRequest = {
        name: 'Test User',
        contactNumber: '1234567890',
        email: 'test@example.com',
        password: 'password123'
      };

      service.signup(signupRequest).subscribe(response => {
        expect(response.message).toBe('Successfully Registered');
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/signup');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(signupRequest);
      req.flush({ message: 'Successfully Registered' });
    });

    it('should handle signup error for existing email', () => {
      const signupRequest: SignupRequest = {
        name: 'Test User',
        contactNumber: '1234567890',
        email: 'existing@example.com',
        password: 'password123'
      };

      service.signup(signupRequest).subscribe({
        error: (error) => {
          expect(error.status).toBe(400);
        }
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/signup');
      req.flush({ message: 'Email already exists' }, { status: 400, statusText: 'Bad Request' });
    });
  });

  describe('login', () => {
    it('should send login request and store token on success', () => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'password123'
      };
      const mockToken = 'mock-jwt-token';

      service.login(loginRequest).subscribe(response => {
        expect(response.token).toBe(mockToken);
        expect(service.getToken()).toBe(mockToken);
        expect(service.isLoggedIn()).toBeTrue();
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(loginRequest);
      req.flush({ token: mockToken });
    });

    it('should handle login error for wrong credentials', () => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'wrongpassword'
      };

      service.login(loginRequest).subscribe({
        error: (error) => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      req.flush({ message: 'Wrong Credentials' }, { status: 401, statusText: 'Unauthorized' });
    });
  });

  describe('logout', () => {
    it('should remove token and update authentication state', () => {
      localStorage.setItem('auth_token', 'test-token');
      
      service.logout();
      
      expect(service.getToken()).toBeNull();
      expect(service.isLoggedIn()).toBeFalse();
    });
  });

  describe('isLoggedIn', () => {
    it('should return true when token exists', () => {
      localStorage.setItem('auth_token', 'test-token');
      expect(service.isLoggedIn()).toBeTrue();
    });

    it('should return false when no token exists', () => {
      expect(service.isLoggedIn()).toBeFalse();
    });
  });
});
