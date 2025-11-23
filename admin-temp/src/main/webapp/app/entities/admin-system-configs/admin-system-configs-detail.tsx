import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './admin-system-configs.reducer';

export const AdminSystemConfigsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const adminSystemConfigsEntity = useAppSelector(state => state.admin.adminSystemConfigs.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="adminSystemConfigsDetailsHeading">
          <Translate contentKey="adminApp.adminSystemConfigs.detail.title">AdminSystemConfigs</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{adminSystemConfigsEntity.id}</dd>
          <dt>
            <span id="configKey">
              <Translate contentKey="adminApp.adminSystemConfigs.configKey">Config Key</Translate>
            </span>
          </dt>
          <dd>{adminSystemConfigsEntity.configKey}</dd>
          <dt>
            <span id="configValue">
              <Translate contentKey="adminApp.adminSystemConfigs.configValue">Config Value</Translate>
            </span>
          </dt>
          <dd>{adminSystemConfigsEntity.configValue}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="adminApp.adminSystemConfigs.description">Description</Translate>
            </span>
          </dt>
          <dd>{adminSystemConfigsEntity.description}</dd>
          <dt>
            <span id="configValueType">
              <Translate contentKey="adminApp.adminSystemConfigs.configValueType">Config Value Type</Translate>
            </span>
          </dt>
          <dd>{adminSystemConfigsEntity.configValueType}</dd>
          <dt>
            <span id="configCategory">
              <Translate contentKey="adminApp.adminSystemConfigs.configCategory">Config Category</Translate>
            </span>
          </dt>
          <dd>{adminSystemConfigsEntity.configCategory}</dd>
          <dt>
            <span id="configFile">
              <Translate contentKey="adminApp.adminSystemConfigs.configFile">Config File</Translate>
            </span>
          </dt>
          <dd>
            {adminSystemConfigsEntity.configFile ? (
              <div>
                {adminSystemConfigsEntity.configFileContentType ? (
                  <a onClick={openFile(adminSystemConfigsEntity.configFileContentType, adminSystemConfigsEntity.configFile)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {adminSystemConfigsEntity.configFileContentType}, {byteSize(adminSystemConfigsEntity.configFile)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.adminSystemConfigs.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {adminSystemConfigsEntity.createdDate ? (
              <TextFormat value={adminSystemConfigsEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.adminSystemConfigs.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {adminSystemConfigsEntity.lastModifiedDate ? (
              <TextFormat value={adminSystemConfigsEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.adminSystemConfigs.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{adminSystemConfigsEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.adminSystemConfigs.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{adminSystemConfigsEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="adminApp.adminSystemConfigs.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{adminSystemConfigsEntity.isActive ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/admin-system-configs" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin-system-configs/${adminSystemConfigsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AdminSystemConfigsDetail;
