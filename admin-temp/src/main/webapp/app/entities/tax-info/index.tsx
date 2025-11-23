import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TaxInfo from './tax-info';
import TaxInfoDetail from './tax-info-detail';
import TaxInfoUpdate from './tax-info-update';
import TaxInfoDeleteDialog from './tax-info-delete-dialog';

const TaxInfoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TaxInfo />} />
    <Route path="new" element={<TaxInfoUpdate />} />
    <Route path=":id">
      <Route index element={<TaxInfoDetail />} />
      <Route path="edit" element={<TaxInfoUpdate />} />
      <Route path="delete" element={<TaxInfoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TaxInfoRoutes;
