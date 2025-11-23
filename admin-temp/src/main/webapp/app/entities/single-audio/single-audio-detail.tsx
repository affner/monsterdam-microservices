import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './single-audio.reducer';

export const SingleAudioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const singleAudioEntity = useAppSelector(state => state.admin.singleAudio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="singleAudioDetailsHeading">
          <Translate contentKey="adminApp.singleAudio.detail.title">SingleAudio</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.id}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="adminApp.singleAudio.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.thumbnail ? (
              <div>
                {singleAudioEntity.thumbnailContentType ? (
                  <a onClick={openFile(singleAudioEntity.thumbnailContentType, singleAudioEntity.thumbnail)}>
                    <img
                      src={`data:${singleAudioEntity.thumbnailContentType};base64,${singleAudioEntity.thumbnail}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {singleAudioEntity.thumbnailContentType}, {byteSize(singleAudioEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="thumbnailS3Key">
              <Translate contentKey="adminApp.singleAudio.thumbnailS3Key">Thumbnail S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.thumbnailS3Key}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="adminApp.singleAudio.content">Content</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.content ? (
              <div>
                {singleAudioEntity.contentContentType ? (
                  <a onClick={openFile(singleAudioEntity.contentContentType, singleAudioEntity.content)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {singleAudioEntity.contentContentType}, {byteSize(singleAudioEntity.content)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="contentS3Key">
              <Translate contentKey="adminApp.singleAudio.contentS3Key">Content S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.contentS3Key}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="adminApp.singleAudio.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.duration ? <DurationFormat value={singleAudioEntity.duration} /> : null} ({singleAudioEntity.duration})
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.singleAudio.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.createdDate ? (
              <TextFormat value={singleAudioEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.singleAudio.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.lastModifiedDate ? (
              <TextFormat value={singleAudioEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.singleAudio.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.singleAudio.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.singleAudio.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/single-audio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/single-audio/${singleAudioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SingleAudioDetail;
