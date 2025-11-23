import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SingleLiveStream from './single-live-stream';
import SingleLiveStreamDetail from './single-live-stream-detail';
import SingleLiveStreamUpdate from './single-live-stream-update';
import SingleLiveStreamDeleteDialog from './single-live-stream-delete-dialog';

const SingleLiveStreamRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SingleLiveStream />} />
    <Route path="new" element={<SingleLiveStreamUpdate />} />
    <Route path=":id">
      <Route index element={<SingleLiveStreamDetail />} />
      <Route path="edit" element={<SingleLiveStreamUpdate />} />
      <Route path="delete" element={<SingleLiveStreamDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SingleLiveStreamRoutes;
