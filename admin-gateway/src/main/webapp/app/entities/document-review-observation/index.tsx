import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DocumentReviewObservation from './document-review-observation';
import DocumentReviewObservationDetail from './document-review-observation-detail';
import DocumentReviewObservationUpdate from './document-review-observation-update';
import DocumentReviewObservationDeleteDialog from './document-review-observation-delete-dialog';

const DocumentReviewObservationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DocumentReviewObservation />} />
    <Route path="new" element={<DocumentReviewObservationUpdate />} />
    <Route path=":id">
      <Route index element={<DocumentReviewObservationDetail />} />
      <Route path="edit" element={<DocumentReviewObservationUpdate />} />
      <Route path="delete" element={<DocumentReviewObservationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DocumentReviewObservationRoutes;
