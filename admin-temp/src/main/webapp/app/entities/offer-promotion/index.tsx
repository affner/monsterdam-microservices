import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OfferPromotion from './offer-promotion';
import OfferPromotionDetail from './offer-promotion-detail';
import OfferPromotionUpdate from './offer-promotion-update';
import OfferPromotionDeleteDialog from './offer-promotion-delete-dialog';

const OfferPromotionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OfferPromotion />} />
    <Route path="new" element={<OfferPromotionUpdate />} />
    <Route path=":id">
      <Route index element={<OfferPromotionDetail />} />
      <Route path="edit" element={<OfferPromotionUpdate />} />
      <Route path="delete" element={<OfferPromotionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OfferPromotionRoutes;
