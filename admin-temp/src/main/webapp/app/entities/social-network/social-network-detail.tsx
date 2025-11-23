import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './social-network.reducer';

export const SocialNetworkDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const socialNetworkEntity = useAppSelector(state => state.admin.socialNetwork.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="socialNetworkDetailsHeading">
          <Translate contentKey="adminApp.socialNetwork.detail.title">SocialNetwork</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{socialNetworkEntity.id}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="adminApp.socialNetwork.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {socialNetworkEntity.thumbnail ? (
              <div>
                {socialNetworkEntity.thumbnailContentType ? (
                  <a onClick={openFile(socialNetworkEntity.thumbnailContentType, socialNetworkEntity.thumbnail)}>
                    <img
                      src={`data:${socialNetworkEntity.thumbnailContentType};base64,${socialNetworkEntity.thumbnail}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {socialNetworkEntity.thumbnailContentType}, {byteSize(socialNetworkEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="name">
              <Translate contentKey="adminApp.socialNetwork.name">Name</Translate>
            </span>
          </dt>
          <dd>{socialNetworkEntity.name}</dd>
          <dt>
            <span id="completeName">
              <Translate contentKey="adminApp.socialNetwork.completeName">Complete Name</Translate>
            </span>
          </dt>
          <dd>{socialNetworkEntity.completeName}</dd>
          <dt>
            <span id="mainLink">
              <Translate contentKey="adminApp.socialNetwork.mainLink">Main Link</Translate>
            </span>
          </dt>
          <dd>{socialNetworkEntity.mainLink}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.socialNetwork.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {socialNetworkEntity.createdDate ? (
              <TextFormat value={socialNetworkEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.socialNetwork.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {socialNetworkEntity.lastModifiedDate ? (
              <TextFormat value={socialNetworkEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.socialNetwork.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{socialNetworkEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.socialNetwork.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{socialNetworkEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.socialNetwork.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{socialNetworkEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/social-network" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/social-network/${socialNetworkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SocialNetworkDetail;
