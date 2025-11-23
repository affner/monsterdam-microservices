import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './content-package.reducer';

export const ContentPackageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contentPackageEntity = useAppSelector(state => state.admin.contentPackage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contentPackageDetailsHeading">
          <Translate contentKey="adminApp.contentPackage.detail.title">ContentPackage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="adminApp.contentPackage.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.amount}</dd>
          <dt>
            <span id="videoCount">
              <Translate contentKey="adminApp.contentPackage.videoCount">Video Count</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.videoCount}</dd>
          <dt>
            <span id="imageCount">
              <Translate contentKey="adminApp.contentPackage.imageCount">Image Count</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.imageCount}</dd>
          <dt>
            <span id="isPaidContent">
              <Translate contentKey="adminApp.contentPackage.isPaidContent">Is Paid Content</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.isPaidContent ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.contentPackage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {contentPackageEntity.createdDate ? (
              <TextFormat value={contentPackageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.contentPackage.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {contentPackageEntity.lastModifiedDate ? (
              <TextFormat value={contentPackageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.contentPackage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.contentPackage.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.contentPackage.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{contentPackageEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.contentPackage.audio">Audio</Translate>
          </dt>
          <dd>{contentPackageEntity.audio ? contentPackageEntity.audio.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.contentPackage.usersTagged">Users Tagged</Translate>
          </dt>
          <dd>
            {contentPackageEntity.usersTaggeds
              ? contentPackageEntity.usersTaggeds.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {contentPackageEntity.usersTaggeds && i === contentPackageEntity.usersTaggeds.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/content-package" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/content-package/${contentPackageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContentPackageDetail;
