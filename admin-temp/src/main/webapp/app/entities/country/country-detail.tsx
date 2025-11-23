import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './country.reducer';

export const CountryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const countryEntity = useAppSelector(state => state.admin.country.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="countryDetailsHeading">
          <Translate contentKey="adminApp.country.detail.title">Country</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{countryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="adminApp.country.name">Name</Translate>
            </span>
          </dt>
          <dd>{countryEntity.name}</dd>
          <dt>
            <span id="alpha2Code">
              <Translate contentKey="adminApp.country.alpha2Code">Alpha 2 Code</Translate>
            </span>
          </dt>
          <dd>{countryEntity.alpha2Code}</dd>
          <dt>
            <span id="alpha3Code">
              <Translate contentKey="adminApp.country.alpha3Code">Alpha 3 Code</Translate>
            </span>
          </dt>
          <dd>{countryEntity.alpha3Code}</dd>
          <dt>
            <span id="phoneCode">
              <Translate contentKey="adminApp.country.phoneCode">Phone Code</Translate>
            </span>
          </dt>
          <dd>{countryEntity.phoneCode}</dd>
          <dt>
            <span id="thumbnailCountry">
              <Translate contentKey="adminApp.country.thumbnailCountry">Thumbnail Country</Translate>
            </span>
          </dt>
          <dd>
            {countryEntity.thumbnailCountry ? (
              <div>
                {countryEntity.thumbnailCountryContentType ? (
                  <a onClick={openFile(countryEntity.thumbnailCountryContentType, countryEntity.thumbnailCountry)}>
                    <img
                      src={`data:${countryEntity.thumbnailCountryContentType};base64,${countryEntity.thumbnailCountry}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {countryEntity.thumbnailCountryContentType}, {byteSize(countryEntity.thumbnailCountry)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="adminApp.country.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {countryEntity.createdDate ? <TextFormat value={countryEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="adminApp.country.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {countryEntity.lastModifiedDate ? (
              <TextFormat value={countryEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="adminApp.country.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{countryEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="adminApp.country.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{countryEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="adminApp.country.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{countryEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/country" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/country/${countryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CountryDetail;
