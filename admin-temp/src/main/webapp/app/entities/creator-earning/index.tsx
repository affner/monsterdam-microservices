import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CreatorEarning from './creator-earning';
import CreatorEarningDetail from './creator-earning-detail';
import CreatorEarningUpdate from './creator-earning-update';
import CreatorEarningDeleteDialog from './creator-earning-delete-dialog';

const CreatorEarningRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CreatorEarning />} />
    <Route path="new" element={<CreatorEarningUpdate />} />
    <Route path=":id">
      <Route index element={<CreatorEarningDetail />} />
      <Route path="edit" element={<CreatorEarningUpdate />} />
      <Route path="delete" element={<CreatorEarningDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CreatorEarningRoutes;
