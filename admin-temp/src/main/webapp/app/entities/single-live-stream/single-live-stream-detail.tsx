import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './single-live-stream.reducer';

export const SingleLiveStreamDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const singleLiveStreamEntity = useAppSelector(state => state.admin.singleLiveStream.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="singleLiveStreamDetailsHeading">
          <Translate contentKey="adminApp.singleLiveStream.detail.title">SingleLiveStream</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{singleLiveStreamEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="adminApp.singleLiveStream.title">Title</Translate>
            </span>
          </dt>
          <dd>{singleLiveStreamEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.singleLiveStream.description">Description</Translate>
            </span>
          </dt>
          <dd>{singleLiveStreamEntity.description}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="adminApp.singleLiveStream.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {singleLiveStreamEntity.thumbnail ? (
              <div>
                {singleLiveStreamEntity.thumbnailContentType ? (
                  <a onClick={openFile(singleLiveStreamEntity.thumbnailContentType, singleLiveStreamEntity.thumbnail)}>
                    <img
                      src={`data:${singleLiveStreamEntity.thumbnailContentType};base64,${singleLiveStreamEntity.thumbnail}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {singleLiveStreamEntity.thumbnailContentType}, {byteSize(singleLiveStreamEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="thumbnailS3Key">
              <Translate contentKey="adminApp.singleLiveStream.thumbnailS3Key">Thumbnail S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleLiveStreamEntity.thumbnailS3Key}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="adminApp.singleLiveStream.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>
            {singleLiveStreamEntity.startTime ? (
              <TextFormat value={singleLiveStreamEntity.startTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="adminApp.singleLiveStream.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>
            {singleLiveStreamEntity.endTime ? (
              <TextFormat value={singleLiveStreamEntity.endTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="liveContent">
              <Translate contentKey="adminApp.singleLiveStream.liveContent">Live Content</Translate>
            </span>
          </dt>
          <dd>
            {singleLiveStreamEntity.liveContent ? (
              <div>
                {singleLiveStreamEntity.liveContentContentType ? (
                  <a onClick={openFile(singleLiveStreamEntity.liveContentContentType, singleLiveStreamEntity.liveContent)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {singleLiveStreamEntity.liveContentContentType}, {byteSize(singleLiveStreamEntity.liveContent)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="liveContentS3Key">
              <Translate contentKey="adminApp.singleLiveStream.liveContentS3Key">Live Content S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleLiveStreamEntity.liveContentS3Key}</dd>
          <dt>
            <span id="isRecorded">
              <Translate contentKey="adminApp.singleLiveStream.isRecorded">Is Recorded</Translate>
            </span>
          </dt>
          <dd>{singleLiveStreamEntity.isRecorded ? 'true' : 'false'}</dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="adminApp.singleLiveStream.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{singleLiveStreamEntity.likeCount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.singleLiveStream.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {singleLiveStreamEntity.createdDate ? (
              <TextFormat value={singleLiveStreamEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.singleLiveStream.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {singleLiveStreamEntity.lastModifiedDate ? (
              <TextFormat value={singleLiveStreamEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.singleLiveStream.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{singleLiveStreamEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.singleLiveStream.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{singleLiveStreamEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.singleLiveStream.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{singleLiveStreamEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/single-live-stream" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/single-live-stream/${singleLiveStreamEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SingleLiveStreamDetail;
