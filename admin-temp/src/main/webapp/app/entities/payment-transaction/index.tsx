import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaymentTransaction from './payment-transaction';
import PaymentTransactionDetail from './payment-transaction-detail';
import PaymentTransactionUpdate from './payment-transaction-update';
import PaymentTransactionDeleteDialog from './payment-transaction-delete-dialog';

const PaymentTransactionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaymentTransaction />} />
    <Route path="new" element={<PaymentTransactionUpdate />} />
    <Route path=":id">
      <Route index element={<PaymentTransactionDetail />} />
      <Route path="edit" element={<PaymentTransactionUpdate />} />
      <Route path="delete" element={<PaymentTransactionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaymentTransactionRoutes;
