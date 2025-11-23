import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IdentityDocumentReview from './identity-document-review';
import IdentityDocumentReviewDetail from './identity-document-review-detail';
import IdentityDocumentReviewUpdate from './identity-document-review-update';
import IdentityDocumentReviewDeleteDialog from './identity-document-review-delete-dialog';

const IdentityDocumentReviewRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IdentityDocumentReview />} />
    <Route path="new" element={<IdentityDocumentReviewUpdate />} />
    <Route path=":id">
      <Route index element={<IdentityDocumentReviewDetail />} />
      <Route path="edit" element={<IdentityDocumentReviewUpdate />} />
      <Route path="delete" element={<IdentityDocumentReviewDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IdentityDocumentReviewRoutes;
