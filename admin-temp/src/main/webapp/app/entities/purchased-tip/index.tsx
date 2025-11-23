import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PurchasedTip from './purchased-tip';
import PurchasedTipDetail from './purchased-tip-detail';
import PurchasedTipUpdate from './purchased-tip-update';
import PurchasedTipDeleteDialog from './purchased-tip-delete-dialog';

const PurchasedTipRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PurchasedTip />} />
    <Route path="new" element={<PurchasedTipUpdate />} />
    <Route path=":id">
      <Route index element={<PurchasedTipDetail />} />
      <Route path="edit" element={<PurchasedTipUpdate />} />
      <Route path="delete" element={<PurchasedTipDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PurchasedTipRoutes;
