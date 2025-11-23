import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HelpQuestion from './help-question';
import HelpQuestionDetail from './help-question-detail';
import HelpQuestionUpdate from './help-question-update';
import HelpQuestionDeleteDialog from './help-question-delete-dialog';

const HelpQuestionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HelpQuestion />} />
    <Route path="new" element={<HelpQuestionUpdate />} />
    <Route path=":id">
      <Route index element={<HelpQuestionDetail />} />
      <Route path="edit" element={<HelpQuestionUpdate />} />
      <Route path="delete" element={<HelpQuestionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HelpQuestionRoutes;
