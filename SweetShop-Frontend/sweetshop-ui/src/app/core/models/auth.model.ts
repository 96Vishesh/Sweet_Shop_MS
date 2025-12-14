export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  name: string;
  contactNumber: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token?: string;
  message?: string;
}

export interface User {
  id?: number;
  name: string;
  email: string;
  contactNumber: string;
  role: string;
  status: string;
}
