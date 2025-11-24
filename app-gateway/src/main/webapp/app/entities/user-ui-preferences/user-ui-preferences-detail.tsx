import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-ui-preferences.reducer';

export const UserUIPreferencesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userUIPreferencesEntity = useAppSelector(state => state.monsterdam.userUIPreferences.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userUIPreferencesDetailsHeading">
          <Translate contentKey="monsterdamApp.userUIPreferences.detail.title">UserUIPreferences</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userUIPreferencesEntity.id}</dd>
          <dt>
            <span id="preferences">
              <Translate contentKey="monsterdamApp.userUIPreferences.preferences">Preferences</Translate>
            </span>
          </dt>
          <dd>{userUIPreferencesEntity.preferences}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamApp.userUIPreferences.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userUIPreferencesEntity.createdDate ? (
              <TextFormat value={userUIPreferencesEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamApp.userUIPreferences.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userUIPreferencesEntity.lastModifiedDate ? (
              <TextFormat value={userUIPreferencesEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamApp.userUIPreferences.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userUIPreferencesEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamApp.userUIPreferences.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userUIPreferencesEntity.lastModifiedBy}</dd>
        </dl>
        <Button tag={Link} to="/user-ui-preferences" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-ui-preferences/${userUIPreferencesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserUIPreferencesDetail;
