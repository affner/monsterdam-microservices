import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LikeMark from './like-mark';
import LikeMarkDetail from './like-mark-detail';
import LikeMarkUpdate from './like-mark-update';
import LikeMarkDeleteDialog from './like-mark-delete-dialog';

const LikeMarkRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LikeMark />} />
    <Route path="new" element={<LikeMarkUpdate />} />
    <Route path=":id">
      <Route index element={<LikeMarkDetail />} />
      <Route path="edit" element={<LikeMarkUpdate />} />
      <Route path="delete" element={<LikeMarkDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LikeMarkRoutes;
