import { Injectable } from '@angular/core';
import axios from 'axios';
import { Organization } from '../models/organization.model';

@Injectable({
  providedIn: 'root'
})
  export class OrganizationService {
    private URL = 'http://localhost:8080/organizations';

    async getAllOrganizations(): Promise<Organization[]> {
      const token = localStorage.getItem('token');
      const headers = token
        ? { Authorization: token.startsWith('Bearer ') ? token : `Bearer ${token}` }
        : {};

      const response = await axios.get<Organization[] | Organization>(`${this.URL}/all`, {
        headers
      });

      if (Array.isArray(response.data)) {
        return response.data;
      } else if (response.data) {
        return [response.data];
      }
      return [];
    }

    async getCurrentOrganization(): Promise<Organization | null> {
    const token = localStorage.getItem('token');

    if (!token) return null;

    const authHeader = token.startsWith('Bearer ') ? token : `Bearer ${token}`;

    const response = await axios.get<Organization>(this.URL, {
      headers: {
        Authorization: authHeader
      }
    });

    return response.data || null;
  }

  private getAuthHeader() {
    const token = localStorage.getItem('token');
    const authHeader = token?.startsWith('Bearer ') ? token : `Bearer ${token}`;
    return { Authorization: authHeader };
  }

  async create(data: Partial<Organization>): Promise<Organization> {
    const response = await axios.post<Organization>(this.URL, data, {
      headers: this.getAuthHeader()
    });
    return response.data;
  }

  async update(id: number, data: Partial<Organization>): Promise<Organization> {
    const response = await axios.put<Organization>(`${this.URL}/${id}`, data, {
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
