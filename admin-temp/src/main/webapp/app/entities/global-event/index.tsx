import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import GlobalEvent from './global-event';
import GlobalEventDetail from './global-event-detail';
import GlobalEventUpdate from './global-event-update';
import GlobalEventDeleteDialog from './global-event-delete-dialog';

const GlobalEventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GlobalEvent />} />
    <Route path="new" element={<GlobalEventUpdate />} />
    <Route path=":id">
      <Route index element={<GlobalEventDetail />} />
      <Route path="edit" element={<GlobalEventUpdate />} />
      <Route path="delete" element={<GlobalEventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GlobalEventRoutes;
