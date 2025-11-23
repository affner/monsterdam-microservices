import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TaxDeclaration from './tax-declaration';
import TaxDeclarationDetail from './tax-declaration-detail';
import TaxDeclarationUpdate from './tax-declaration-update';
import TaxDeclarationDeleteDialog from './tax-declaration-delete-dialog';

const TaxDeclarationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TaxDeclaration />} />
    <Route path="new" element={<TaxDeclarationUpdate />} />
    <Route path=":id">
      <Route index element={<TaxDeclarationDetail />} />
      <Route path="edit" element={<TaxDeclarationUpdate />} />
      <Route path="delete" element={<TaxDeclarationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TaxDeclarationRoutes;
