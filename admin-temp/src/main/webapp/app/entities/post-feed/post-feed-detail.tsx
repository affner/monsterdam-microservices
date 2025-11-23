import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post-feed.reducer';

export const PostFeedDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postFeedEntity = useAppSelector(state => state.admin.postFeed.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postFeedDetailsHeading">
          <Translate contentKey="adminApp.postFeed.detail.title">PostFeed</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.id}</dd>
          <dt>
            <span id="postContent">
              <Translate contentKey="adminApp.postFeed.postContent">Post Content</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.postContent}</dd>
          <dt>
            <span id="isHidden">
              <Translate contentKey="adminApp.postFeed.isHidden">Is Hidden</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.isHidden ? 'true' : 'false'}</dd>
          <dt>
            <span id="pinnedPost">
              <Translate contentKey="adminApp.postFeed.pinnedPost">Pinned Post</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.pinnedPost ? 'true' : 'false'}</dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="adminApp.postFeed.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.likeCount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.postFeed.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {postFeedEntity.createdDate ? <TextFormat value={postFeedEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.postFeed.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {postFeedEntity.lastModifiedDate ? (
              <TextFormat value={postFeedEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.postFeed.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.postFeed.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.postFeed.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.postFeed.poll">Poll</Translate>
          </dt>
          <dd>{postFeedEntity.poll ? postFeedEntity.poll.question : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.postFeed.contentPackage">Content Package</Translate>
          </dt>
          <dd>{postFeedEntity.contentPackage ? postFeedEntity.contentPackage.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.postFeed.hashTags">Hash Tags</Translate>
          </dt>
          <dd>
            {postFeedEntity.hashTags
              ? postFeedEntity.hashTags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {postFeedEntity.hashTags && i === postFeedEntity.hashTags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.postFeed.creator">Creator</Translate>
          </dt>
          <dd>{postFeedEntity.creator ? postFeedEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/post-feed" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post-feed/${postFeedEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostFeedDetail;
