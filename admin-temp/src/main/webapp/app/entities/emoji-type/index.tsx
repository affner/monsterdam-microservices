import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EmojiType from './emoji-type';
import EmojiTypeDetail from './emoji-type-detail';
import EmojiTypeUpdate from './emoji-type-update';
import EmojiTypeDeleteDialog from './emoji-type-delete-dialog';

const EmojiTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EmojiType />} />
    <Route path="new" element={<EmojiTypeUpdate />} />
    <Route path=":id">
      <Route index element={<EmojiTypeDetail />} />
      <Route path="edit" element={<EmojiTypeUpdate />} />
      <Route path="delete" element={<EmojiTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmojiTypeRoutes;
