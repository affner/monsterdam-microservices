import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BookMark from './book-mark';
import BookMarkDetail from './book-mark-detail';
import BookMarkUpdate from './book-mark-update';
import BookMarkDeleteDialog from './book-mark-delete-dialog';

const BookMarkRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BookMark />} />
    <Route path="new" element={<BookMarkUpdate />} />
    <Route path=":id">
      <Route index element={<BookMarkDetail />} />
      <Route path="edit" element={<BookMarkUpdate />} />
      <Route path="delete" element={<BookMarkDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BookMarkRoutes;
