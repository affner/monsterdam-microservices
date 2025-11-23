import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.admin.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">
          <Translate contentKey="adminApp.userProfile.detail.title">UserProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="emailContact">
              <Translate contentKey="adminApp.userProfile.emailContact">Email Contact</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.emailContact}</dd>
          <dt>
            <span id="profilePhoto">
              <Translate contentKey="adminApp.userProfile.profilePhoto">Profile Photo</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.profilePhoto ? (
              <div>
                {userProfileEntity.profilePhotoContentType ? (
                  <a onClick={openFile(userProfileEntity.profilePhotoContentType, userProfileEntity.profilePhoto)}>
                    <img
                      src={`data:${userProfileEntity.profilePhotoContentType};base64,${userProfileEntity.profilePhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {userProfileEntity.profilePhotoContentType}, {byteSize(userProfileEntity.profilePhoto)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="coverPhoto">
              <Translate contentKey="adminApp.userProfile.coverPhoto">Cover Photo</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.coverPhoto ? (
              <div>
                {userProfileEntity.coverPhotoContentType ? (
                  <a onClick={openFile(userProfileEntity.coverPhotoContentType, userProfileEntity.coverPhoto)}>
                    <img
                      src={`data:${userProfileEntity.coverPhotoContentType};base64,${userProfileEntity.coverPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {userProfileEntity.coverPhotoContentType}, {byteSize(userProfileEntity.coverPhoto)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="profilePhotoS3Key">
              <Translate contentKey="adminApp.userProfile.profilePhotoS3Key">Profile Photo S 3 Key</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.profilePhotoS3Key}</dd>
          <dt>
            <span id="coverPhotoS3Key">
              <Translate contentKey="adminApp.userProfile.coverPhotoS3Key">Cover Photo S 3 Key</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.coverPhotoS3Key}</dd>
          <dt>
            <span id="mainContentUrl">
              <Translate contentKey="adminApp.userProfile.mainContentUrl">Main Content Url</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.mainContentUrl}</dd>
          <dt>
            <span id="mobilePhone">
              <Translate contentKey="adminApp.userProfile.mobilePhone">Mobile Phone</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.mobilePhone}</dd>
          <dt>
            <span id="websiteUrl">
              <Translate contentKey="adminApp.userProfile.websiteUrl">Website Url</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.websiteUrl}</dd>
          <dt>
            <span id="amazonWishlistUrl">
              <Translate contentKey="adminApp.userProfile.amazonWishlistUrl">Amazon Wishlist Url</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.amazonWishlistUrl}</dd>
          <dt>
            <span id="lastLoginDate">
              <Translate contentKey="adminApp.userProfile.lastLoginDate">Last Login Date</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.lastLoginDate ? (
              <TextFormat value={userProfileEntity.lastLoginDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="biography">
              <Translate contentKey="adminApp.userProfile.biography">Biography</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.biography}</dd>
          <dt>
            <span id="isFree">
              <Translate contentKey="adminApp.userProfile.isFree">Is Free</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.isFree ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.userProfile.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.createdDate ? (
              <TextFormat value={userProfileEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.userProfile.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.lastModifiedDate ? (
              <TextFormat value={userProfileEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.userProfile.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.userProfile.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.userProfile.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.userLite">User Lite</Translate>
          </dt>
          <dd>{userProfileEntity.userLite ? userProfileEntity.userLite.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.settings">Settings</Translate>
          </dt>
          <dd>{userProfileEntity.settings ? userProfileEntity.settings.id : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.countryOfBirth">Country Of Birth</Translate>
          </dt>
          <dd>{userProfileEntity.countryOfBirth ? userProfileEntity.countryOfBirth.name : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.stateOfResidence">State Of Residence</Translate>
          </dt>
          <dd>{userProfileEntity.stateOfResidence ? userProfileEntity.stateOfResidence.stateName : ''}</dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.followed">Followed</Translate>
          </dt>
          <dd>
            {userProfileEntity.followeds
              ? userProfileEntity.followeds.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {userProfileEntity.followeds && i === userProfileEntity.followeds.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.blockedList">Blocked List</Translate>
          </dt>
          <dd>
            {userProfileEntity.blockedLists
              ? userProfileEntity.blockedLists.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {userProfileEntity.blockedLists && i === userProfileEntity.blockedLists.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.loyaLists">Loya Lists</Translate>
          </dt>
          <dd>
            {userProfileEntity.loyaLists
              ? userProfileEntity.loyaLists.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {userProfileEntity.loyaLists && i === userProfileEntity.loyaLists.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.subscribed">Subscribed</Translate>
          </dt>
          <dd>
            {userProfileEntity.subscribeds
              ? userProfileEntity.subscribeds.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {userProfileEntity.subscribeds && i === userProfileEntity.subscribeds.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.joinedEvents">Joined Events</Translate>
          </dt>
          <dd>
            {userProfileEntity.joinedEvents
              ? userProfileEntity.joinedEvents.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.startDate}</a>
                    {userProfileEntity.joinedEvents && i === userProfileEntity.joinedEvents.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.blockedUbications">Blocked Ubications</Translate>
          </dt>
          <dd>
            {userProfileEntity.blockedUbications
              ? userProfileEntity.blockedUbications.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {userProfileEntity.blockedUbications && i === userProfileEntity.blockedUbications.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="adminApp.userProfile.hashTags">Hash Tags</Translate>
          </dt>
          <dd>
            {userProfileEntity.hashTags
              ? userProfileEntity.hashTags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {userProfileEntity.hashTags && i === userProfileEntity.hashTags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;
