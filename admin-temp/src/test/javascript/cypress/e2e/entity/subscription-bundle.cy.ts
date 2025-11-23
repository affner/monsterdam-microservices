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

describe('SubscriptionBundle e2e test', () => {
  const subscriptionBundlePageUrl = '/subscription-bundle';
  const subscriptionBundlePageUrlPattern = new RegExp('/subscription-bundle(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const subscriptionBundleSample = {"amount":741.41,"duration":17730,"createdDate":"2024-02-29T01:48:40.643Z","isDeleted":true};

  let subscriptionBundle;
  // let userProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {"emailContact":"_w","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"equivalent deal where","coverPhotoS3Key":"packaging scud helplessly","mainContentUrl":"unhinge baa brazen","mobilePhone":"+62816226866584","websiteUrl":"y@a9pR.,","amazonWishlistUrl":"<Nt$@^#Ee.Jp","lastLoginDate":"2024-02-29T16:00:43.411Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":false,"createdDate":"2024-02-29T22:05:30.454Z","lastModifiedDate":"2024-02-29T11:07:02.535Z","createdBy":"stopsign","lastModifiedBy":"oddly oh","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/subscription-bundles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/subscription-bundles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/subscription-bundles/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/purchased-subscriptions', {
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
    if (subscriptionBundle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subscription-bundles/${subscriptionBundle.id}`,
      }).then(() => {
        subscriptionBundle = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
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

  it('SubscriptionBundles menu should load SubscriptionBundles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('subscription-bundle');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubscriptionBundle').should('exist');
    cy.url().should('match', subscriptionBundlePageUrlPattern);
  });

  describe('SubscriptionBundle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subscriptionBundlePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubscriptionBundle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/subscription-bundle/new$'));
        cy.getEntityCreateUpdateHeading('SubscriptionBundle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionBundlePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/subscription-bundles',
          body: {
            ...subscriptionBundleSample,
            creator: userProfile,
          },
        }).then(({ body }) => {
          subscriptionBundle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/subscription-bundles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/subscription-bundles?page=0&size=20>; rel="last",<http://localhost/api/subscription-bundles?page=0&size=20>; rel="first"',
              },
              body: [subscriptionBundle],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(subscriptionBundlePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(subscriptionBundlePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details SubscriptionBundle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subscriptionBundle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionBundlePageUrlPattern);
      });

      it('edit button click should load edit SubscriptionBundle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscriptionBundle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionBundlePageUrlPattern);
      });

      it('edit button click should load edit SubscriptionBundle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscriptionBundle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionBundlePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of SubscriptionBundle', () => {
        cy.intercept('GET', '/api/subscription-bundles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subscriptionBundle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionBundlePageUrlPattern);

        subscriptionBundle = undefined;
      });
    });
  });

  describe('new SubscriptionBundle page', () => {
    beforeEach(() => {
      cy.visit(`${subscriptionBundlePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SubscriptionBundle');
    });

    it.skip('should create an instance of SubscriptionBundle', () => {
      cy.get(`[data-cy="amount"]`).type('972.17');
      cy.get(`[data-cy="amount"]`).should('have.value', '972.17');

      cy.get(`[data-cy="duration"]`).type('PT57M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT57M');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T08:13');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T08:13');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T15:22');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T15:22');

      cy.get(`[data-cy="createdBy"]`).type('failing esteemed flag');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'failing esteemed flag');

      cy.get(`[data-cy="lastModifiedBy"]`).type('shakily');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'shakily');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creator"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        subscriptionBundle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', subscriptionBundlePageUrlPattern);
    });
  });
});
