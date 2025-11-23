import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PurchasedSubscription from './purchased-subscription';
import PurchasedSubscriptionDetail from './purchased-subscription-detail';
import PurchasedSubscriptionUpdate from './purchased-subscription-update';
import PurchasedSubscriptionDeleteDialog from './purchased-subscription-delete-dialog';

const PurchasedSubscriptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PurchasedSubscription />} />
    <Route path="new" element={<PurchasedSubscriptionUpdate />} />
    <Route path=":id">
      <Route index element={<PurchasedSubscriptionDetail />} />
      <Route path="edit" element={<PurchasedSubscriptionUpdate />} />
      <Route path="delete" element={<PurchasedSubscriptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PurchasedSubscriptionRoutes;
