import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Currency from './currency';
import CurrencyDetail from './currency-detail';
import CurrencyUpdate from './currency-update';
import CurrencyDeleteDialog from './currency-delete-dialog';

const CurrencyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Currency />} />
    <Route path="new" element={<CurrencyUpdate />} />
    <Route path=":id">
      <Route index element={<CurrencyDetail />} />
      <Route path="edit" element={<CurrencyUpdate />} />
      <Route path="delete" element={<CurrencyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CurrencyRoutes;
