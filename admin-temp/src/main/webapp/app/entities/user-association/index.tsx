import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserAssociation from './user-association';
import UserAssociationDetail from './user-association-detail';
import UserAssociationUpdate from './user-association-update';
import UserAssociationDeleteDialog from './user-association-delete-dialog';

const UserAssociationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserAssociation />} />
    <Route path="new" element={<UserAssociationUpdate />} />
    <Route path=":id">
      <Route index element={<UserAssociationDetail />} />
      <Route path="edit" element={<UserAssociationUpdate />} />
      <Route path="delete" element={<UserAssociationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserAssociationRoutes;
