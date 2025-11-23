import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SingleVideo from './single-video';
import SingleVideoDetail from './single-video-detail';
import SingleVideoUpdate from './single-video-update';
import SingleVideoDeleteDialog from './single-video-delete-dialog';

const SingleVideoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SingleVideo />} />
    <Route path="new" element={<SingleVideoUpdate />} />
    <Route path=":id">
      <Route index element={<SingleVideoDetail />} />
      <Route path="edit" element={<SingleVideoUpdate />} />
      <Route path="delete" element={<SingleVideoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SingleVideoRoutes;
