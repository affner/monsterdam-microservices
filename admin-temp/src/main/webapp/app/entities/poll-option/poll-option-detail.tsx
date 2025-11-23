import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './poll-option.reducer';

export const PollOptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pollOptionEntity = useAppSelector(state => state.admin.pollOption.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pollOptionDetailsHeading">
          <Translate contentKey="adminApp.pollOption.detail.title">PollOption</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pollOptionEntity.id}</dd>
          <dt>
            <span id="optionDescription">
              <Translate contentKey="adminApp.pollOption.optionDescription">Option Description</Translate>
            </span>
          </dt>
          <dd>{pollOptionEntity.optionDescription}</dd>
          <dt>
            <span id="voteCount">
              <Translate contentKey="adminApp.pollOption.voteCount">Vote Count</Translate>
            </span>
          </dt>
          <dd>{pollOptionEntity.voteCount}</dd>
          <dt>
            <Translate contentKey="adminApp.pollOption.poll">Poll</Translate>
          </dt>
          <dd>{pollOptionEntity.poll ? pollOptionEntity.poll.question : ''}</dd>
        </dl>
        <Button tag={Link} to="/poll-option" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/poll-option/${pollOptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PollOptionDetail;
