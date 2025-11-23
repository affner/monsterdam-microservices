import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VideoStory from './video-story';
import VideoStoryDetail from './video-story-detail';
import VideoStoryUpdate from './video-story-update';
import VideoStoryDeleteDialog from './video-story-delete-dialog';

const VideoStoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VideoStory />} />
    <Route path="new" element={<VideoStoryUpdate />} />
    <Route path=":id">
      <Route index element={<VideoStoryDetail />} />
      <Route path="edit" element={<VideoStoryUpdate />} />
      <Route path="delete" element={<VideoStoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VideoStoryRoutes;
