import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './help-subcategory.reducer';

export const HelpSubcategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const helpSubcategoryEntity = useAppSelector(state => state.admin.helpSubcategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="helpSubcategoryDetailsHeading">
          <Translate contentKey="adminApp.helpSubcategory.detail.title">HelpSubcategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{helpSubcategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="adminApp.helpSubcategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{helpSubcategoryEntity.name}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.helpSubcategory.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{helpSubcategoryEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.helpSubcategory.category">Category</Translate>
          </dt>
          <dd>{helpSubcategoryEntity.category ? helpSubcategoryEntity.category.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/help-subcategory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/help-subcategory/${helpSubcategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HelpSubcategoryDetail;
