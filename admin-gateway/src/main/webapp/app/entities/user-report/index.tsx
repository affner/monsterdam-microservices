import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserReport from './user-report';
import UserReportDetail from './user-report-detail';
import UserReportUpdate from './user-report-update';
import UserReportDeleteDialog from './user-report-delete-dialog';

const UserReportRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserReport />} />
    <Route path="new" element={<UserReportUpdate />} />
    <Route path=":id">
      <Route index element={<UserReportDetail />} />
      <Route path="edit" element={<UserReportUpdate />} />
      <Route path="delete" element={<UserReportDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserReportRoutes;
