import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HelpCategory from './help-category';
import HelpCategoryDetail from './help-category-detail';
import HelpCategoryUpdate from './help-category-update';
import HelpCategoryDeleteDialog from './help-category-delete-dialog';

const HelpCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HelpCategory />} />
    <Route path="new" element={<HelpCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<HelpCategoryDetail />} />
      <Route path="edit" element={<HelpCategoryUpdate />} />
      <Route path="delete" element={<HelpCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HelpCategoryRoutes;
