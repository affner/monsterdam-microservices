import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PollOption from './poll-option';
import PollOptionDetail from './poll-option-detail';
import PollOptionUpdate from './poll-option-update';
import PollOptionDeleteDialog from './poll-option-delete-dialog';

const PollOptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PollOption />} />
    <Route path="new" element={<PollOptionUpdate />} />
    <Route path=":id">
      <Route index element={<PollOptionDetail />} />
      <Route path="edit" element={<PollOptionUpdate />} />
      <Route path="delete" element={<PollOptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PollOptionRoutes;
