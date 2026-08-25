import { Injectable } from '@angular/core';
import axios from 'axios';
import { Collaborator } from '../models/organization.model';

@Injectable({ providedIn: 'root' })
export class CollaboratorService {
  private URL = 'http://localhost:8080/collaborators';

  async getAll(): Promise<Collaborator[]> {
    const token = localStorage.getItem('token');
    const authHeader = token?.startsWith('Bearer ') ? token : `Bearer ${token}`;

    const response = await axios.get<Collaborator[]>(this.URL, {
      headers: { Authorization: authHeader }
    });

    return response.data;
  }

  private getAuthHeader() {
    const token = localStorage.getItem('token');
    const authHeader = token?.startsWith('Bearer ') ? token : `Bearer ${token}`;
    return { Authorization: authHeader };
  }

  async create(data: Partial<Collaborator>): Promise<Collaborator> {
    const response = await axios.post<Collaborator>(this.URL, data, {
      headers: this.getAuthHeader()
    });
    return response.data;
  }

  async update(id: number, data: Partial<Collaborator>): Promise<Collaborator> {
    const response = await axios.put<Collaborator>(`${this.URL}/${id}`, data, {
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
