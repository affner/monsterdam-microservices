import dayjs from 'dayjs';
import { IIdentityDocumentReview } from 'app/shared/model/identity-document-review.model';
import { DocumentStatus } from 'app/shared/model/enumerations/document-status.model';
import { DocumentType } from 'app/shared/model/enumerations/document-type.model';

export interface IIdentityDocument {
  id?: number;
  documentName?: string;
  documentDescription?: string | null;
  documentStatus?: keyof typeof DocumentStatus | null;
  documentType?: keyof typeof DocumentType | null;
  fileDocumentContentType?: string;
  fileDocument?: string;
  fileDocumentS3Key?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  review?: IIdentityDocumentReview | null;
}

export const defaultValue: Readonly<IIdentityDocument> = {};
