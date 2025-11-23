import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './special-title.reducer';

export const SpecialTitleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const specialTitleEntity = useAppSelector(state => state.admin.specialTitle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="specialTitleDetailsHeading">
          <Translate contentKey="adminApp.specialTitle.detail.title">SpecialTitle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{specialTitleEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.specialTitle.description">Description</Translate>
            </span>
          </dt>
          <dd>{specialTitleEntity.description}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.specialTitle.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {specialTitleEntity.createdDate ? (
              <TextFormat value={specialTitleEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.specialTitle.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {specialTitleEntity.lastModifiedDate ? (
              <TextFormat value={specialTitleEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.specialTitle.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{specialTitleEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.specialTitle.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{specialTitleEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.specialTitle.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{specialTitleEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/special-title" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/special-title/${specialTitleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpecialTitleDetail;
