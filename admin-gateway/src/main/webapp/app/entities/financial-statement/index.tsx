import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FinancialStatement from './financial-statement';
import FinancialStatementDetail from './financial-statement-detail';
import FinancialStatementUpdate from './financial-statement-update';
import FinancialStatementDeleteDialog from './financial-statement-delete-dialog';

const FinancialStatementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FinancialStatement />} />
    <Route path="new" element={<FinancialStatementUpdate />} />
    <Route path=":id">
      <Route index element={<FinancialStatementDetail />} />
      <Route path="edit" element={<FinancialStatementUpdate />} />
      <Route path="delete" element={<FinancialStatementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FinancialStatementRoutes;
