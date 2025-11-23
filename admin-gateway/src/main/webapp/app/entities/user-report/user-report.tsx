import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { searchEntities, getEntities } from './user-report.reducer';

export const UserReport = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const userReportList = useAppSelector(state => state.admin.userReport.entities);
  const loading = useAppSelector(state => state.admin.userReport.loading);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    } else {
      dispatch(
        getEntities({
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    }
  };

  const startSearching = e => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort, search]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="user-report-heading" data-cy="UserReportHeading">
        <Translate contentKey="adminApp.userReport.home.title">User Reports</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.userReport.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-report/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.userReport.home.createLabel">Create new User Report</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('adminApp.userReport.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {userReportList && userReportList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.userReport.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('reportDescription')}>
                  <Translate contentKey="adminApp.userReport.reportDescription">Report Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reportDescription')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="adminApp.userReport.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.userReport.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.userReport.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.userReport.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.userReport.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="adminApp.userReport.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th className="hand" onClick={sort('reportCategory')}>
                  <Translate contentKey="adminApp.userReport.reportCategory">Report Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reportCategory')} />
                </th>
                <th className="hand" onClick={sort('reporterId')}>
                  <Translate contentKey="adminApp.userReport.reporterId">Reporter Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reporterId')} />
                </th>
                <th className="hand" onClick={sort('reportedId')}>
                  <Translate contentKey="adminApp.userReport.reportedId">Reported Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reportedId')} />
                </th>
                <th className="hand" onClick={sort('multimediaId')}>
                  <Translate contentKey="adminApp.userReport.multimediaId">Multimedia Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('multimediaId')} />
                </th>
                <th className="hand" onClick={sort('messageId')}>
                  <Translate contentKey="adminApp.userReport.messageId">Message Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('messageId')} />
                </th>
                <th className="hand" onClick={sort('postId')}>
                  <Translate contentKey="adminApp.userReport.postId">Post Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postId')} />
                </th>
                <th className="hand" onClick={sort('commentId')}>
                  <Translate contentKey="adminApp.userReport.commentId">Comment Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('commentId')} />
                </th>
                <th>
                  <Translate contentKey="adminApp.userReport.ticket">Ticket</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userReportList.map((userReport, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-report/${userReport.id}`} color="link" size="sm">
                      {userReport.id}
                    </Button>
                  </td>
                  <td>{userReport.reportDescription}</td>
                  <td>
                    <Translate contentKey={`adminApp.ReportStatus.${userReport.status}`} />
                  </td>
                  <td>
                    {userReport.createdDate ? <TextFormat type="date" value={userReport.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {userReport.lastModifiedDate ? (
                      <TextFormat type="date" value={userReport.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userReport.createdBy}</td>
                  <td>{userReport.lastModifiedBy}</td>
                  <td>{userReport.isDeleted ? 'true' : 'false'}</td>
                  <td>
                    <Translate contentKey={`adminApp.ReportCategory.${userReport.reportCategory}`} />
                  </td>
                  <td>{userReport.reporterId}</td>
                  <td>{userReport.reportedId}</td>
                  <td>{userReport.multimediaId}</td>
                  <td>{userReport.messageId}</td>
                  <td>{userReport.postId}</td>
                  <td>{userReport.commentId}</td>
                  <td>{userReport.ticket ? <Link to={`/assistance-ticket/${userReport.ticket.id}`}>{userReport.ticket.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-report/${userReport.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/user-report/${userReport.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/user-report/${userReport.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="adminApp.userReport.home.notFound">No User Reports found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserReport;
