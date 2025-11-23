import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './personal-social-links.reducer';

export const PersonalSocialLinksDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const personalSocialLinksEntity = useAppSelector(state => state.admin.personalSocialLinks.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="personalSocialLinksDetailsHeading">
          <Translate contentKey="adminApp.personalSocialLinks.detail.title">PersonalSocialLinks</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.id}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="adminApp.personalSocialLinks.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {personalSocialLinksEntity.thumbnail ? (
              <div>
                {personalSocialLinksEntity.thumbnailContentType ? (
                  <a onClick={openFile(personalSocialLinksEntity.thumbnailContentType, personalSocialLinksEntity.thumbnail)}>
                    <img
                      src={`data:${personalSocialLinksEntity.thumbnailContentType};base64,${personalSocialLinksEntity.thumbnail}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {personalSocialLinksEntity.thumbnailContentType}, {byteSize(personalSocialLinksEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="normalImage">
              <Translate contentKey="adminApp.personalSocialLinks.normalImage">Normal Image</Translate>
            </span>
          </dt>
          <dd>
            {personalSocialLinksEntity.normalImage ? (
              <div>
                {personalSocialLinksEntity.normalImageContentType ? (
                  <a onClick={openFile(personalSocialLinksEntity.normalImageContentType, personalSocialLinksEntity.normalImage)}>
                    <img
                      src={`data:${personalSocialLinksEntity.normalImageContentType};base64,${personalSocialLinksEntity.normalImage}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {personalSocialLinksEntity.normalImageContentType}, {byteSize(personalSocialLinksEntity.normalImage)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="normalImageS3Key">
              <Translate contentKey="adminApp.personalSocialLinks.normalImageS3Key">Normal Image S 3 Key</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.normalImageS3Key}</dd>
          <dt>
            <span id="thumbnailIconS3Key">
              <Translate contentKey="adminApp.personalSocialLinks.thumbnailIconS3Key">Thumbnail Icon S 3 Key</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.thumbnailIconS3Key}</dd>
          <dt>
            <span id="socialLink">
              <Translate contentKey="adminApp.personalSocialLinks.socialLink">Social Link</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.socialLink}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.personalSocialLinks.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {personalSocialLinksEntity.createdDate ? (
              <TextFormat value={personalSocialLinksEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.personalSocialLinks.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {personalSocialLinksEntity.lastModifiedDate ? (
              <TextFormat value={personalSocialLinksEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.personalSocialLinks.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.personalSocialLinks.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.personalSocialLinks.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.personalSocialLinks.socialNetwork">Social Network</Translate>
          </dt>
          <dd>{personalSocialLinksEntity.socialNetwork ? personalSocialLinksEntity.socialNetwork.thumbnail : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.personalSocialLinks.user">User</Translate>
          </dt>
          <dd>{personalSocialLinksEntity.user ? personalSocialLinksEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/personal-social-links" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/personal-social-links/${personalSocialLinksEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PersonalSocialLinksDetail;
