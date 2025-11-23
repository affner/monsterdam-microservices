import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './identity-document-review.reducer';

export const IdentityDocumentReviewDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const identityDocumentReviewEntity = useAppSelector(state => state.admin.identityDocumentReview.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="identityDocumentReviewDetailsHeading">
          <Translate contentKey="adminApp.identityDocumentReview.detail.title">IdentityDocumentReview</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{identityDocumentReviewEntity.id}</dd>
          <dt>
            <span id="documentStatus">
              <Translate contentKey="adminApp.identityDocumentReview.documentStatus">Document Status</Translate>
            </span>
          </dt>
          <dd>{identityDocumentReviewEntity.documentStatus}</dd>
          <dt>
            <span id="resolutionDate">
              <Translate contentKey="adminApp.identityDocumentReview.resolutionDate">Resolution Date</Translate>
            </span>
          </dt>
          <dd>
            {identityDocumentReviewEntity.resolutionDate ? (
              <TextFormat value={identityDocumentReviewEntity.resolutionDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="reviewStatus">
              <Translate contentKey="adminApp.identityDocumentReview.reviewStatus">Review Status</Translate>
            </span>
          </dt>
          <dd>{identityDocumentReviewEntity.reviewStatus}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.identityDocumentReview.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {identityDocumentReviewEntity.createdDate ? (
              <TextFormat value={identityDocumentReviewEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.identityDocumentReview.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {identityDocumentReviewEntity.lastModifiedDate ? (
              <TextFormat value={identityDocumentReviewEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.identityDocumentReview.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{identityDocumentReviewEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.identityDocumentReview.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{identityDocumentReviewEntity.lastModifiedBy}</dd>
          <dt>
            <Translate contentKey="adminApp.identityDocumentReview.ticket">Ticket</Translate>
          </dt>
          <dd>{identityDocumentReviewEntity.ticket ? identityDocumentReviewEntity.ticket.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/identity-document-review" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/identity-document-review/${identityDocumentReviewEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IdentityDocumentReviewDetail;
