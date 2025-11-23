import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { getEntities as getDirectMessages } from 'app/entities/direct-message/direct-message.reducer';
import { IAdminUserProfile } from 'app/shared/model/admin-user-profile.model';
import { getEntities as getAdminUserProfiles } from 'app/entities/admin-user-profile/admin-user-profile.reducer';
import { IAdminAnnouncement } from 'app/shared/model/admin-announcement.model';
import { AdminAnnouncementType } from 'app/shared/model/enumerations/admin-announcement-type.model';
import { getEntity, updateEntity, createEntity, reset } from './admin-announcement.reducer';

export const AdminAnnouncementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const directMessages = useAppSelector(state => state.admin.directMessage.entities);
  const adminUserProfiles = useAppSelector(state => state.admin.adminUserProfile.entities);
  const adminAnnouncementEntity = useAppSelector(state => state.admin.adminAnnouncement.entity);
  const loading = useAppSelector(state => state.admin.adminAnnouncement.loading);
  const updating = useAppSelector(state => state.admin.adminAnnouncement.updating);
  const updateSuccess = useAppSelector(state => state.admin.adminAnnouncement.updateSuccess);
  const adminAnnouncementTypeValues = Object.keys(AdminAnnouncementType);

  const handleClose = () => {
    navigate('/admin-announcement' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDirectMessages({}));
    dispatch(getAdminUserProfiles({}));
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
      ...adminAnnouncementEntity,
      ...values,
      announcerMessage: directMessages.find(it => it.id.toString() === values.announcerMessage.toString()),
      admin: adminUserProfiles.find(it => it.id.toString() === values.admin.toString()),
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
          announcementType: 'BANNER',
          ...adminAnnouncementEntity,
          createdDate: convertDateTimeFromServer(adminAnnouncementEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(adminAnnouncementEntity.lastModifiedDate),
          announcerMessage: adminAnnouncementEntity?.announcerMessage?.id,
          admin: adminAnnouncementEntity?.admin?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.adminAnnouncement.home.createOrEditLabel" data-cy="AdminAnnouncementCreateUpdateHeading">
            <Translate contentKey="adminApp.adminAnnouncement.home.createOrEditLabel">Create or edit a AdminAnnouncement</Translate>
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
                  id="admin-announcement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.adminAnnouncement.announcementType')}
                id="admin-announcement-announcementType"
                name="announcementType"
                data-cy="announcementType"
                type="select"
              >
                {adminAnnouncementTypeValues.map(adminAnnouncementType => (
                  <option value={adminAnnouncementType} key={adminAnnouncementType}>
                    {translate('adminApp.AdminAnnouncementType.' + adminAnnouncementType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.adminAnnouncement.title')}
                id="admin-announcement-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminAnnouncement.content')}
                id="admin-announcement-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminAnnouncement.createdDate')}
                id="admin-announcement-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.adminAnnouncement.lastModifiedDate')}
                id="admin-announcement-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.adminAnnouncement.createdBy')}
                id="admin-announcement-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.adminAnnouncement.lastModifiedBy')}
                id="admin-announcement-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                id="admin-announcement-announcerMessage"
                name="announcerMessage"
                data-cy="announcerMessage"
                label={translate('adminApp.adminAnnouncement.announcerMessage')}
                type="select"
              >
                <option value="" key="0" />
                {directMessages
                  ? directMessages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="admin-announcement-admin"
                name="admin"
                data-cy="admin"
                label={translate('adminApp.adminAnnouncement.admin')}
                type="select"
                required
              >
                <option value="" key="0" />
                {adminUserProfiles
                  ? adminUserProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/admin-announcement" replace color="info">
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

export default AdminAnnouncementUpdate;
