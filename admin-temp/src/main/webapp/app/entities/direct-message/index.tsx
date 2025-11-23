import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DirectMessage from './direct-message';
import DirectMessageDetail from './direct-message-detail';
import DirectMessageUpdate from './direct-message-update';
import DirectMessageDeleteDialog from './direct-message-delete-dialog';

const DirectMessageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DirectMessage />} />
    <Route path="new" element={<DirectMessageUpdate />} />
    <Route path=":id">
      <Route index element={<DirectMessageDetail />} />
      <Route path="edit" element={<DirectMessageUpdate />} />
      <Route path="delete" element={<DirectMessageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DirectMessageRoutes;
