import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './single-video.reducer';

export const SingleVideoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const singleVideoEntity = useAppSelector(state => state.admin.singleVideo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="singleVideoDetailsHeading">
          <Translate contentKey="adminApp.singleVideo.detail.title">SingleVideo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.id}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="adminApp.singleVideo.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {singleVideoEntity.thumbnail ? (
              <div>
                {singleVideoEntity.thumbnailContentType ? (
                  <a onClick={openFile(singleVideoEntity.thumbnailContentType, singleVideoEntity.thumbnail)}>
                    <img
                      src={`data:${singleVideoEntity.thumbnailContentType};base64,${singleVideoEntity.thumbnail}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {singleVideoEntity.thumbnailContentType}, {byteSize(singleVideoEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="thumbnailS3Key">
              <Translate contentKey="adminApp.singleVideo.thumbnailS3Key">Thumbnail S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.thumbnailS3Key}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="adminApp.singleVideo.content">Content</Translate>
            </span>
          </dt>
          <dd>
            {singleVideoEntity.content ? (
              <div>
                {singleVideoEntity.contentContentType ? (
                  <a onClick={openFile(singleVideoEntity.contentContentType, singleVideoEntity.content)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {singleVideoEntity.contentContentType}, {byteSize(singleVideoEntity.content)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="contentS3Key">
              <Translate contentKey="adminApp.singleVideo.contentS3Key">Content S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.contentS3Key}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="adminApp.singleVideo.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {singleVideoEntity.duration ? <DurationFormat value={singleVideoEntity.duration} /> : null} ({singleVideoEntity.duration})
          </dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="adminApp.singleVideo.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.likeCount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.singleVideo.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {singleVideoEntity.createdDate ? (
              <TextFormat value={singleVideoEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.singleVideo.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {singleVideoEntity.lastModifiedDate ? (
              <TextFormat value={singleVideoEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.singleVideo.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.singleVideo.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.singleVideo.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.singleVideo.belongPackage">Belong Package</Translate>
          </dt>
          <dd>{singleVideoEntity.belongPackage ? singleVideoEntity.belongPackage.amount : ''}</dd>
        </dl>
        <Button tag={Link} to="/single-video" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/single-video/${singleVideoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SingleVideoDetail;
