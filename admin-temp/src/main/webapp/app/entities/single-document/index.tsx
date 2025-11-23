import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SingleDocument from './single-document';
import SingleDocumentDetail from './single-document-detail';
import SingleDocumentUpdate from './single-document-update';
import SingleDocumentDeleteDialog from './single-document-delete-dialog';

const SingleDocumentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SingleDocument />} />
    <Route path="new" element={<SingleDocumentUpdate />} />
    <Route path=":id">
      <Route index element={<SingleDocumentDetail />} />
      <Route path="edit" element={<SingleDocumentUpdate />} />
      <Route path="delete" element={<SingleDocumentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SingleDocumentRoutes;
