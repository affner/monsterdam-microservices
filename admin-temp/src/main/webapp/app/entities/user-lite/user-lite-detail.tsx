import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-lite.reducer';

export const UserLiteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userLiteEntity = useAppSelector(state => state.admin.userLite.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userLiteDetailsHeading">
          <Translate contentKey="adminApp.userLite.detail.title">UserLite</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.id}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="adminApp.userLite.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {userLiteEntity.thumbnail ? (
              <div>
                {userLiteEntity.thumbnailContentType ? (
                  <a onClick={openFile(userLiteEntity.thumbnailContentType, userLiteEntity.thumbnail)}>
                    <img
                      src={`data:${userLiteEntity.thumbnailContentType};base64,${userLiteEntity.thumbnail}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {userLiteEntity.thumbnailContentType}, {byteSize(userLiteEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="thumbnailS3Key">
              <Translate contentKey="adminApp.userLite.thumbnailS3Key">Thumbnail S 3 Key</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.thumbnailS3Key}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="adminApp.userLite.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {userLiteEntity.birthDate ? <TextFormat value={userLiteEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="gender">
              <Translate contentKey="adminApp.userLite.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.gender}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.userLite.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userLiteEntity.createdDate ? <TextFormat value={userLiteEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.userLite.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userLiteEntity.lastModifiedDate ? (
              <TextFormat value={userLiteEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.userLite.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.userLite.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.userLite.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="nickName">
              <Translate contentKey="adminApp.userLite.nickName">Nick Name</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.nickName}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="adminApp.userLite.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.fullName}</dd>
          <dt>
            <span id="contentPreference">
              <Translate contentKey="adminApp.userLite.contentPreference">Content Preference</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.contentPreference}</dd>
        </dl>
        <Button tag={Link} to="/user-lite" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-lite/${userLiteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserLiteDetail;
