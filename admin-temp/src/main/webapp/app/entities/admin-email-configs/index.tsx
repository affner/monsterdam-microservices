import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AdminEmailConfigs from './admin-email-configs';
import AdminEmailConfigsDetail from './admin-email-configs-detail';
import AdminEmailConfigsUpdate from './admin-email-configs-update';
import AdminEmailConfigsDeleteDialog from './admin-email-configs-delete-dialog';

const AdminEmailConfigsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AdminEmailConfigs />} />
    <Route path="new" element={<AdminEmailConfigsUpdate />} />
    <Route path=":id">
      <Route index element={<AdminEmailConfigsDetail />} />
      <Route path="edit" element={<AdminEmailConfigsUpdate />} />
      <Route path="delete" element={<AdminEmailConfigsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AdminEmailConfigsRoutes;
