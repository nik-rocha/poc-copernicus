import { Injectable } from '@angular/core';
import axios from 'axios';
import { RegisterUserRequest } from "../models/register.model"
import { AuthResponse, LoginRequest } from '../models/login.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private URL = 'http://localhost:8080'

  async register(data: RegisterUserRequest): Promise<any> {
    const response = await axios.post(`${this.URL}/auth/register`, data);
    return response.data;
  }

  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response = await axios.post<AuthResponse>(`${this.URL}/auth/login`, credentials);

    if (response.data?.token) {
      localStorage.setItem('token', response.data.token);
    }

    return response.data;
  }
}
