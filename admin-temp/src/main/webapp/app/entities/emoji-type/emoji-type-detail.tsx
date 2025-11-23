import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './emoji-type.reducer';

export const EmojiTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const emojiTypeEntity = useAppSelector(state => state.admin.emojiType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="emojiTypeDetailsHeading">
          <Translate contentKey="adminApp.emojiType.detail.title">EmojiType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{emojiTypeEntity.id}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="adminApp.emojiType.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {emojiTypeEntity.thumbnail ? (
              <div>
                {emojiTypeEntity.thumbnailContentType ? (
                  <a onClick={openFile(emojiTypeEntity.thumbnailContentType, emojiTypeEntity.thumbnail)}>
                    <img
                      src={`data:${emojiTypeEntity.thumbnailContentType};base64,${emojiTypeEntity.thumbnail}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {emojiTypeEntity.thumbnailContentType}, {byteSize(emojiTypeEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.emojiType.description">Description</Translate>
            </span>
          </dt>
          <dd>{emojiTypeEntity.description}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.emojiType.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {emojiTypeEntity.createdDate ? <TextFormat value={emojiTypeEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.emojiType.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {emojiTypeEntity.lastModifiedDate ? (
              <TextFormat value={emojiTypeEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.emojiType.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{emojiTypeEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.emojiType.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{emojiTypeEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.emojiType.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{emojiTypeEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/emoji-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/emoji-type/${emojiTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmojiTypeDetail;
