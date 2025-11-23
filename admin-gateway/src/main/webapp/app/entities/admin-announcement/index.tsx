import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AdminAnnouncement from './admin-announcement';
import AdminAnnouncementDetail from './admin-announcement-detail';
import AdminAnnouncementUpdate from './admin-announcement-update';
import AdminAnnouncementDeleteDialog from './admin-announcement-delete-dialog';

const AdminAnnouncementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AdminAnnouncement />} />
    <Route path="new" element={<AdminAnnouncementUpdate />} />
    <Route path=":id">
      <Route index element={<AdminAnnouncementDetail />} />
      <Route path="edit" element={<AdminAnnouncementUpdate />} />
      <Route path="delete" element={<AdminAnnouncementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AdminAnnouncementRoutes;
