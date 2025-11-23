import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './book-mark.reducer';

export const BookMarkDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookMarkEntity = useAppSelector(state => state.admin.bookMark.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookMarkDetailsHeading">
          <Translate contentKey="adminApp.bookMark.detail.title">BookMark</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookMarkEntity.id}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.bookMark.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {bookMarkEntity.createdDate ? <TextFormat value={bookMarkEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.bookMark.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {bookMarkEntity.lastModifiedDate ? (
              <TextFormat value={bookMarkEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.bookMark.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{bookMarkEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.bookMark.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{bookMarkEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.bookMark.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{bookMarkEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.bookMark.post">Post</Translate>
          </dt>
          <dd>{bookMarkEntity.post ? bookMarkEntity.post.postContent : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.bookMark.message">Message</Translate>
          </dt>
          <dd>{bookMarkEntity.message ? bookMarkEntity.message.messageContent : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.bookMark.user">User</Translate>
          </dt>
          <dd>{bookMarkEntity.user ? bookMarkEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/book-mark" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/book-mark/${bookMarkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookMarkDetail;
