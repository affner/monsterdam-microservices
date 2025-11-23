import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SpecialAward from './special-award';
import SpecialAwardDetail from './special-award-detail';
import SpecialAwardUpdate from './special-award-update';
import SpecialAwardDeleteDialog from './special-award-delete-dialog';

const SpecialAwardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SpecialAward />} />
    <Route path="new" element={<SpecialAwardUpdate />} />
    <Route path=":id">
      <Route index element={<SpecialAwardDetail />} />
      <Route path="edit" element={<SpecialAwardUpdate />} />
      <Route path="delete" element={<SpecialAwardDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SpecialAwardRoutes;
