import { Injectable } from '@angular/core';
import axios from 'axios';
import { Device } from '../models/organization.model';

@Injectable({ providedIn: 'root' })
export class DeviceService {
  private URL = 'http://localhost:8080/devices';

  async getAll(): Promise<Device[]> {
    const token = localStorage.getItem('token');
    const authHeader = token?.startsWith('Bearer ') ? token : `Bearer ${token}`;

    const response = await axios.get<Device[]>(this.URL, {
      headers: { Authorization: authHeader }
    });

    return response.data;
  }

  private getAuthHeader() {
    const token = localStorage.getItem('token');
    const authHeader = token?.startsWith('Bearer ') ? token : `Bearer ${token}`;
    return { Authorization: authHeader };
  }

  async create(data: Partial<Device>): Promise<Device> {
    const response = await axios.post<Device>(this.URL, data, {
      headers: this.getAuthHeader()
    });
    return response.data;
  }

  async update(id: number, data: Partial<Device>): Promise<Device> {
    const response = await axios.put<Device>(`${this.URL}/${id}`, data, {
      headers: this.getAuthHeader()
    });
    return response.data;
  }

  async delete(id: number): Promise<void> {
    await axios.delete(`${this.URL}/${id}`, {
      headers: this.getAuthHeader()
    });
  }
}
