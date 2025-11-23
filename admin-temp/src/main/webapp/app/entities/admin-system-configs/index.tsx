import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AdminSystemConfigs from './admin-system-configs';
import AdminSystemConfigsDetail from './admin-system-configs-detail';
import AdminSystemConfigsUpdate from './admin-system-configs-update';
import AdminSystemConfigsDeleteDialog from './admin-system-configs-delete-dialog';

const AdminSystemConfigsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AdminSystemConfigs />} />
    <Route path="new" element={<AdminSystemConfigsUpdate />} />
    <Route path=":id">
      <Route index element={<AdminSystemConfigsDetail />} />
      <Route path="edit" element={<AdminSystemConfigsUpdate />} />
      <Route path="delete" element={<AdminSystemConfigsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AdminSystemConfigsRoutes;
