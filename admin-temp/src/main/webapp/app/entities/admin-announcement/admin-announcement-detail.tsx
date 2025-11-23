import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './admin-announcement.reducer';

export const AdminAnnouncementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const adminAnnouncementEntity = useAppSelector(state => state.admin.adminAnnouncement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="adminAnnouncementDetailsHeading">
          <Translate contentKey="adminApp.adminAnnouncement.detail.title">AdminAnnouncement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{adminAnnouncementEntity.id}</dd>
          <dt>
            <span id="announcementType">
              <Translate contentKey="adminApp.adminAnnouncement.announcementType">Announcement Type</Translate>
            </span>
          </dt>
          <dd>{adminAnnouncementEntity.announcementType}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="adminApp.adminAnnouncement.title">Title</Translate>
            </span>
          </dt>
          <dd>{adminAnnouncementEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="adminApp.adminAnnouncement.content">Content</Translate>
            </span>
          </dt>
          <dd>{adminAnnouncementEntity.content}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.adminAnnouncement.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {adminAnnouncementEntity.createdDate ? (
              <TextFormat value={adminAnnouncementEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.adminAnnouncement.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {adminAnnouncementEntity.lastModifiedDate ? (
              <TextFormat value={adminAnnouncementEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.adminAnnouncement.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{adminAnnouncementEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.adminAnnouncement.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{adminAnnouncementEntity.lastModifiedBy}</dd>
          <dt>
            <Translate contentKey="adminApp.adminAnnouncement.announcerMessage">Announcer Message</Translate>
          </dt>
          <dd>{adminAnnouncementEntity.announcerMessage ? adminAnnouncementEntity.announcerMessage.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.adminAnnouncement.admin">Admin</Translate>
          </dt>
          <dd>{adminAnnouncementEntity.admin ? adminAnnouncementEntity.admin.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/admin-announcement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin-announcement/${adminAnnouncementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AdminAnnouncementDetail;
