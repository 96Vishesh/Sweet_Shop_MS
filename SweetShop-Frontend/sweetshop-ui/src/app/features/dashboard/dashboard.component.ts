import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SweetService } from '../../core/services/sweet.service';
import { AuthService } from '../../core/services/auth.service';
import { Sweet, SweetRequest, SweetSearchParams } from '../../core/models/sweet.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  sweets: Sweet[] = [];
  filteredSweets: Sweet[] = [];
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  // Search filters
  searchName = '';
  searchCategory = '';
  searchMinPrice: number | null = null;
  searchMaxPrice: number | null = null;

  // Categories for filter dropdown
  categories: string[] = ['Traditional', 'Bengali', 'South Indian', 'Dry Fruits', 'Milk Based', 'Other'];

  // Modal states
  showAddModal = false;
  showEditModal = false;
  showDeleteModal = false;
  showPurchaseModal = false;
  showRestockModal = false;

  // Form data
  sweetForm: SweetRequest = {
    name: '',
    category: '',
    price: '',
    quantity: '',
    description: ''
  };

  // Selected sweet for operations
  selectedSweet: Sweet | null = null;
  actionQuantity = 1;

  // User state
  isLoggedIn = false;
  isAdmin = false;

  constructor(
    private sweetService: SweetService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.isAdmin = this.authService.isAdmin();
    this.loadSweets();
  }

  loadSweets(): void {
    this.isLoading = true;
    this.clearMessages();

    this.sweetService.getAllSweets().subscribe({
      next: (sweets) => {
        this.sweets = sweets;
        this.filteredSweets = sweets;
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to load sweets. Please try again.';
        console.error('Error loading sweets:', error);
      }
    });
  }

  searchSweets(): void {
    const params: SweetSearchParams = {};

    if (this.searchName.trim()) {
      params.name = this.searchName.trim();
    }
    if (this.searchCategory) {
      params.category = this.searchCategory;
    }
    if (this.searchMinPrice !== null && this.searchMinPrice >= 0) {
      params.minPrice = this.searchMinPrice;
    }
    if (this.searchMaxPrice !== null && this.searchMaxPrice >= 0) {
      params.maxPrice = this.searchMaxPrice;
    }

    // If no filters, load all sweets
    if (Object.keys(params).length === 0) {
      this.loadSweets();
      return;
    }

    this.isLoading = true;
    this.clearMessages();

    this.sweetService.searchSweets(params).subscribe({
      next: (sweets) => {
        this.filteredSweets = sweets;
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Search failed. Please try again.';
        console.error('Error searching sweets:', error);
      }
    });
  }

  clearFilters(): void {
    this.searchName = '';
    this.searchCategory = '';
    this.searchMinPrice = null;
    this.searchMaxPrice = null;
    this.loadSweets();
  }

  // Add Sweet
  openAddModal(): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/auth']);
      return;
    }
    this.resetForm();
    this.showAddModal = true;
  }

  closeAddModal(): void {
    this.showAddModal = false;
    this.resetForm();
  }

  addSweet(): void {
    if (!this.validateForm()) return;

    this.isLoading = true;
    this.sweetService.addSweet(this.sweetForm).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = response.message || 'Sweet added successfully!';
        this.closeAddModal();
        this.loadSweets();
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Failed to add sweet.';
      }
    });
  }

  // Edit Sweet
  openEditModal(sweet: Sweet): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/auth']);
      return;
    }
    this.selectedSweet = sweet;
    this.sweetForm = {
      name: sweet.name,
      category: sweet.category,
      price: sweet.price.toString(),
      quantity: sweet.quantity.toString(),
      description: sweet.description || ''
    };
    this.showEditModal = true;
  }

  closeEditModal(): void {
    this.showEditModal = false;
    this.selectedSweet = null;
    this.resetForm();
  }

  updateSweet(): void {
    if (!this.selectedSweet || !this.validateForm()) return;

    this.isLoading = true;
    this.sweetService.updateSweet(this.selectedSweet.id, this.sweetForm).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = response.message || 'Sweet updated successfully!';
        this.closeEditModal();
        this.loadSweets();
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Failed to update sweet.';
      }
    });
  }

  // Delete Sweet
  openDeleteModal(sweet: Sweet): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/auth']);
      return;
    }
    this.selectedSweet = sweet;
    this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    this.showDeleteModal = false;
    this.selectedSweet = null;
  }

  deleteSweet(): void {
    if (!this.selectedSweet) return;

    this.isLoading = true;
    this.sweetService.deleteSweet(this.selectedSweet.id).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = response.message || 'Sweet deleted successfully!';
        this.closeDeleteModal();
        this.loadSweets();
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Failed to delete sweet. Admin access required.';
      }
    });
  }

  // Purchase Sweet
  openPurchaseModal(sweet: Sweet): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/auth']);
      return;
    }
    this.selectedSweet = sweet;
    this.actionQuantity = 1;
    this.showPurchaseModal = true;
  }

  closePurchaseModal(): void {
    this.showPurchaseModal = false;
    this.selectedSweet = null;
    this.actionQuantity = 1;
  }

  purchaseSweet(): void {
    if (!this.selectedSweet || this.actionQuantity < 1) return;

    if (this.actionQuantity > this.selectedSweet.quantity) {
      this.errorMessage = 'Insufficient stock available.';
      return;
    }

    this.isLoading = true;
    this.sweetService.purchaseSweet(this.selectedSweet.id, this.actionQuantity).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = response.message || 'Purchase successful!';
        this.closePurchaseModal();
        this.loadSweets();
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Purchase failed.';
      }
    });
  }

  // Restock Sweet
  openRestockModal(sweet: Sweet): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/auth']);
      return;
    }
    this.selectedSweet = sweet;
    this.actionQuantity = 10;
    this.showRestockModal = true;
  }

  closeRestockModal(): void {
    this.showRestockModal = false;
    this.selectedSweet = null;
    this.actionQuantity = 10;
  }

  restockSweet(): void {
    if (!this.selectedSweet || this.actionQuantity < 1) return;

    this.isLoading = true;
    this.sweetService.restockSweet(this.selectedSweet.id, this.actionQuantity).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = response.message || 'Restock successful!';
        this.closeRestockModal();
        this.loadSweets();
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Restock failed. Admin access required.';
      }
    });
  }

  // Utility methods
  private validateForm(): boolean {
    if (!this.sweetForm.name.trim()) {
      this.errorMessage = 'Name is required';
      return false;
    }
    if (!this.sweetForm.category) {
      this.errorMessage = 'Category is required';
      return false;
    }
    if (!this.sweetForm.price || parseFloat(this.sweetForm.price) <= 0) {
      this.errorMessage = 'Valid price is required';
      return false;
    }
    if (!this.sweetForm.quantity || parseInt(this.sweetForm.quantity) < 0) {
      this.errorMessage = 'Valid quantity is required';
      return false;
    }
    return true;
  }

  private resetForm(): void {
    this.sweetForm = {
      name: '',
      category: '',
      price: '',
      quantity: '',
      description: ''
    };
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  logout(): void {
    this.authService.logout();
    this.isLoggedIn = false;
    this.isAdmin = false;
    this.router.navigate(['/auth']);
  }

  goToLogin(): void {
    this.router.navigate(['/auth']);
  }
}
