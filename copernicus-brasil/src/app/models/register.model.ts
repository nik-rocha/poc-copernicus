export interface RegisterUserRequest {
  fullName: string;
  email: string;
  password: string;
  accessLevel: string;
  hasOrganization: boolean;
  corporateName?: string;
  registrationCode?: string;
  organizationId?: number | null;
}
