import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Liability from './liability';
import LiabilityDetail from './liability-detail';
import LiabilityUpdate from './liability-update';
import LiabilityDeleteDialog from './liability-delete-dialog';

const LiabilityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Liability />} />
    <Route path="new" element={<LiabilityUpdate />} />
    <Route path=":id">
      <Route index element={<LiabilityDetail />} />
      <Route path="edit" element={<LiabilityUpdate />} />
      <Route path="delete" element={<LiabilityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LiabilityRoutes;
