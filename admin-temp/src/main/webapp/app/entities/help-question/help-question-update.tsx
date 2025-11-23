import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHelpRelatedArticle } from 'app/shared/model/help-related-article.model';
import { getEntities as getHelpRelatedArticles } from 'app/entities/help-related-article/help-related-article.reducer';
import { IHelpSubcategory } from 'app/shared/model/help-subcategory.model';
import { getEntities as getHelpSubcategories } from 'app/entities/help-subcategory/help-subcategory.reducer';
import { IHelpQuestion } from 'app/shared/model/help-question.model';
import { getEntity, updateEntity, createEntity, reset } from './help-question.reducer';

export const HelpQuestionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const helpRelatedArticles = useAppSelector(state => state.admin.helpRelatedArticle.entities);
  const helpSubcategories = useAppSelector(state => state.admin.helpSubcategory.entities);
  const helpQuestionEntity = useAppSelector(state => state.admin.helpQuestion.entity);
  const loading = useAppSelector(state => state.admin.helpQuestion.loading);
  const updating = useAppSelector(state => state.admin.helpQuestion.updating);
  const updateSuccess = useAppSelector(state => state.admin.helpQuestion.updateSuccess);

  const handleClose = () => {
    navigate('/help-question' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHelpRelatedArticles({}));
    dispatch(getHelpSubcategories({}));
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

    const entity = {
      ...helpQuestionEntity,
      ...values,
      questions: mapIdList(values.questions),
      subcategory: helpSubcategories.find(it => it.id.toString() === values.subcategory.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...helpQuestionEntity,
          questions: helpQuestionEntity?.questions?.map(e => e.id.toString()),
          subcategory: helpQuestionEntity?.subcategory?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.helpQuestion.home.createOrEditLabel" data-cy="HelpQuestionCreateUpdateHeading">
            <Translate contentKey="adminApp.helpQuestion.home.createOrEditLabel">Create or edit a HelpQuestion</Translate>
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
                  id="help-question-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.helpQuestion.title')}
                id="help-question-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.helpQuestion.content')}
                id="help-question-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.helpQuestion.isDeleted')}
                id="help-question-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.helpQuestion.question')}
                id="help-question-question"
                data-cy="question"
                type="select"
                multiple
                name="questions"
              >
                <option value="" key="0" />
                {helpRelatedArticles
                  ? helpRelatedArticles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="help-question-subcategory"
                name="subcategory"
                data-cy="subcategory"
                label={translate('adminApp.helpQuestion.subcategory')}
                type="select"
              >
                <option value="" key="0" />
                {helpSubcategories
                  ? helpSubcategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/help-question" replace color="info">
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

export default HelpQuestionUpdate;
