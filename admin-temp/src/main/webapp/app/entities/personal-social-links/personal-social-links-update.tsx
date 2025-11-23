import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISocialNetwork } from 'app/shared/model/social-network.model';
import { getEntities as getSocialNetworks } from 'app/entities/social-network/social-network.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IPersonalSocialLinks } from 'app/shared/model/personal-social-links.model';
import { getEntity, updateEntity, createEntity, reset } from './personal-social-links.reducer';

export const PersonalSocialLinksUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const socialNetworks = useAppSelector(state => state.admin.socialNetwork.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const personalSocialLinksEntity = useAppSelector(state => state.admin.personalSocialLinks.entity);
  const loading = useAppSelector(state => state.admin.personalSocialLinks.loading);
  const updating = useAppSelector(state => state.admin.personalSocialLinks.updating);
  const updateSuccess = useAppSelector(state => state.admin.personalSocialLinks.updateSuccess);

  const handleClose = () => {
    navigate('/personal-social-links' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSocialNetworks({}));
    dispatch(getUserProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...personalSocialLinksEntity,
      ...values,
      socialNetwork: socialNetworks.find(it => it.id.toString() === values.socialNetwork.toString()),
      user: userProfiles.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...personalSocialLinksEntity,
          createdDate: convertDateTimeFromServer(personalSocialLinksEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(personalSocialLinksEntity.lastModifiedDate),
          socialNetwork: personalSocialLinksEntity?.socialNetwork?.id,
          user: personalSocialLinksEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.personalSocialLinks.home.createOrEditLabel" data-cy="PersonalSocialLinksCreateUpdateHeading">
            <Translate contentKey="adminApp.personalSocialLinks.home.createOrEditLabel">Create or edit a PersonalSocialLinks</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="personal-social-links-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedBlobField
                label={translate('adminApp.personalSocialLinks.thumbnail')}
                id="personal-social-links-thumbnail"
                name="thumbnail"
                data-cy="thumbnail"
                isImage
                accept="image/*"
              />
              <ValidatedBlobField
                label={translate('adminApp.personalSocialLinks.normalImage')}
                id="personal-social-links-normalImage"
                name="normalImage"
                data-cy="normalImage"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('adminApp.personalSocialLinks.normalImageS3Key')}
                id="personal-social-links-normalImageS3Key"
                name="normalImageS3Key"
                data-cy="normalImageS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.personalSocialLinks.thumbnailIconS3Key')}
                id="personal-social-links-thumbnailIconS3Key"
                name="thumbnailIconS3Key"
                data-cy="thumbnailIconS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.personalSocialLinks.socialLink')}
                id="personal-social-links-socialLink"
                name="socialLink"
                data-cy="socialLink"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.personalSocialLinks.createdDate')}
                id="personal-social-links-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.personalSocialLinks.lastModifiedDate')}
                id="personal-social-links-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.personalSocialLinks.createdBy')}
                id="personal-social-links-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.personalSocialLinks.lastModifiedBy')}
                id="personal-social-links-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.personalSocialLinks.isDeleted')}
                id="personal-social-links-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="personal-social-links-socialNetwork"
                name="socialNetwork"
                data-cy="socialNetwork"
                label={translate('adminApp.personalSocialLinks.socialNetwork')}
                type="select"
              >
                <option value="" key="0" />
                {socialNetworks
                  ? socialNetworks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.thumbnail}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="personal-social-links-user"
                name="user"
                data-cy="user"
                label={translate('adminApp.personalSocialLinks.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/personal-social-links" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PersonalSocialLinksUpdate;
