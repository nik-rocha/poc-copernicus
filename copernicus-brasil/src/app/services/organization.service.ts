import { Injectable } from '@angular/core';
import axios from 'axios';
import { Organization } from '../models/organization.model';

@Injectable({
  providedIn: 'root'
})
  export class OrganizationService {
    private URL = 'http://localhost:8080/organizations';

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
}
