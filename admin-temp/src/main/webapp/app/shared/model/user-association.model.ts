import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { AssociationStatus } from 'app/shared/model/enumerations/association-status.model';

export interface IUserAssociation {
  id?: number;
  requestedDate?: dayjs.Dayjs;
  status?: keyof typeof AssociationStatus | null;
  associationToken?: string;
  expiryDate?: dayjs.Dayjs;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  owner?: IUserProfile;
}

export const defaultValue: Readonly<IUserAssociation> = {
  isDeleted: false,
};
