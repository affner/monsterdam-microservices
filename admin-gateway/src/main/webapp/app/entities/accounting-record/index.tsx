import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AccountingRecord from './accounting-record';
import AccountingRecordDetail from './accounting-record-detail';
import AccountingRecordUpdate from './accounting-record-update';
import AccountingRecordDeleteDialog from './accounting-record-delete-dialog';

const AccountingRecordRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AccountingRecord />} />
    <Route path="new" element={<AccountingRecordUpdate />} />
    <Route path=":id">
      <Route index element={<AccountingRecordDetail />} />
      <Route path="edit" element={<AccountingRecordUpdate />} />
      <Route path="delete" element={<AccountingRecordDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AccountingRecordRoutes;
