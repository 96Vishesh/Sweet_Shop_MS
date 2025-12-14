import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DashboardComponent } from './dashboard.component';
import { SweetService } from '../../core/services/sweet.service';
import { AuthService } from '../../core/services/auth.service';
import { of, throwError } from 'rxjs';
import { Sweet } from '../../core/models/sweet.model';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let sweetService: jasmine.SpyObj<SweetService>;
  let authService: jasmine.SpyObj<AuthService>;

  const mockSweets: Sweet[] = [
    { id: 1, name: 'Gulab Jamun', category: 'Traditional', price: 50, quantity: 100, description: 'Delicious sweet' },
    { id: 2, name: 'Rasgulla', category: 'Bengali', price: 40, quantity: 80, description: 'Soft and spongy' },
    { id: 3, name: 'Jalebi', category: 'Traditional', price: 30, quantity: 0, description: 'Crispy and sweet' }
  ];

  beforeEach(async () => {
    const sweetServiceSpy = jasmine.createSpyObj('SweetService', [
      'getAllSweets', 'searchSweets', 'addSweet', 'updateSweet', 
      'deleteSweet', 'purchaseSweet', 'restockSweet'
    ]);
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['isLoggedIn', 'isAdmin', 'logout']);

    // Default mock returns
    sweetServiceSpy.getAllSweets.and.returnValue(of(mockSweets));
    authServiceSpy.isLoggedIn.and.returnValue(true);
    authServiceSpy.isAdmin.and.returnValue(false);

    await TestBed.configureTestingModule({
      imports: [
        DashboardComponent,
        HttpClientTestingModule,
        FormsModule
      ],
      providers: [
        { provide: SweetService, useValue: sweetServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        provideRouter([
          { path: 'auth', component: DashboardComponent }
        ])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    sweetService = TestBed.inject(SweetService) as jasmine.SpyObj<SweetService>;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should load sweets on init', fakeAsync(() => {
    fixture.detectChanges();
    tick();
    
    expect(sweetService.getAllSweets).toHaveBeenCalled();
    expect(component.sweets.length).toBe(3);
    expect(component.filteredSweets.length).toBe(3);
  }));

  it('should set isLoggedIn and isAdmin on init', () => {
    fixture.detectChanges();
    
    expect(authService.isLoggedIn).toHaveBeenCalled();
    expect(authService.isAdmin).toHaveBeenCalled();
    expect(component.isLoggedIn).toBeTrue();
    expect(component.isAdmin).toBeFalse();
  });

  it('should show admin controls when user is admin', () => {
    authService.isAdmin.and.returnValue(true);
    fixture.detectChanges();
    
    expect(component.isAdmin).toBeTrue();
  });

  describe('Search', () => {
    beforeEach(() => {
      fixture.detectChanges();
    });

    it('should search sweets by name', fakeAsync(() => {
      sweetService.searchSweets.and.returnValue(of([mockSweets[0]]));
      
      component.searchName = 'Gulab';
      component.searchSweets();
      tick();
      
      expect(sweetService.searchSweets).toHaveBeenCalledWith({ name: 'Gulab' });
      expect(component.filteredSweets.length).toBe(1);
    }));

    it('should search sweets by category', fakeAsync(() => {
      sweetService.searchSweets.and.returnValue(of([mockSweets[0], mockSweets[2]]));
      
      component.searchCategory = 'Traditional';
      component.searchSweets();
      tick();
      
      expect(sweetService.searchSweets).toHaveBeenCalledWith({ category: 'Traditional' });
    }));

    it('should search sweets by price range', fakeAsync(() => {
      sweetService.searchSweets.and.returnValue(of(mockSweets));
      
      component.searchMinPrice = 20;
      component.searchMaxPrice = 100;
      component.searchSweets();
      tick();
      
      expect(sweetService.searchSweets).toHaveBeenCalledWith({ minPrice: 20, maxPrice: 100 });
    }));

    it('should clear filters and reload all sweets', fakeAsync(() => {
      component.searchName = 'Test';
      component.searchCategory = 'Traditional';
      component.searchMinPrice = 10;
      component.searchMaxPrice = 50;
      
      component.clearFilters();
      tick();
      
      expect(component.searchName).toBe('');
      expect(component.searchCategory).toBe('');
      expect(component.searchMinPrice).toBeNull();
      expect(component.searchMaxPrice).toBeNull();
      expect(sweetService.getAllSweets).toHaveBeenCalled();
    }));
  });

  describe('Add Sweet', () => {
    beforeEach(() => {
      fixture.detectChanges();
    });

    it('should open add modal', () => {
      component.openAddModal();
      expect(component.showAddModal).toBeTrue();
    });

    it('should close add modal', () => {
      component.showAddModal = true;
      component.closeAddModal();
      expect(component.showAddModal).toBeFalse();
    });

    it('should add sweet successfully', fakeAsync(() => {
      sweetService.addSweet.and.returnValue(of({ message: 'Sweet added successfully' }));
      
      component.sweetForm = {
        name: 'New Sweet',
        category: 'Traditional',
        price: '50',
        quantity: '100',
        description: 'Test description'
      };
      
      component.addSweet();
      tick();
      
      expect(sweetService.addSweet).toHaveBeenCalled();
      expect(component.successMessage).toBe('Sweet added successfully');
      expect(component.showAddModal).toBeFalse();
    }));

    it('should show error when form is invalid', () => {
      component.sweetForm = {
        name: '',
        category: '',
        price: '',
        quantity: '',
        description: ''
      };
      
      component.addSweet();
      
      expect(component.errorMessage).toBe('Name is required');
    });
  });

  describe('Edit Sweet', () => {
    beforeEach(() => {
      fixture.detectChanges();
    });

    it('should open edit modal with sweet data', () => {
      component.openEditModal(mockSweets[0]);
      
      expect(component.showEditModal).toBeTrue();
      expect(component.selectedSweet).toEqual(mockSweets[0]);
      expect(component.sweetForm.name).toBe('Gulab Jamun');
    });

    it('should update sweet successfully', fakeAsync(() => {
      sweetService.updateSweet.and.returnValue(of({ message: 'Sweet updated successfully' }));
      
      component.selectedSweet = mockSweets[0];
      component.sweetForm = {
        name: 'Updated Sweet',
        category: 'Traditional',
        price: '60',
        quantity: '150',
        description: 'Updated description'
      };
      
      component.updateSweet();
      tick();
      
      expect(sweetService.updateSweet).toHaveBeenCalledWith(1, component.sweetForm);
      expect(component.successMessage).toBe('Sweet updated successfully');
    }));
  });

  describe('Delete Sweet', () => {
    beforeEach(() => {
      fixture.detectChanges();
    });

    it('should open delete modal', () => {
      component.openDeleteModal(mockSweets[0]);
      
      expect(component.showDeleteModal).toBeTrue();
      expect(component.selectedSweet).toEqual(mockSweets[0]);
    });

    it('should delete sweet successfully', fakeAsync(() => {
      sweetService.deleteSweet.and.returnValue(of({ message: 'Sweet deleted successfully' }));
      
      component.selectedSweet = mockSweets[0];
      component.deleteSweet();
      tick();
      
      expect(sweetService.deleteSweet).toHaveBeenCalledWith(1);
      expect(component.successMessage).toBe('Sweet deleted successfully');
    }));

    it('should show error when delete fails (non-admin)', fakeAsync(() => {
      sweetService.deleteSweet.and.returnValue(throwError(() => ({
        error: { message: 'Admin access required' }
      })));
      
      component.selectedSweet = mockSweets[0];
      component.deleteSweet();
      tick();
      
      expect(component.errorMessage).toBe('Admin access required');
    }));
  });

  describe('Purchase Sweet', () => {
    beforeEach(() => {
      fixture.detectChanges();
    });

    it('should open purchase modal', () => {
      component.openPurchaseModal(mockSweets[0]);
      
      expect(component.showPurchaseModal).toBeTrue();
      expect(component.selectedSweet).toEqual(mockSweets[0]);
      expect(component.actionQuantity).toBe(1);
    });

    it('should purchase sweet successfully', fakeAsync(() => {
      sweetService.purchaseSweet.and.returnValue(of({ message: 'Sweet purchased successfully' }));
      
      component.selectedSweet = mockSweets[0];
      component.actionQuantity = 5;
      component.purchaseSweet();
      tick();
      
      expect(sweetService.purchaseSweet).toHaveBeenCalledWith(1, 5);
      expect(component.successMessage).toBe('Sweet purchased successfully');
    }));

    it('should show error when quantity exceeds stock', () => {
      component.selectedSweet = mockSweets[0]; // quantity: 100
      component.actionQuantity = 150;
      
      component.purchaseSweet();
      
      expect(component.errorMessage).toBe('Insufficient stock available.');
    });
  });

  describe('Restock Sweet', () => {
    beforeEach(() => {
      fixture.detectChanges();
    });

    it('should open restock modal', () => {
      component.openRestockModal(mockSweets[0]);
      
      expect(component.showRestockModal).toBeTrue();
      expect(component.selectedSweet).toEqual(mockSweets[0]);
      expect(component.actionQuantity).toBe(10);
    });

    it('should restock sweet successfully', fakeAsync(() => {
      sweetService.restockSweet.and.returnValue(of({ message: 'Sweet restocked successfully' }));
      
      component.selectedSweet = mockSweets[0];
      component.actionQuantity = 50;
      component.restockSweet();
      tick();
      
      expect(sweetService.restockSweet).toHaveBeenCalledWith(1, 50);
      expect(component.successMessage).toBe('Sweet restocked successfully');
    }));

    it('should show error when restock fails (non-admin)', fakeAsync(() => {
      sweetService.restockSweet.and.returnValue(throwError(() => ({
        error: { message: 'Admin access required' }
      })));
      
      component.selectedSweet = mockSweets[0];
      component.actionQuantity = 50;
      component.restockSweet();
      tick();
      
      expect(component.errorMessage).toBe('Admin access required');
    }));
  });

  describe('Logout', () => {
    beforeEach(() => {
      fixture.detectChanges();
    });

    it('should logout and reset state', () => {
      component.isLoggedIn = true;
      component.isAdmin = true;
      
      component.logout();
      
      expect(authService.logout).toHaveBeenCalled();
      expect(component.isLoggedIn).toBeFalse();
      expect(component.isAdmin).toBeFalse();
    });
  });

  describe('UI State', () => {
    it('should show loading state while fetching sweets', () => {
      expect(component.isLoading).toBeFalse();
      
      component.loadSweets();
      expect(component.isLoading).toBeTrue();
    });

    it('should handle error when loading sweets fails', fakeAsync(() => {
      sweetService.getAllSweets.and.returnValue(throwError(() => new Error('Network error')));
      
      fixture.detectChanges();
      tick();
      
      expect(component.errorMessage).toBe('Failed to load sweets. Please try again.');
      expect(component.isLoading).toBeFalse();
    }));
  });
});
