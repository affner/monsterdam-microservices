import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PersonalSocialLinks from './personal-social-links';
import PersonalSocialLinksDetail from './personal-social-links-detail';
import PersonalSocialLinksUpdate from './personal-social-links-update';
import PersonalSocialLinksDeleteDialog from './personal-social-links-delete-dialog';

const PersonalSocialLinksRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PersonalSocialLinks />} />
    <Route path="new" element={<PersonalSocialLinksUpdate />} />
    <Route path=":id">
      <Route index element={<PersonalSocialLinksDetail />} />
      <Route path="edit" element={<PersonalSocialLinksUpdate />} />
      <Route path="delete" element={<PersonalSocialLinksDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PersonalSocialLinksRoutes;
