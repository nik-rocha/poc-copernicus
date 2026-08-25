export interface Organization {
  idOrganization?: number;
  corporateName: string;
  registrationCode: string;
  collaborators?: Collaborator[];
  devices?: Device[];
}

export interface Collaborator {
  idCollaborator?: number;
  fullName: string;
  email: string;
  password: string;
  accessLevel: string;
  organization?: Organization;
  organizationId?: number;
  organizationName?: string;
}

export interface Device {
  idDevice?: number;
  model: string;
  assetTag: string;
  organization?: Organization;
  organizationId?: number;
  organizationName?: string;
}
