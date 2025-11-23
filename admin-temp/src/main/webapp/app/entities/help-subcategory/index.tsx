import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HelpSubcategory from './help-subcategory';
import HelpSubcategoryDetail from './help-subcategory-detail';
import HelpSubcategoryUpdate from './help-subcategory-update';
import HelpSubcategoryDeleteDialog from './help-subcategory-delete-dialog';

const HelpSubcategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HelpSubcategory />} />
    <Route path="new" element={<HelpSubcategoryUpdate />} />
    <Route path=":id">
      <Route index element={<HelpSubcategoryDetail />} />
      <Route path="edit" element={<HelpSubcategoryUpdate />} />
      <Route path="delete" element={<HelpSubcategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HelpSubcategoryRoutes;
