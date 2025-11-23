import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PayoutMethod from './payout-method';
import PayoutMethodDetail from './payout-method-detail';
import PayoutMethodUpdate from './payout-method-update';
import PayoutMethodDeleteDialog from './payout-method-delete-dialog';

const PayoutMethodRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PayoutMethod />} />
    <Route path="new" element={<PayoutMethodUpdate />} />
    <Route path=":id">
      <Route index element={<PayoutMethodDetail />} />
      <Route path="edit" element={<PayoutMethodUpdate />} />
      <Route path="delete" element={<PayoutMethodDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PayoutMethodRoutes;
