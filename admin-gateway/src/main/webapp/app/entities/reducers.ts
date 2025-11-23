import adminUserProfile from 'app/entities/admin-user-profile/admin-user-profile.reducer';
import identityDocumentReview from 'app/entities/identity-document-review/identity-document-review.reducer';
import documentReviewObservation from 'app/entities/document-review-observation/document-review-observation.reducer';
import identityDocument from 'app/entities/identity-document/identity-document.reducer';
import assistanceTicket from 'app/entities/assistance-ticket/assistance-ticket.reducer';
import moderationAction from 'app/entities/moderation-action/moderation-action.reducer';
import adminAnnouncement from 'app/entities/admin-announcement/admin-announcement.reducer';
import userReport from 'app/entities/user-report/user-report.reducer';
import accountingRecord from 'app/entities/accounting-record/accounting-record.reducer';
import financialStatement from 'app/entities/financial-statement/financial-statement.reducer';
import taxDeclaration from 'app/entities/tax-declaration/tax-declaration.reducer';
import budget from 'app/entities/budget/budget.reducer';
import asset from 'app/entities/asset/asset.reducer';
import liability from 'app/entities/liability/liability.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  adminUserProfile,
  identityDocumentReview,
  documentReviewObservation,
  identityDocument,
  assistanceTicket,
  moderationAction,
  adminAnnouncement,
  userReport,
  accountingRecord,
  financialStatement,
  taxDeclaration,
  budget,
  asset,
  liability,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
