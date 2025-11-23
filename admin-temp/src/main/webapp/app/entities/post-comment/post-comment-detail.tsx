import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post-comment.reducer';

export const PostCommentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postCommentEntity = useAppSelector(state => state.admin.postComment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postCommentDetailsHeading">
          <Translate contentKey="adminApp.postComment.detail.title">PostComment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.id}</dd>
          <dt>
            <span id="commentContent">
              <Translate contentKey="adminApp.postComment.commentContent">Comment Content</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.commentContent}</dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="adminApp.postComment.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.likeCount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.postComment.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {postCommentEntity.createdDate ? (
              <TextFormat value={postCommentEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.postComment.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {postCommentEntity.lastModifiedDate ? (
              <TextFormat value={postCommentEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.postComment.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.postComment.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.postComment.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.postComment.post">Post</Translate>
          </dt>
          <dd>{postCommentEntity.post ? postCommentEntity.post.postContent : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.postComment.responseTo">Response To</Translate>
          </dt>
          <dd>{postCommentEntity.responseTo ? postCommentEntity.responseTo.commentContent : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.postComment.commenter">Commenter</Translate>
          </dt>
          <dd>{postCommentEntity.commenter ? postCommentEntity.commenter.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/post-comment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post-comment/${postCommentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostCommentDetail;
