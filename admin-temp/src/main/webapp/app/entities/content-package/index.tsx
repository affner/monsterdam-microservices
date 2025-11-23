import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ContentPackage from './content-package';
import ContentPackageDetail from './content-package-detail';
import ContentPackageUpdate from './content-package-update';
import ContentPackageDeleteDialog from './content-package-delete-dialog';

const ContentPackageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ContentPackage />} />
    <Route path="new" element={<ContentPackageUpdate />} />
    <Route path=":id">
      <Route index element={<ContentPackageDetail />} />
      <Route path="edit" element={<ContentPackageUpdate />} />
      <Route path="delete" element={<ContentPackageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContentPackageRoutes;
