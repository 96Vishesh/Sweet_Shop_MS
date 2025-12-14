import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { LoginRequest, SignupRequest } from '../../core/models/auth.model';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent {
  isLoginMode = true;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  // Login form fields
  loginEmail = '';
  loginPassword = '';

  // Signup form fields
  signupName = '';
  signupEmail = '';
  signupContactNumber = '';
  signupPassword = '';
  signupConfirmPassword = '';

  // Password visibility
  showPassword = false;
  showConfirmPassword = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  toggleMode(): void {
    this.isLoginMode = !this.isLoginMode;
    this.clearMessages();
    this.clearForms();
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  onLogin(): void {
    if (!this.loginEmail || !this.loginPassword) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    this.isLoading = true;
    this.clearMessages();

    const loginRequest: LoginRequest = {
      email: this.loginEmail,
      password: this.loginPassword
    };

    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.token) {
          this.successMessage = 'Login successful!';
          setTimeout(() => {
            this.router.navigate(['/dashboard']);
          }, 1000);
        } else if (response.message) {
          this.errorMessage = response.message;
        }
      },
      error: (error) => {
        this.isLoading = false;
        if (error.error?.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Login failed. Please check your credentials.';
        }
      }
    });
  }

  onSignup(): void {
    if (!this.signupName || !this.signupEmail || !this.signupContactNumber || 
        !this.signupPassword || !this.signupConfirmPassword) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    if (this.signupPassword !== this.signupConfirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    if (this.signupPassword.length < 6) {
      this.errorMessage = 'Password must be at least 6 characters';
      return;
    }

    this.isLoading = true;
    this.clearMessages();

    const signupRequest: SignupRequest = {
      name: this.signupName,
      email: this.signupEmail,
      contactNumber: this.signupContactNumber,
      password: this.signupPassword
    };

    this.authService.signup(signupRequest).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = 'Account created successfully! Please login.';
        setTimeout(() => {
          this.isLoginMode = true;
          this.loginEmail = this.signupEmail;
          this.clearSignupForm();
        }, 1500);
      },
      error: (error) => {
        this.isLoading = false;
        if (error.error?.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Registration failed. Please try again.';
        }
      }
    });
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  private clearForms(): void {
    this.loginEmail = '';
    this.loginPassword = '';
    this.clearSignupForm();
  }

  private clearSignupForm(): void {
    this.signupName = '';
    this.signupEmail = '';
    this.signupContactNumber = '';
    this.signupPassword = '';
    this.signupConfirmPassword = '';
  }
}
