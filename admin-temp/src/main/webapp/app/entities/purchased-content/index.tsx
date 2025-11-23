import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PurchasedContent from './purchased-content';
import PurchasedContentDetail from './purchased-content-detail';
import PurchasedContentUpdate from './purchased-content-update';
import PurchasedContentDeleteDialog from './purchased-content-delete-dialog';

const PurchasedContentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PurchasedContent />} />
    <Route path="new" element={<PurchasedContentUpdate />} />
    <Route path=":id">
      <Route index element={<PurchasedContentDetail />} />
      <Route path="edit" element={<PurchasedContentUpdate />} />
      <Route path="delete" element={<PurchasedContentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PurchasedContentRoutes;
