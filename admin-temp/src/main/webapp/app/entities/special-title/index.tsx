import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SpecialTitle from './special-title';
import SpecialTitleDetail from './special-title-detail';
import SpecialTitleUpdate from './special-title-update';
import SpecialTitleDeleteDialog from './special-title-delete-dialog';

const SpecialTitleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SpecialTitle />} />
    <Route path="new" element={<SpecialTitleUpdate />} />
    <Route path=":id">
      <Route index element={<SpecialTitleDetail />} />
      <Route path="edit" element={<SpecialTitleUpdate />} />
      <Route path="delete" element={<SpecialTitleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SpecialTitleRoutes;
