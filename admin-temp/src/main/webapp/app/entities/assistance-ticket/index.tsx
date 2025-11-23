import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AssistanceTicket from './assistance-ticket';
import AssistanceTicketDetail from './assistance-ticket-detail';
import AssistanceTicketUpdate from './assistance-ticket-update';
import AssistanceTicketDeleteDialog from './assistance-ticket-delete-dialog';

const AssistanceTicketRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AssistanceTicket />} />
    <Route path="new" element={<AssistanceTicketUpdate />} />
    <Route path=":id">
      <Route index element={<AssistanceTicketDetail />} />
      <Route path="edit" element={<AssistanceTicketUpdate />} />
      <Route path="delete" element={<AssistanceTicketDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AssistanceTicketRoutes;
