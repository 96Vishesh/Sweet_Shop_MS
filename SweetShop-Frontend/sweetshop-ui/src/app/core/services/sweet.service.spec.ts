import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SweetService } from './sweet.service';
import { AuthService } from './auth.service';
import { Sweet, SweetRequest } from '../models/sweet.model';

describe('SweetService', () => {
  let service: SweetService;
  let httpMock: HttpTestingController;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  const mockSweets: Sweet[] = [
    { id: 1, name: 'Gulab Jamun', category: 'Traditional', price: 50, quantity: 100, description: 'Delicious' },
    { id: 2, name: 'Rasgulla', category: 'Bengali', price: 40, quantity: 80, description: 'Soft and spongy' }
  ];

  beforeEach(() => {
    const spy = jasmine.createSpyObj('AuthService', ['getToken']);
    spy.getToken.and.returnValue('mock-jwt-token');

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        SweetService,
        { provide: AuthService, useValue: spy }
      ]
    });

    service = TestBed.inject(SweetService);
    httpMock = TestBed.inject(HttpTestingController);
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getAllSweets', () => {
    it('should return all sweets', () => {
      service.getAllSweets().subscribe(sweets => {
        expect(sweets.length).toBe(2);
        expect(sweets).toEqual(mockSweets);
      });

      const req = httpMock.expectOne('http://localhost:8080/api/sweets');
      expect(req.request.method).toBe('GET');
      req.flush(mockSweets);
    });
  });

  describe('searchSweets', () => {
    it('should search sweets by name', () => {
      service.searchSweets({ name: 'Gulab' }).subscribe(sweets => {
        expect(sweets.length).toBe(1);
      });

      const req = httpMock.expectOne('http://localhost:8080/api/sweets/search?name=Gulab');
      expect(req.request.method).toBe('GET');
      req.flush([mockSweets[0]]);
    });

    it('should search sweets by category', () => {
      service.searchSweets({ category: 'Traditional' }).subscribe();

      const req = httpMock.expectOne('http://localhost:8080/api/sweets/search?category=Traditional');
      expect(req.request.method).toBe('GET');
      req.flush([mockSweets[0]]);
    });

    it('should search sweets by price range', () => {
      service.searchSweets({ minPrice: 20, maxPrice: 100 }).subscribe();

      const req = httpMock.expectOne('http://localhost:8080/api/sweets/search?minPrice=20&maxPrice=100');
      expect(req.request.method).toBe('GET');
      req.flush(mockSweets);
    });

    it('should search sweets with combined filters', () => {
      service.searchSweets({ name: 'Gulab', category: 'Traditional', minPrice: 20, maxPrice: 100 }).subscribe();

      const req = httpMock.expectOne(
        'http://localhost:8080/api/sweets/search?name=Gulab&category=Traditional&minPrice=20&maxPrice=100'
      );
      expect(req.request.method).toBe('GET');
      req.flush([mockSweets[0]]);
    });
  });

  describe('addSweet', () => {
    it('should add a new sweet with auth header', () => {
      const newSweet: SweetRequest = {
        name: 'Jalebi',
        category: 'Traditional',
        price: '30.00',
        quantity: '50',
        description: 'Crispy and sweet'
      };

      service.addSweet(newSweet).subscribe(response => {
        expect(response.message).toBe('Sweet added successfully');
      });

      const req = httpMock.expectOne('http://localhost:8080/api/sweets');
      expect(req.request.method).toBe('POST');
      expect(req.request.headers.get('Authorization')).toBe('Bearer mock-jwt-token');
      expect(req.request.body).toEqual(newSweet);
      req.flush({ message: 'Sweet added successfully' });
    });
  });

  describe('updateSweet', () => {
    it('should update sweet with auth header', () => {
      const updateData: SweetRequest = {
        name: 'Gulab Jamun Special',
        category: 'Traditional',
        price: '60.00',
        quantity: '150',
        description: 'Premium quality'
      };

      service.updateSweet(1, updateData).subscribe(response => {
        expect(response.message).toBe('Sweet updated successfully');
      });

      const req = httpMock.expectOne('http://localhost:8080/api/sweets/1');
      expect(req.request.method).toBe('PUT');
      expect(req.request.headers.get('Authorization')).toBe('Bearer mock-jwt-token');
      req.flush({ message: 'Sweet updated successfully' });
    });
  });

  describe('deleteSweet', () => {
    it('should delete sweet with auth header', () => {
      service.deleteSweet(1).subscribe(response => {
        expect(response.message).toBe('Sweet deleted successfully');
      });

      const req = httpMock.expectOne('http://localhost:8080/api/sweets/1');
      expect(req.request.method).toBe('DELETE');
      expect(req.request.headers.get('Authorization')).toBe('Bearer mock-jwt-token');
      req.flush({ message: 'Sweet deleted successfully' });
    });
  });

  describe('purchaseSweet', () => {
    it('should purchase sweet with quantity', () => {
      service.purchaseSweet(1, 5).subscribe(response => {
        expect(response.message).toContain('purchased successfully');
      });

      const req = httpMock.expectOne('http://localhost:8080/api/sweets/1/purchase');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({ quantity: 5 });
      req.flush({ message: 'Sweet purchased successfully. Remaining quantity: 95' });
    });
  });

  describe('restockSweet', () => {
    it('should restock sweet with quantity', () => {
      service.restockSweet(1, 50).subscribe(response => {
        expect(response.message).toContain('restocked successfully');
      });

      const req = httpMock.expectOne('http://localhost:8080/api/sweets/1/restock');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({ quantity: 50 });
      req.flush({ message: 'Sweet restocked successfully. New quantity: 150' });
    });
  });
});
