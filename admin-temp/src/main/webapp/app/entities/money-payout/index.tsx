import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MoneyPayout from './money-payout';
import MoneyPayoutDetail from './money-payout-detail';
import MoneyPayoutUpdate from './money-payout-update';
import MoneyPayoutDeleteDialog from './money-payout-delete-dialog';

const MoneyPayoutRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MoneyPayout />} />
    <Route path="new" element={<MoneyPayoutUpdate />} />
    <Route path=":id">
      <Route index element={<MoneyPayoutDetail />} />
      <Route path="edit" element={<MoneyPayoutUpdate />} />
      <Route path="delete" element={<MoneyPayoutDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MoneyPayoutRoutes;
