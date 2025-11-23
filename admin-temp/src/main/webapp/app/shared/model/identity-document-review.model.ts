import dayjs from 'dayjs';
import { IAssistanceTicket } from 'app/shared/model/assistance-ticket.model';
import { IIdentityDocument } from 'app/shared/model/identity-document.model';
import { IDocumentReviewObservation } from 'app/shared/model/document-review-observation.model';
import { DocumentStatus } from 'app/shared/model/enumerations/document-status.model';
import { ReviewStatus } from 'app/shared/model/enumerations/review-status.model';

export interface IIdentityDocumentReview {
  id?: number;
  documentStatus?: keyof typeof DocumentStatus | null;
  resolutionDate?: dayjs.Dayjs | null;
  reviewStatus?: keyof typeof ReviewStatus | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  ticket?: IAssistanceTicket;
  documents?: IIdentityDocument[] | null;
  observations?: IDocumentReviewObservation[] | null;
}

export const defaultValue: Readonly<IIdentityDocumentReview> = {};
