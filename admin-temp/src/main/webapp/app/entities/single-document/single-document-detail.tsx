import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './single-document.reducer';

export const SingleDocumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const singleDocumentEntity = useAppSelector(state => state.admin.singleDocument.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="singleDocumentDetailsHeading">
          <Translate contentKey="adminApp.singleDocument.detail.title">SingleDocument</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{singleDocumentEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="adminApp.singleDocument.title">Title</Translate>
            </span>
          </dt>
          <dd>{singleDocumentEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.singleDocument.description">Description</Translate>
            </span>
          </dt>
          <dd>{singleDocumentEntity.description}</dd>
          <dt>
            <span id="documentFile">
              <Translate contentKey="adminApp.singleDocument.documentFile">Document File</Translate>
            </span>
          </dt>
          <dd>
            {singleDocumentEntity.documentFile ? (
              <div>
                {singleDocumentEntity.documentFileContentType ? (
                  <a onClick={openFile(singleDocumentEntity.documentFileContentType, singleDocumentEntity.documentFile)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {singleDocumentEntity.documentFileContentType}, {byteSize(singleDocumentEntity.documentFile)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="documentFileS3Key">
              <Translate contentKey="adminApp.singleDocument.documentFileS3Key">Document File S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleDocumentEntity.documentFileS3Key}</dd>
          <dt>
            <span id="documentType">
              <Translate contentKey="adminApp.singleDocument.documentType">Document Type</Translate>
            </span>
          </dt>
          <dd>{singleDocumentEntity.documentType}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.singleDocument.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {singleDocumentEntity.createdDate ? (
              <TextFormat value={singleDocumentEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.singleDocument.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {singleDocumentEntity.lastModifiedDate ? (
              <TextFormat value={singleDocumentEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.singleDocument.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{singleDocumentEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.singleDocument.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{singleDocumentEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.singleDocument.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{singleDocumentEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.singleDocument.user">User</Translate>
          </dt>
          <dd>{singleDocumentEntity.user ? singleDocumentEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/single-document" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/single-document/${singleDocumentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SingleDocumentDetail;
