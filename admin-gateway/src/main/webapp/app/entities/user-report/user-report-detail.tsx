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
            <span id="reporterId">
              <Translate contentKey="adminApp.userReport.reporterId">Reporter Id</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.reporterId}</dd>
          <dt>
            <span id="reportedId">
              <Translate contentKey="adminApp.userReport.reportedId">Reported Id</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.reportedId}</dd>
          <dt>
            <span id="multimediaId">
              <Translate contentKey="adminApp.userReport.multimediaId">Multimedia Id</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.multimediaId}</dd>
          <dt>
            <span id="messageId">
              <Translate contentKey="adminApp.userReport.messageId">Message Id</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.messageId}</dd>
          <dt>
            <span id="postId">
              <Translate contentKey="adminApp.userReport.postId">Post Id</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.postId}</dd>
          <dt>
            <span id="commentId">
              <Translate contentKey="adminApp.userReport.commentId">Comment Id</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.commentId}</dd>
          <dt>
            <Translate contentKey="adminApp.userReport.ticket">Ticket</Translate>
          </dt>
          <dd>{userReportEntity.ticket ? userReportEntity.ticket.id : ''}</dd>
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
