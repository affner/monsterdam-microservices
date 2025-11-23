import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import AdminUserProfile from './admin-user-profile';
import IdentityDocumentReview from './identity-document-review';
import DocumentReviewObservation from './document-review-observation';
import IdentityDocument from './identity-document';
import AssistanceTicket from './assistance-ticket';
import ModerationAction from './moderation-action';
import AdminAnnouncement from './admin-announcement';
import UserReport from './user-report';
import AccountingRecord from './accounting-record';
import FinancialStatement from './financial-statement';
import TaxDeclaration from './tax-declaration';
import Budget from './budget';
import Asset from './asset';
import Liability from './liability';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('admin', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="admin-user-profile/*" element={<AdminUserProfile />} />
        <Route path="identity-document-review/*" element={<IdentityDocumentReview />} />
        <Route path="document-review-observation/*" element={<DocumentReviewObservation />} />
        <Route path="identity-document/*" element={<IdentityDocument />} />
        <Route path="assistance-ticket/*" element={<AssistanceTicket />} />
        <Route path="moderation-action/*" element={<ModerationAction />} />
        <Route path="admin-announcement/*" element={<AdminAnnouncement />} />
        <Route path="user-report/*" element={<UserReport />} />
        <Route path="accounting-record/*" element={<AccountingRecord />} />
        <Route path="financial-statement/*" element={<FinancialStatement />} />
        <Route path="tax-declaration/*" element={<TaxDeclaration />} />
        <Route path="budget/*" element={<Budget />} />
        <Route path="asset/*" element={<Asset />} />
        <Route path="liability/*" element={<Liability />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
