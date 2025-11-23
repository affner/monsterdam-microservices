import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserMention from './user-mention';
import UserMentionDetail from './user-mention-detail';
import UserMentionUpdate from './user-mention-update';
import UserMentionDeleteDialog from './user-mention-delete-dialog';

const UserMentionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserMention />} />
    <Route path="new" element={<UserMentionUpdate />} />
    <Route path=":id">
      <Route index element={<UserMentionDetail />} />
      <Route path="edit" element={<UserMentionUpdate />} />
      <Route path="delete" element={<UserMentionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserMentionRoutes;
