import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AdminUserProfile from './admin-user-profile';
import AdminUserProfileDetail from './admin-user-profile-detail';
import AdminUserProfileUpdate from './admin-user-profile-update';
import AdminUserProfileDeleteDialog from './admin-user-profile-delete-dialog';

const AdminUserProfileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AdminUserProfile />} />
    <Route path="new" element={<AdminUserProfileUpdate />} />
    <Route path=":id">
      <Route index element={<AdminUserProfileDetail />} />
      <Route path="edit" element={<AdminUserProfileUpdate />} />
      <Route path="delete" element={<AdminUserProfileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AdminUserProfileRoutes;
