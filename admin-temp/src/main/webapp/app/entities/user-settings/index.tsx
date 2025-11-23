import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserSettings from './user-settings';
import UserSettingsDetail from './user-settings-detail';
import UserSettingsUpdate from './user-settings-update';
import UserSettingsDeleteDialog from './user-settings-delete-dialog';

const UserSettingsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserSettings />} />
    <Route path="new" element={<UserSettingsUpdate />} />
    <Route path=":id">
      <Route index element={<UserSettingsDetail />} />
      <Route path="edit" element={<UserSettingsUpdate />} />
      <Route path="delete" element={<UserSettingsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserSettingsRoutes;
