import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserLite from './user-lite';
import UserLiteDetail from './user-lite-detail';
import UserLiteUpdate from './user-lite-update';
import UserLiteDeleteDialog from './user-lite-delete-dialog';

const UserLiteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserLite />} />
    <Route path="new" element={<UserLiteUpdate />} />
    <Route path=":id">
      <Route index element={<UserLiteDetail />} />
      <Route path="edit" element={<UserLiteUpdate />} />
      <Route path="delete" element={<UserLiteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserLiteRoutes;
