import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './help-question.reducer';

export const HelpQuestionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const helpQuestionEntity = useAppSelector(state => state.admin.helpQuestion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="helpQuestionDetailsHeading">
          <Translate contentKey="adminApp.helpQuestion.detail.title">HelpQuestion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{helpQuestionEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="adminApp.helpQuestion.title">Title</Translate>
            </span>
          </dt>
          <dd>{helpQuestionEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="adminApp.helpQuestion.content">Content</Translate>
            </span>
          </dt>
          <dd>{helpQuestionEntity.content}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.helpQuestion.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{helpQuestionEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.helpQuestion.question">Question</Translate>
          </dt>
          <dd>
            {helpQuestionEntity.questions
              ? helpQuestionEntity.questions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {helpQuestionEntity.questions && i === helpQuestionEntity.questions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.helpQuestion.subcategory">Subcategory</Translate>
          </dt>
          <dd>{helpQuestionEntity.subcategory ? helpQuestionEntity.subcategory.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/help-question" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/help-question/${helpQuestionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HelpQuestionDetail;
