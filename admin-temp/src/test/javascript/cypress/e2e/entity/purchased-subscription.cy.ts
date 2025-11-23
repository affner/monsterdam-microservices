import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('PurchasedSubscription e2e test', () => {
  const purchasedSubscriptionPageUrl = '/purchased-subscription';
  const purchasedSubscriptionPageUrlPattern = new RegExp('/purchased-subscription(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const purchasedSubscriptionSample = {"createdDate":"2024-02-29T04:02:23.111Z","isDeleted":true,"endDate":"2024-02-28","subscriptionStatus":"EXPIRED","viewerId":164,"creatorId":16397};

  let purchasedSubscription;
  // let creatorEarning;
  // let subscriptionBundle;
  // let userProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/creator-earnings',
      body: {"amount":25495.25,"createdDate":"2024-02-29T21:19:57.853Z","lastModifiedDate":"2024-02-29T00:48:19.324Z","createdBy":"since","lastModifiedBy":"thorough but","isDeleted":false},
    }).then(({ body }) => {
      creatorEarning = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/subscription-bundles',
      body: {"amount":502.1,"duration":31243,"createdDate":"2024-02-29T07:11:28.151Z","lastModifiedDate":"2024-02-29T19:23:47.136Z","createdBy":"book insulate","lastModifiedBy":"unless marinate berth","isDeleted":true},
    }).then(({ body }) => {
      subscriptionBundle = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {"emailContact":"gu","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"corny afore","coverPhotoS3Key":"wearily preface","mainContentUrl":"uh-huh","mobilePhone":"+43682362059","websiteUrl":"MY#V@PK^ZTr.E2q","amazonWishlistUrl":"/b7.];@=j.ZhT","lastLoginDate":"2024-02-29T05:54:16.744Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T04:46:33.042Z","lastModifiedDate":"2024-02-29T06:27:03.753Z","createdBy":"woot except snowsuit","lastModifiedBy":"sit caffeine","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/purchased-subscriptions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/purchased-subscriptions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/purchased-subscriptions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/payment-transactions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/wallet-transactions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/creator-earnings', {
      statusCode: 200,
      body: [creatorEarning],
    });

    cy.intercept('GET', '/api/subscription-bundles', {
      statusCode: 200,
      body: [subscriptionBundle],
    });

    cy.intercept('GET', '/api/offer-promotions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

  });
   */

  afterEach(() => {
    if (purchasedSubscription) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/purchased-subscriptions/${purchasedSubscription.id}`,
      }).then(() => {
        purchasedSubscription = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (creatorEarning) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/creator-earnings/${creatorEarning.id}`,
      }).then(() => {
        creatorEarning = undefined;
      });
    }
    if (subscriptionBundle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subscription-bundles/${subscriptionBundle.id}`,
      }).then(() => {
        subscriptionBundle = undefined;
      });
    }
    if (userProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-profiles/${userProfile.id}`,
      }).then(() => {
        userProfile = undefined;
      });
    }
  });
   */

  it('PurchasedSubscriptions menu should load PurchasedSubscriptions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('purchased-subscription');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PurchasedSubscription').should('exist');
    cy.url().should('match', purchasedSubscriptionPageUrlPattern);
  });

  describe('PurchasedSubscription page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(purchasedSubscriptionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PurchasedSubscription page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/purchased-subscription/new$'));
        cy.getEntityCreateUpdateHeading('PurchasedSubscription');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedSubscriptionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/purchased-subscriptions',
          body: {
            ...purchasedSubscriptionSample,
            creatorEarning: creatorEarning,
            subscriptionBundle: subscriptionBundle,
            viewer: userProfile,
          },
        }).then(({ body }) => {
          purchasedSubscription = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/purchased-subscriptions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/purchased-subscriptions?page=0&size=20>; rel="last",<http://localhost/api/purchased-subscriptions?page=0&size=20>; rel="first"',
              },
              body: [purchasedSubscription],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(purchasedSubscriptionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(purchasedSubscriptionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PurchasedSubscription page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('purchasedSubscription');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedSubscriptionPageUrlPattern);
      });

      it('edit button click should load edit PurchasedSubscription page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedSubscription');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedSubscriptionPageUrlPattern);
      });

      it('edit button click should load edit PurchasedSubscription page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedSubscription');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedSubscriptionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of PurchasedSubscription', () => {
        cy.intercept('GET', '/api/purchased-subscriptions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('purchasedSubscription').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedSubscriptionPageUrlPattern);

        purchasedSubscription = undefined;
      });
    });
  });

  describe('new PurchasedSubscription page', () => {
    beforeEach(() => {
      cy.visit(`${purchasedSubscriptionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PurchasedSubscription');
    });

    it.skip('should create an instance of PurchasedSubscription', () => {
      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T07:34');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T07:34');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T10:29');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T10:29');

      cy.get(`[data-cy="createdBy"]`).type('questioningly');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'questioningly');

      cy.get(`[data-cy="lastModifiedBy"]`).type('lest');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'lest');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="endDate"]`).type('2024-02-29');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="subscriptionStatus"]`).select('CANCELLED');

      cy.get(`[data-cy="viewerId"]`).type('10808');
      cy.get(`[data-cy="viewerId"]`).should('have.value', '10808');

      cy.get(`[data-cy="creatorId"]`).type('27712');
      cy.get(`[data-cy="creatorId"]`).should('have.value', '27712');

      cy.get(`[data-cy="creatorEarning"]`).select(1);
      cy.get(`[data-cy="subscriptionBundle"]`).select(1);
      cy.get(`[data-cy="viewer"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        purchasedSubscription = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', purchasedSubscriptionPageUrlPattern);
    });
  });
});
