import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './single-photo.reducer';

export const SinglePhotoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const singlePhotoEntity = useAppSelector(state => state.admin.singlePhoto.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="singlePhotoDetailsHeading">
          <Translate contentKey="adminApp.singlePhoto.detail.title">SinglePhoto</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.id}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="adminApp.singlePhoto.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {singlePhotoEntity.thumbnail ? (
              <div>
                {singlePhotoEntity.thumbnailContentType ? (
                  <a onClick={openFile(singlePhotoEntity.thumbnailContentType, singlePhotoEntity.thumbnail)}>
                    <img
                      src={`data:${singlePhotoEntity.thumbnailContentType};base64,${singlePhotoEntity.thumbnail}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {singlePhotoEntity.thumbnailContentType}, {byteSize(singlePhotoEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="thumbnailS3Key">
              <Translate contentKey="adminApp.singlePhoto.thumbnailS3Key">Thumbnail S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.thumbnailS3Key}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="adminApp.singlePhoto.content">Content</Translate>
            </span>
          </dt>
          <dd>
            {singlePhotoEntity.content ? (
              <div>
                {singlePhotoEntity.contentContentType ? (
                  <a onClick={openFile(singlePhotoEntity.contentContentType, singlePhotoEntity.content)}>
                    <img
                      src={`data:${singlePhotoEntity.contentContentType};base64,${singlePhotoEntity.content}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {singlePhotoEntity.contentContentType}, {byteSize(singlePhotoEntity.content)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="contentS3Key">
              <Translate contentKey="adminApp.singlePhoto.contentS3Key">Content S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.contentS3Key}</dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="adminApp.singlePhoto.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.likeCount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.singlePhoto.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {singlePhotoEntity.createdDate ? (
              <TextFormat value={singlePhotoEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.singlePhoto.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {singlePhotoEntity.lastModifiedDate ? (
              <TextFormat value={singlePhotoEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.singlePhoto.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.singlePhoto.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.singlePhoto.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.singlePhoto.belongPackage">Belong Package</Translate>
          </dt>
          <dd>{singlePhotoEntity.belongPackage ? singlePhotoEntity.belongPackage.amount : ''}</dd>
        </dl>
        <Button tag={Link} to="/single-photo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/single-photo/${singlePhotoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SinglePhotoDetail;
