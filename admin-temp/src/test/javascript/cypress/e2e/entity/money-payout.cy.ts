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

describe('MoneyPayout e2e test', () => {
  const moneyPayoutPageUrl = '/money-payout';
  const moneyPayoutPageUrlPattern = new RegExp('/money-payout(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const moneyPayoutSample = {"amount":24874.33,"createdDate":"2024-02-29T19:31:59.930Z","isDeleted":false,"withdrawStatus":"PROCESSED"};

  let moneyPayout;
  // let creatorEarning;
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
      body: {"amount":11317.62,"createdDate":"2024-02-29T00:27:55.066Z","lastModifiedDate":"2024-02-29T12:51:14.673Z","createdBy":"daily aha untried","lastModifiedBy":"nor miserably","isDeleted":true},
    }).then(({ body }) => {
      creatorEarning = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {"emailContact":"0hucr1","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"phooey","coverPhotoS3Key":"up modulo interestingly","mainContentUrl":"whereas","mobilePhone":"+6094878363","websiteUrl":"N@o+7Muc.$/","amazonWishlistUrl":"mh\"@q[.j","lastLoginDate":"2024-02-29T05:43:56.639Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":false,"createdDate":"2024-02-29T15:38:54.386Z","lastModifiedDate":"2024-02-29T11:41:46.274Z","createdBy":"around or ignorant","lastModifiedBy":"absentmindedly","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/money-payouts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/money-payouts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/money-payouts/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/creator-earnings', {
      statusCode: 200,
      body: [creatorEarning],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

  });
   */

  afterEach(() => {
    if (moneyPayout) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/money-payouts/${moneyPayout.id}`,
      }).then(() => {
        moneyPayout = undefined;
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

  it('MoneyPayouts menu should load MoneyPayouts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('money-payout');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MoneyPayout').should('exist');
    cy.url().should('match', moneyPayoutPageUrlPattern);
  });

  describe('MoneyPayout page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(moneyPayoutPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MoneyPayout page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/money-payout/new$'));
        cy.getEntityCreateUpdateHeading('MoneyPayout');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyPayoutPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/money-payouts',
          body: {
            ...moneyPayoutSample,
            creatorEarning: creatorEarning,
            creator: userProfile,
          },
        }).then(({ body }) => {
          moneyPayout = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/money-payouts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/money-payouts?page=0&size=20>; rel="last",<http://localhost/api/money-payouts?page=0&size=20>; rel="first"',
              },
              body: [moneyPayout],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(moneyPayoutPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(moneyPayoutPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details MoneyPayout page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('moneyPayout');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyPayoutPageUrlPattern);
      });

      it('edit button click should load edit MoneyPayout page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoneyPayout');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyPayoutPageUrlPattern);
      });

      it('edit button click should load edit MoneyPayout page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoneyPayout');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyPayoutPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of MoneyPayout', () => {
        cy.intercept('GET', '/api/money-payouts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('moneyPayout').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyPayoutPageUrlPattern);

        moneyPayout = undefined;
      });
    });
  });

  describe('new MoneyPayout page', () => {
    beforeEach(() => {
      cy.visit(`${moneyPayoutPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MoneyPayout');
    });

    it.skip('should create an instance of MoneyPayout', () => {
      cy.get(`[data-cy="amount"]`).type('10265.54');
      cy.get(`[data-cy="amount"]`).should('have.value', '10265.54');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T02:05');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T02:05');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T03:04');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T03:04');

      cy.get(`[data-cy="createdBy"]`).type('yum although');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'yum although');

      cy.get(`[data-cy="lastModifiedBy"]`).type('perfumed playfully even');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'perfumed playfully even');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="withdrawStatus"]`).select('PROCESSED');

      cy.get(`[data-cy="creatorEarning"]`).select(1);
      cy.get(`[data-cy="creator"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        moneyPayout = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', moneyPayoutPageUrlPattern);
    });
  });
});
