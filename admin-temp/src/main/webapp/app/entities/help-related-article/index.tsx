import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HelpRelatedArticle from './help-related-article';
import HelpRelatedArticleDetail from './help-related-article-detail';
import HelpRelatedArticleUpdate from './help-related-article-update';
import HelpRelatedArticleDeleteDialog from './help-related-article-delete-dialog';

const HelpRelatedArticleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HelpRelatedArticle />} />
    <Route path="new" element={<HelpRelatedArticleUpdate />} />
    <Route path=":id">
      <Route index element={<HelpRelatedArticleDetail />} />
      <Route path="edit" element={<HelpRelatedArticleUpdate />} />
      <Route path="delete" element={<HelpRelatedArticleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HelpRelatedArticleRoutes;
