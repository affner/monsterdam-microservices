import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './assistance-ticket.reducer';

export const AssistanceTicketDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const assistanceTicketEntity = useAppSelector(state => state.admin.assistanceTicket.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="assistanceTicketDetailsHeading">
          <Translate contentKey="adminApp.assistanceTicket.detail.title">AssistanceTicket</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{assistanceTicketEntity.id}</dd>
          <dt>
            <span id="subject">
              <Translate contentKey="adminApp.assistanceTicket.subject">Subject</Translate>
            </span>
          </dt>
          <dd>{assistanceTicketEntity.subject}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.assistanceTicket.description">Description</Translate>
            </span>
          </dt>
          <dd>{assistanceTicketEntity.description}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="adminApp.assistanceTicket.status">Status</Translate>
            </span>
          </dt>
          <dd>{assistanceTicketEntity.status}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="adminApp.assistanceTicket.type">Type</Translate>
            </span>
          </dt>
          <dd>{assistanceTicketEntity.type}</dd>
          <dt>
            <span id="openedAt">
              <Translate contentKey="adminApp.assistanceTicket.openedAt">Opened At</Translate>
            </span>
          </dt>
          <dd>
            {assistanceTicketEntity.openedAt ? (
              <TextFormat value={assistanceTicketEntity.openedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="closedAt">
              <Translate contentKey="adminApp.assistanceTicket.closedAt">Closed At</Translate>
            </span>
          </dt>
          <dd>
            {assistanceTicketEntity.closedAt ? (
              <TextFormat value={assistanceTicketEntity.closedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="comments">
              <Translate contentKey="adminApp.assistanceTicket.comments">Comments</Translate>
            </span>
          </dt>
          <dd>{assistanceTicketEntity.comments}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.assistanceTicket.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {assistanceTicketEntity.createdDate ? (
              <TextFormat value={assistanceTicketEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.assistanceTicket.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {assistanceTicketEntity.lastModifiedDate ? (
              <TextFormat value={assistanceTicketEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.assistanceTicket.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{assistanceTicketEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.assistanceTicket.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{assistanceTicketEntity.lastModifiedBy}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="adminApp.assistanceTicket.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{assistanceTicketEntity.userId}</dd>
          <dt>
            <Translate contentKey="adminApp.assistanceTicket.moderationAction">Moderation Action</Translate>
          </dt>
          <dd>{assistanceTicketEntity.moderationAction ? assistanceTicketEntity.moderationAction.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.assistanceTicket.assignedAdmin">Assigned Admin</Translate>
          </dt>
          <dd>{assistanceTicketEntity.assignedAdmin ? assistanceTicketEntity.assignedAdmin.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/assistance-ticket" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/assistance-ticket/${assistanceTicketEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssistanceTicketDetail;
