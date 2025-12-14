export interface Sweet {
  id: number;
  name: string;
  category: string;
  price: number;
  quantity: number;
  description?: string;
}

export interface SweetRequest {
  name: string;
  category: string;
  price: string;
  quantity: string;
  description?: string;
}

export interface SweetSearchParams {
  name?: string;
  category?: string;
  minPrice?: number;
  maxPrice?: number;
}

export interface QuantityRequest {
  quantity: number;
}

export interface ApiResponse {
  message: string;
}
