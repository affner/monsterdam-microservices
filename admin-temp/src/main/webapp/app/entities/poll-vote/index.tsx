import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PollVote from './poll-vote';
import PollVoteDetail from './poll-vote-detail';
import PollVoteUpdate from './poll-vote-update';
import PollVoteDeleteDialog from './poll-vote-delete-dialog';

const PollVoteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PollVote />} />
    <Route path="new" element={<PollVoteUpdate />} />
    <Route path=":id">
      <Route index element={<PollVoteDetail />} />
      <Route path="edit" element={<PollVoteUpdate />} />
      <Route path="delete" element={<PollVoteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PollVoteRoutes;
