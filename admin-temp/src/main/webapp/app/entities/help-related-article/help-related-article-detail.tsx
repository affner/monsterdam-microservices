import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './help-related-article.reducer';

export const HelpRelatedArticleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const helpRelatedArticleEntity = useAppSelector(state => state.admin.helpRelatedArticle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="helpRelatedArticleDetailsHeading">
          <Translate contentKey="adminApp.helpRelatedArticle.detail.title">HelpRelatedArticle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{helpRelatedArticleEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="adminApp.helpRelatedArticle.title">Title</Translate>
            </span>
          </dt>
          <dd>{helpRelatedArticleEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="adminApp.helpRelatedArticle.content">Content</Translate>
            </span>
          </dt>
          <dd>{helpRelatedArticleEntity.content}</dd>
        </dl>
        <Button tag={Link} to="/help-related-article" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/help-related-article/${helpRelatedArticleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HelpRelatedArticleDetail;
