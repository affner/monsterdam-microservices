import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './like-mark.reducer';

export const LikeMarkDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const likeMarkEntity = useAppSelector(state => state.admin.likeMark.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="likeMarkDetailsHeading">
          <Translate contentKey="adminApp.likeMark.detail.title">LikeMark</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{likeMarkEntity.id}</dd>
          <dt>
            <span id="emojiTypeId">
              <Translate contentKey="adminApp.likeMark.emojiTypeId">Emoji Type Id</Translate>
            </span>
          </dt>
          <dd>{likeMarkEntity.emojiTypeId}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.likeMark.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {likeMarkEntity.createdDate ? <TextFormat value={likeMarkEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.likeMark.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {likeMarkEntity.lastModifiedDate ? (
              <TextFormat value={likeMarkEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.likeMark.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{likeMarkEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.likeMark.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{likeMarkEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.likeMark.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{likeMarkEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="multimediaId">
              <Translate contentKey="adminApp.likeMark.multimediaId">Multimedia Id</Translate>
            </span>
          </dt>
          <dd>{likeMarkEntity.multimediaId}</dd>
          <dt>
            <span id="messageId">
              <Translate contentKey="adminApp.likeMark.messageId">Message Id</Translate>
            </span>
          </dt>
          <dd>{likeMarkEntity.messageId}</dd>
          <dt>
            <span id="postId">
              <Translate contentKey="adminApp.likeMark.postId">Post Id</Translate>
            </span>
          </dt>
          <dd>{likeMarkEntity.postId}</dd>
          <dt>
            <span id="commentId">
              <Translate contentKey="adminApp.likeMark.commentId">Comment Id</Translate>
            </span>
          </dt>
          <dd>{likeMarkEntity.commentId}</dd>
          <dt>
            <span id="likerUserId">
              <Translate contentKey="adminApp.likeMark.likerUserId">Liker User Id</Translate>
            </span>
          </dt>
          <dd>{likeMarkEntity.likerUserId}</dd>
        </dl>
        <Button tag={Link} to="/like-mark" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/like-mark/${likeMarkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LikeMarkDetail;
