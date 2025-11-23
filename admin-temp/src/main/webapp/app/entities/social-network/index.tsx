import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SocialNetwork from './social-network';
import SocialNetworkDetail from './social-network-detail';
import SocialNetworkUpdate from './social-network-update';
import SocialNetworkDeleteDialog from './social-network-delete-dialog';

const SocialNetworkRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SocialNetwork />} />
    <Route path="new" element={<SocialNetworkUpdate />} />
    <Route path=":id">
      <Route index element={<SocialNetworkDetail />} />
      <Route path="edit" element={<SocialNetworkUpdate />} />
      <Route path="delete" element={<SocialNetworkDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SocialNetworkRoutes;
