import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { searchEntities, getEntities } from './purchased-subscription.reducer';

export const PurchasedSubscription = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const purchasedSubscriptionList = useAppSelector(state => state.admin.purchasedSubscription.entities);
  const loading = useAppSelector(state => state.admin.purchasedSubscription.loading);
  const totalItems = useAppSelector(state => state.admin.purchasedSubscription.totalItems);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    } else {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    }
  };

  const startSearching = e => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="purchased-subscription-heading" data-cy="PurchasedSubscriptionHeading">
        <Translate contentKey="adminApp.purchasedSubscription.home.title">Purchased Subscriptions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="adminApp.purchasedSubscription.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/purchased-subscription/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="adminApp.purchasedSubscription.home.createLabel">Create new Purchased Subscription</Translate>
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
                  placeholder={translate('adminApp.purchasedSubscription.home.search')}
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
        {purchasedSubscriptionList && purchasedSubscriptionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="adminApp.purchasedSubscription.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="adminApp.purchasedSubscription.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="adminApp.purchasedSubscription.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="adminApp.purchasedSubscription.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="adminApp.purchasedSubscription.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="adminApp.purchasedSubscription.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="adminApp.purchasedSubscription.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('subscriptionStatus')}>
                  <Translate contentKey="adminApp.purchasedSubscription.subscriptionStatus">Subscription Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subscriptionStatus')} />
                </th>
                <th className="hand" onClick={sort('viewerId')}>
                  <Translate contentKey="adminApp.purchasedSubscription.viewerId">Viewer Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('viewerId')} />
                </th>
                <th className="hand" onClick={sort('creatorId')}>
                  <Translate contentKey="adminApp.purchasedSubscription.creatorId">Creator Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('creatorId')} />
                </th>
                <th>
                  <Translate contentKey="adminApp.purchasedSubscription.payment">Payment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.purchasedSubscription.walletTransaction">Wallet Transaction</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.purchasedSubscription.creatorEarning">Creator Earning</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.purchasedSubscription.subscriptionBundle">Subscription Bundle</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.purchasedSubscription.appliedPromotion">Applied Promotion</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="adminApp.purchasedSubscription.viewer">Viewer</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {purchasedSubscriptionList.map((purchasedSubscription, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/purchased-subscription/${purchasedSubscription.id}`} color="link" size="sm">
                      {purchasedSubscription.id}
                    </Button>
                  </td>
                  <td>
                    {purchasedSubscription.createdDate ? (
                      <TextFormat type="date" value={purchasedSubscription.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {purchasedSubscription.lastModifiedDate ? (
                      <TextFormat type="date" value={purchasedSubscription.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{purchasedSubscription.createdBy}</td>
                  <td>{purchasedSubscription.lastModifiedBy}</td>
                  <td>{purchasedSubscription.isDeleted ? 'true' : 'false'}</td>
                  <td>
                    {purchasedSubscription.endDate ? (
                      <TextFormat type="date" value={purchasedSubscription.endDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`adminApp.PurchasedSubscriptionStatus.${purchasedSubscription.subscriptionStatus}`} />
                  </td>
                  <td>{purchasedSubscription.viewerId}</td>
                  <td>{purchasedSubscription.creatorId}</td>
                  <td>
                    {purchasedSubscription.payment ? (
                      <Link to={`/payment-transaction/${purchasedSubscription.payment.id}`}>{purchasedSubscription.payment.amount}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {purchasedSubscription.walletTransaction ? (
                      <Link to={`/wallet-transaction/${purchasedSubscription.walletTransaction.id}`}>
                        {purchasedSubscription.walletTransaction.amount}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {purchasedSubscription.creatorEarning ? (
                      <Link to={`/creator-earning/${purchasedSubscription.creatorEarning.id}`}>
                        {purchasedSubscription.creatorEarning.amount}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {purchasedSubscription.subscriptionBundle ? (
                      <Link to={`/subscription-bundle/${purchasedSubscription.subscriptionBundle.id}`}>
                        {purchasedSubscription.subscriptionBundle.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {purchasedSubscription.appliedPromotion ? (
                      <Link to={`/offer-promotion/${purchasedSubscription.appliedPromotion.id}`}>
                        {purchasedSubscription.appliedPromotion.promotionType}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {purchasedSubscription.viewer ? (
                      <Link to={`/user-profile/${purchasedSubscription.viewer.id}`}>{purchasedSubscription.viewer.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/purchased-subscription/${purchasedSubscription.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/purchased-subscription/${purchasedSubscription.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/purchased-subscription/${purchasedSubscription.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
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
              <Translate contentKey="adminApp.purchasedSubscription.home.notFound">No Purchased Subscriptions found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={purchasedSubscriptionList && purchasedSubscriptionList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default PurchasedSubscription;
