import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './poll-vote.reducer';

export const PollVoteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pollVoteEntity = useAppSelector(state => state.admin.pollVote.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pollVoteDetailsHeading">
          <Translate contentKey="adminApp.pollVote.detail.title">PollVote</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pollVoteEntity.id}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.pollVote.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {pollVoteEntity.createdDate ? <TextFormat value={pollVoteEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.pollVote.pollOption">Poll Option</Translate>
          </dt>
          <dd>{pollVoteEntity.pollOption ? pollVoteEntity.pollOption.optionDescription : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.pollVote.votingUser">Voting User</Translate>
          </dt>
          <dd>{pollVoteEntity.votingUser ? pollVoteEntity.votingUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/poll-vote" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/poll-vote/${pollVoteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PollVoteDetail;
