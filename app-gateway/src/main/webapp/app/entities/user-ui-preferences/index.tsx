import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserUIPreferences from './user-ui-preferences';
import UserUIPreferencesDetail from './user-ui-preferences-detail';
import UserUIPreferencesUpdate from './user-ui-preferences-update';
import UserUIPreferencesDeleteDialog from './user-ui-preferences-delete-dialog';

const UserUIPreferencesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserUIPreferences />} />
    <Route path="new" element={<UserUIPreferencesUpdate />} />
    <Route path=":id">
      <Route index element={<UserUIPreferencesDetail />} />
      <Route path="edit" element={<UserUIPreferencesUpdate />} />
      <Route path="delete" element={<UserUIPreferencesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserUIPreferencesRoutes;
