import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ModerationAction from './moderation-action';
import ModerationActionDetail from './moderation-action-detail';
import ModerationActionUpdate from './moderation-action-update';
import ModerationActionDeleteDialog from './moderation-action-delete-dialog';

const ModerationActionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ModerationAction />} />
    <Route path="new" element={<ModerationActionUpdate />} />
    <Route path=":id">
      <Route index element={<ModerationActionDetail />} />
      <Route path="edit" element={<ModerationActionUpdate />} />
      <Route path="delete" element={<ModerationActionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ModerationActionRoutes;
