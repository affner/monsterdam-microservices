import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SpecialReward from './special-reward';
import SpecialRewardDetail from './special-reward-detail';
import SpecialRewardUpdate from './special-reward-update';
import SpecialRewardDeleteDialog from './special-reward-delete-dialog';

const SpecialRewardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SpecialReward />} />
    <Route path="new" element={<SpecialRewardUpdate />} />
    <Route path=":id">
      <Route index element={<SpecialRewardDetail />} />
      <Route path="edit" element={<SpecialRewardUpdate />} />
      <Route path="delete" element={<SpecialRewardDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SpecialRewardRoutes;
