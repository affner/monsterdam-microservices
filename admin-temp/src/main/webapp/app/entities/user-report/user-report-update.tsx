import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAssistanceTicket } from 'app/shared/model/assistance-ticket.model';
import { getEntities as getAssistanceTickets } from 'app/entities/assistance-ticket/assistance-ticket.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IVideoStory } from 'app/shared/model/video-story.model';
import { getEntities as getVideoStories } from 'app/entities/video-story/video-story.reducer';
import { ISingleVideo } from 'app/shared/model/single-video.model';
import { getEntities as getSingleVideos } from 'app/entities/single-video/single-video.reducer';
import { ISinglePhoto } from 'app/shared/model/single-photo.model';
import { getEntities as getSinglePhotos } from 'app/entities/single-photo/single-photo.reducer';
import { ISingleAudio } from 'app/shared/model/single-audio.model';
import { getEntities as getSingleAudios } from 'app/entities/single-audio/single-audio.reducer';
import { ISingleLiveStream } from 'app/shared/model/single-live-stream.model';
import { getEntities as getSingleLiveStreams } from 'app/entities/single-live-stream/single-live-stream.reducer';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { getEntities as getDirectMessages } from 'app/entities/direct-message/direct-message.reducer';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { getEntities as getPostFeeds } from 'app/entities/post-feed/post-feed.reducer';
import { IPostComment } from 'app/shared/model/post-comment.model';
import { getEntities as getPostComments } from 'app/entities/post-comment/post-comment.reducer';
import { IUserReport } from 'app/shared/model/user-report.model';
import { ReportStatus } from 'app/shared/model/enumerations/report-status.model';
import { ReportCategory } from 'app/shared/model/enumerations/report-category.model';
import { getEntity, updateEntity, createEntity, reset } from './user-report.reducer';

