import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaymentProvider from './payment-provider';
import PaymentProviderDetail from './payment-provider-detail';
import PaymentProviderUpdate from './payment-provider-update';
import PaymentProviderDeleteDialog from './payment-provider-delete-dialog';

const PaymentProviderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaymentProvider />} />
    <Route path="new" element={<PaymentProviderUpdate />} />
    <Route path=":id">
      <Route index element={<PaymentProviderDetail />} />
      <Route path="edit" element={<PaymentProviderUpdate />} />
      <Route path="delete" element={<PaymentProviderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaymentProviderRoutes;
