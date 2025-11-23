import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PostFeed from './post-feed';
import PostFeedDetail from './post-feed-detail';
import PostFeedUpdate from './post-feed-update';
import PostFeedDeleteDialog from './post-feed-delete-dialog';

const PostFeedRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PostFeed />} />
    <Route path="new" element={<PostFeedUpdate />} />
    <Route path=":id">
      <Route index element={<PostFeedDetail />} />
      <Route path="edit" element={<PostFeedUpdate />} />
      <Route path="delete" element={<PostFeedDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PostFeedRoutes;
