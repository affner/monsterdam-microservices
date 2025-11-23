import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-report.reducer';

export const UserReportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userReportEntity = useAppSelector(state => state.admin.userReport.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userReportDetailsHeading">
          <Translate contentKey="adminApp.userReport.detail.title">UserReport</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.id}</dd>
          <dt>
            <span id="reportDescription">
              <Translate contentKey="adminApp.userReport.reportDescription">Report Description</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.reportDescription}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="adminApp.userReport.status">Status</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.status}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.userReport.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userReportEntity.createdDate ? <TextFormat value={userReportEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.userReport.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userReportEntity.lastModifiedDate ? (
              <TextFormat value={userReportEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.userReport.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.userReport.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.userReport.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="reportCategory">
              <Translate contentKey="adminApp.userReport.reportCategory">Report Category</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.reportCategory}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.ticket">Ticket</Translate>
          </dt>
          <dd>{userReportEntity.ticket ? userReportEntity.ticket.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.reporter">Reporter</Translate>
          </dt>
          <dd>{userReportEntity.reporter ? userReportEntity.reporter.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.reported">Reported</Translate>
          </dt>
          <dd>{userReportEntity.reported ? userReportEntity.reported.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.story">Story</Translate>
          </dt>
          <dd>{userReportEntity.story ? userReportEntity.story.duration : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.video">Video</Translate>
          </dt>
          <dd>{userReportEntity.video ? userReportEntity.video.duration : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.photo">Photo</Translate>
          </dt>
          <dd>{userReportEntity.photo ? userReportEntity.photo.thumbnail : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.audio">Audio</Translate>
          </dt>
          <dd>{userReportEntity.audio ? userReportEntity.audio.duration : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.liveStream">Live Stream</Translate>
          </dt>
          <dd>{userReportEntity.liveStream ? userReportEntity.liveStream.thumbnail : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.message">Message</Translate>
          </dt>
          <dd>{userReportEntity.message ? userReportEntity.message.messageContent : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.post">Post</Translate>
          </dt>
          <dd>{userReportEntity.post ? userReportEntity.post.postContent : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.postComment">Post Comment</Translate>
          </dt>
          <dd>{userReportEntity.postComment ? userReportEntity.postComment.commentContent : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-report" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-report/${userReportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserReportDetail;