export const UserReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const assistanceTickets = useAppSelector(state => state.admin.assistanceTicket.entities);
  const userProfiles = useAppSelector(state => state.admin.userProfile.entities);
  const videoStories = useAppSelector(state => state.admin.videoStory.entities);
  const singleVideos = useAppSelector(state => state.admin.singleVideo.entities);
  const singlePhotos = useAppSelector(state => state.admin.singlePhoto.entities);
  const singleAudios = useAppSelector(state => state.admin.singleAudio.entities);
  const singleLiveStreams = useAppSelector(state => state.admin.singleLiveStream.entities);
  const directMessages = useAppSelector(state => state.admin.directMessage.entities);
  const postFeeds = useAppSelector(state => state.admin.postFeed.entities);
  const postComments = useAppSelector(state => state.admin.postComment.entities);
  const userReportEntity = useAppSelector(state => state.admin.userReport.entity);
  const loading = useAppSelector(state => state.admin.userReport.loading);
  const updating = useAppSelector(state => state.admin.userReport.updating);
  const updateSuccess = useAppSelector(state => state.admin.userReport.updateSuccess);
  const reportStatusValues = Object.keys(ReportStatus);
  const reportCategoryValues = Object.keys(ReportCategory);

  const handleClose = () => {
    navigate('/user-report' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAssistanceTickets({}));
    dispatch(getUserProfiles({}));
    dispatch(getVideoStories({}));
    dispatch(getSingleVideos({}));
    dispatch(getSinglePhotos({}));
    dispatch(getSingleAudios({}));
    dispatch(getSingleLiveStreams({}));
    dispatch(getDirectMessages({}));
    dispatch(getPostFeeds({}));
    dispatch(getPostComments({}));
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
      ...userReportEntity,
      ...values,
      ticket: assistanceTickets.find(it => it.id.toString() === values.ticket.toString()),
      reporter: userProfiles.find(it => it.id.toString() === values.reporter.toString()),
      reported: userProfiles.find(it => it.id.toString() === values.reported.toString()),
      story: videoStories.find(it => it.id.toString() === values.story.toString()),
      video: singleVideos.find(it => it.id.toString() === values.video.toString()),
      photo: singlePhotos.find(it => it.id.toString() === values.photo.toString()),
      audio: singleAudios.find(it => it.id.toString() === values.audio.toString()),
      liveStream: singleLiveStreams.find(it => it.id.toString() === values.liveStream.toString()),
      message: directMessages.find(it => it.id.toString() === values.message.toString()),
      post: postFeeds.find(it => it.id.toString() === values.post.toString()),
      postComment: postComments.find(it => it.id.toString() === values.postComment.toString()),
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
          status: 'PENDING',
          reportCategory: 'POST_REPORT',
          ...userReportEntity,
          createdDate: convertDateTimeFromServer(userReportEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userReportEntity.lastModifiedDate),
          ticket: userReportEntity?.ticket?.id,
          reporter: userReportEntity?.reporter?.id,
          reported: userReportEntity?.reported?.id,
          story: userReportEntity?.story?.id,
          video: userReportEntity?.video?.id,
          photo: userReportEntity?.photo?.id,
          audio: userReportEntity?.audio?.id,
          liveStream: userReportEntity?.liveStream?.id,
          message: userReportEntity?.message?.id,
          post: userReportEntity?.post?.id,
          postComment: userReportEntity?.postComment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="adminApp.userReport.home.createOrEditLabel" data-cy="UserReportCreateUpdateHeading">
            <Translate contentKey="adminApp.userReport.home.createOrEditLabel">Create or edit a UserReport</Translate>
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
                  id="user-report-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('adminApp.userReport.reportDescription')}
                id="user-report-reportDescription"
                name="reportDescription"
                data-cy="reportDescription"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userReport.status')}
                id="user-report-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {reportStatusValues.map(reportStatus => (
                  <option value={reportStatus} key={reportStatus}>
                    {translate('adminApp.ReportStatus.' + reportStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('adminApp.userReport.createdDate')}
                id="user-report-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('adminApp.userReport.lastModifiedDate')}
                id="user-report-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('adminApp.userReport.createdBy')}
                id="user-report-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userReport.lastModifiedBy')}
                id="user-report-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('adminApp.userReport.isDeleted')}
                id="user-report-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('adminApp.userReport.reportCategory')}
                id="user-report-reportCategory"
                name="reportCategory"
                data-cy="reportCategory"
                type="select"
              >
                {reportCategoryValues.map(reportCategory => (
                  <option value={reportCategory} key={reportCategory}>
                    {translate('adminApp.ReportCategory.' + reportCategory)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="user-report-ticket"
                name="ticket"
                data-cy="ticket"
                label={translate('adminApp.userReport.ticket')}
                type="select"
                required
              >
                <option value="" key="0" />
                {assistanceTickets
                  ? assistanceTickets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="user-report-reporter"
                name="reporter"
                data-cy="reporter"
                label={translate('adminApp.userReport.reporter')}
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
              <ValidatedField
                id="user-report-reported"
                name="reported"
                data-cy="reported"
                label={translate('adminApp.userReport.reported')}
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
              <ValidatedField
                id="user-report-story"
                name="story"
                data-cy="story"
                label={translate('adminApp.userReport.story')}
                type="select"
              >
                <option value="" key="0" />
                {videoStories
                  ? videoStories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.duration}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-report-video"
                name="video"
                data-cy="video"
                label={translate('adminApp.userReport.video')}
                type="select"
              >
                <option value="" key="0" />
                {singleVideos
                  ? singleVideos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.duration}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-report-photo"
                name="photo"
                data-cy="photo"
                label={translate('adminApp.userReport.photo')}
                type="select"
              >
                <option value="" key="0" />
                {singlePhotos
                  ? singlePhotos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.thumbnail}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-report-audio"
                name="audio"
                data-cy="audio"
                label={translate('adminApp.userReport.audio')}
                type="select"
              >
                <option value="" key="0" />
                {singleAudios
                  ? singleAudios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.duration}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-report-liveStream"
                name="liveStream"
                data-cy="liveStream"
                label={translate('adminApp.userReport.liveStream')}
                type="select"
              >
                <option value="" key="0" />
                {singleLiveStreams
                  ? singleLiveStreams.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.thumbnail}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-report-message"
                name="message"
                data-cy="message"
                label={translate('adminApp.userReport.message')}
                type="select"
              >
                <option value="" key="0" />
                {directMessages
                  ? directMessages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.messageContent}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="user-report-post" name="post" data-cy="post" label={translate('adminApp.userReport.post')} type="select">
                <option value="" key="0" />
                {postFeeds
                  ? postFeeds.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.postContent}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-report-postComment"
                name="postComment"
                data-cy="postComment"
                label={translate('adminApp.userReport.postComment')}
                type="select"
              >
                <option value="" key="0" />
                {postComments
                  ? postComments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.commentContent}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-report" replace color="info">
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

export default UserReportUpdate;
