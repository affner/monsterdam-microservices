import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SingleAudio from './single-audio';
import SingleAudioDetail from './single-audio-detail';
import SingleAudioUpdate from './single-audio-update';
import SingleAudioDeleteDialog from './single-audio-delete-dialog';

const SingleAudioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SingleAudio />} />
    <Route path="new" element={<SingleAudioUpdate />} />
    <Route path=":id">
      <Route index element={<SingleAudioDetail />} />
      <Route path="edit" element={<SingleAudioUpdate />} />
      <Route path="delete" element={<SingleAudioDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SingleAudioRoutes;
