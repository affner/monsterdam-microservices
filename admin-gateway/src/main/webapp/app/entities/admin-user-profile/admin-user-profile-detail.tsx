import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './admin-user-profile.reducer';

export const AdminUserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const adminUserProfileEntity = useAppSelector(state => state.admin.adminUserProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="adminUserProfileDetailsHeading">
          <Translate contentKey="adminApp.adminUserProfile.detail.title">AdminUserProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{adminUserProfileEntity.id}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="adminApp.adminUserProfile.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{adminUserProfileEntity.fullName}</dd>
          <dt>
            <span id="emailAddress">
              <Translate contentKey="adminApp.adminUserProfile.emailAddress">Email Address</Translate>
            </span>
          </dt>
          <dd>{adminUserProfileEntity.emailAddress}</dd>
          <dt>
            <span id="nickName">
              <Translate contentKey="adminApp.adminUserProfile.nickName">Nick Name</Translate>
            </span>
          </dt>
          <dd>{adminUserProfileEntity.nickName}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="adminApp.adminUserProfile.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{adminUserProfileEntity.gender}</dd>
          <dt>
            <span id="mobilePhone">
              <Translate contentKey="adminApp.adminUserProfile.mobilePhone">Mobile Phone</Translate>
            </span>
          </dt>
          <dd>{adminUserProfileEntity.mobilePhone}</dd>
          <dt>
            <span id="lastLoginDate">
              <Translate contentKey="adminApp.adminUserProfile.lastLoginDate">Last Login Date</Translate>
            </span>
          </dt>
          <dd>
            {adminUserProfileEntity.lastLoginDate ? (
              <TextFormat value={adminUserProfileEntity.lastLoginDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="adminApp.adminUserProfile.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {adminUserProfileEntity.birthDate ? (
              <TextFormat value={adminUserProfileEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.adminUserProfile.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {adminUserProfileEntity.createdDate ? (
              <TextFormat value={adminUserProfileEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.adminUserProfile.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {adminUserProfileEntity.lastModifiedDate ? (
              <TextFormat value={adminUserProfileEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.adminUserProfile.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{adminUserProfileEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.adminUserProfile.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{adminUserProfileEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.adminUserProfile.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{adminUserProfileEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/admin-user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin-user-profile/${adminUserProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AdminUserProfileDetail;
