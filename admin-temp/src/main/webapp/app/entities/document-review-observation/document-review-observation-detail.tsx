import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './document-review-observation.reducer';

export const DocumentReviewObservationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const documentReviewObservationEntity = useAppSelector(state => state.admin.documentReviewObservation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentReviewObservationDetailsHeading">
          <Translate contentKey="adminApp.documentReviewObservation.detail.title">DocumentReviewObservation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{documentReviewObservationEntity.id}</dd>
          <dt>
            <span id="commentDate">
              <Translate contentKey="adminApp.documentReviewObservation.commentDate">Comment Date</Translate>
            </span>
          </dt>
          <dd>
            {documentReviewObservationEntity.commentDate ? (
              <TextFormat value={documentReviewObservationEntity.commentDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="comment">
              <Translate contentKey="adminApp.documentReviewObservation.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{documentReviewObservationEntity.comment}</dd>
          <dt>
            <Translate contentKey="adminApp.documentReviewObservation.review">Review</Translate>
          </dt>
          <dd>{documentReviewObservationEntity.review ? documentReviewObservationEntity.review.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/document-review-observation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/document-review-observation/${documentReviewObservationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentReviewObservationDetail;
