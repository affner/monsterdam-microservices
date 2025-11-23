import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubscriptionBundle from './subscription-bundle';
import SubscriptionBundleDetail from './subscription-bundle-detail';
import SubscriptionBundleUpdate from './subscription-bundle-update';
import SubscriptionBundleDeleteDialog from './subscription-bundle-delete-dialog';

const SubscriptionBundleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubscriptionBundle />} />
    <Route path="new" element={<SubscriptionBundleUpdate />} />
    <Route path=":id">
      <Route index element={<SubscriptionBundleDetail />} />
      <Route path="edit" element={<SubscriptionBundleUpdate />} />
      <Route path="delete" element={<SubscriptionBundleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubscriptionBundleRoutes;
