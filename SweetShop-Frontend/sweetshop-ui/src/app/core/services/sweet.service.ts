import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sweet, SweetRequest, SweetSearchParams, QuantityRequest, ApiResponse } from '../models/sweet.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class SweetService {
  private readonly API_URL = 'http://localhost:8080/api/sweets';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  // GET /api/sweets - View all sweets (Public)
  getAllSweets(): Observable<Sweet[]> {
    return this.http.get<Sweet[]>(this.API_URL);
  }

  // GET /api/sweets/search - Search sweets (Public)
  searchSweets(params: SweetSearchParams): Observable<Sweet[]> {
    let httpParams = new HttpParams();
    
    if (params.name) {
      httpParams = httpParams.set('name', params.name);
    }
    if (params.category) {
      httpParams = httpParams.set('category', params.category);
    }
    if (params.minPrice !== undefined && params.minPrice !== null) {
      httpParams = httpParams.set('minPrice', params.minPrice.toString());
    }
    if (params.maxPrice !== undefined && params.maxPrice !== null) {
      httpParams = httpParams.set('maxPrice', params.maxPrice.toString());
    }

    return this.http.get<Sweet[]>(`${this.API_URL}/search`, { params: httpParams });
  }

  // POST /api/sweets - Add a new sweet (Protected)
  addSweet(sweet: SweetRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(this.API_URL, sweet, {
      headers: this.getAuthHeaders()
    });
  }

  // PUT /api/sweets/:id - Update sweet details (Protected)
  updateSweet(id: number, sweet: SweetRequest): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.API_URL}/${id}`, sweet, {
      headers: this.getAuthHeaders()
    });
  }

  // DELETE /api/sweets/:id - Delete sweet (Admin only)
  deleteSweet(id: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.API_URL}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // POST /api/sweets/:id/purchase - Purchase sweet (Protected)
  purchaseSweet(id: number, quantity: number): Observable<ApiResponse> {
    const body: QuantityRequest = { quantity };
    return this.http.post<ApiResponse>(`${this.API_URL}/${id}/purchase`, body, {
      headers: this.getAuthHeaders()
    });
  }

  // POST /api/sweets/:id/restock - Restock sweet (Admin only)
  restockSweet(id: number, quantity: number): Observable<ApiResponse> {
    const body: QuantityRequest = { quantity };
    return this.http.post<ApiResponse>(`${this.API_URL}/${id}/restock`, body, {
      headers: this.getAuthHeaders()
    });
  }
}
