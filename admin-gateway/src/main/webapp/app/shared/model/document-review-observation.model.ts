import dayjs from 'dayjs';
import { IIdentityDocumentReview } from 'app/shared/model/identity-document-review.model';

export interface IDocumentReviewObservation {
  id?: number;
  commentDate?: dayjs.Dayjs | null;
  comment?: string;
  review?: IIdentityDocumentReview | null;
}

export const defaultValue: Readonly<IDocumentReviewObservation> = {};
