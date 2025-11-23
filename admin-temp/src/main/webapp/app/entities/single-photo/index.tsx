import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SinglePhoto from './single-photo';
import SinglePhotoDetail from './single-photo-detail';
import SinglePhotoUpdate from './single-photo-update';
import SinglePhotoDeleteDialog from './single-photo-delete-dialog';

const SinglePhotoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SinglePhoto />} />
    <Route path="new" element={<SinglePhotoUpdate />} />
    <Route path=":id">
      <Route index element={<SinglePhotoDetail />} />
      <Route path="edit" element={<SinglePhotoUpdate />} />
      <Route path="delete" element={<SinglePhotoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SinglePhotoRoutes;
