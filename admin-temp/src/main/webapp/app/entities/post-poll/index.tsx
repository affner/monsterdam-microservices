import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PostPoll from './post-poll';
import PostPollDetail from './post-poll-detail';
import PostPollUpdate from './post-poll-update';
import PostPollDeleteDialog from './post-poll-delete-dialog';

const PostPollRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PostPoll />} />
    <Route path="new" element={<PostPollUpdate />} />
    <Route path=":id">
      <Route index element={<PostPollDetail />} />
      <Route path="edit" element={<PostPollUpdate />} />
      <Route path="delete" element={<PostPollDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PostPollRoutes;
