import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserEvent from './user-event';
import UserEventDetail from './user-event-detail';
import UserEventUpdate from './user-event-update';
import UserEventDeleteDialog from './user-event-delete-dialog';

const UserEventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserEvent />} />
    <Route path="new" element={<UserEventUpdate />} />
    <Route path=":id">
      <Route index element={<UserEventDetail />} />
      <Route path="edit" element={<UserEventUpdate />} />
      <Route path="delete" element={<UserEventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserEventRoutes;
