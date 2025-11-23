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

describe('PaymentTransaction e2e test', () => {
  const paymentTransactionPageUrl = '/payment-transaction';
  const paymentTransactionPageUrlPattern = new RegExp('/payment-transaction(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const paymentTransactionSample = {"amount":25229.08,"paymentDate":"2024-02-29T04:16:57.880Z","paymentStatus":"CANCELED"};

  let paymentTransaction;
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
      body: {"emailContact":"2gh","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"if fatal","coverPhotoS3Key":"quiet","mainContentUrl":"oof","mobilePhone":"+164656094972","websiteUrl":"q2<@+J.${","amazonWishlistUrl":"U7@.x.K","lastLoginDate":"2024-02-29T19:35:08.556Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T22:08:03.092Z","lastModifiedDate":"2024-02-29T02:19:29.218Z","createdBy":"muted wholly often","lastModifiedBy":"calmly presume absolute","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-transactions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-transactions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-transactions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/payment-methods', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/payment-providers', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

    cy.intercept('GET', '/api/accounting-records', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchased-contents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchased-subscriptions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/wallet-transactions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchased-tips', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (paymentTransaction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-transactions/${paymentTransaction.id}`,
      }).then(() => {
        paymentTransaction = undefined;
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

  it('PaymentTransactions menu should load PaymentTransactions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-transaction');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentTransaction').should('exist');
    cy.url().should('match', paymentTransactionPageUrlPattern);
  });

  describe('PaymentTransaction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentTransactionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentTransaction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/payment-transaction/new$'));
        cy.getEntityCreateUpdateHeading('PaymentTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentTransactionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-transactions',
          body: {
            ...paymentTransactionSample,
            viewer: userProfile,
          },
        }).then(({ body }) => {
          paymentTransaction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-transactions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payment-transactions?page=0&size=20>; rel="last",<http://localhost/api/payment-transactions?page=0&size=20>; rel="first"',
              },
              body: [paymentTransaction],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentTransactionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(paymentTransactionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PaymentTransaction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentTransaction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentTransactionPageUrlPattern);
      });

      it('edit button click should load edit PaymentTransaction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentTransactionPageUrlPattern);
      });

      it('edit button click should load edit PaymentTransaction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentTransaction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentTransactionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of PaymentTransaction', () => {
        cy.intercept('GET', '/api/payment-transactions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('paymentTransaction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentTransactionPageUrlPattern);

        paymentTransaction = undefined;
      });
    });
  });

  describe('new PaymentTransaction page', () => {
    beforeEach(() => {
      cy.visit(`${paymentTransactionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentTransaction');
    });

    it.skip('should create an instance of PaymentTransaction', () => {
      cy.get(`[data-cy="amount"]`).type('22576.08');
      cy.get(`[data-cy="amount"]`).should('have.value', '22576.08');

      cy.get(`[data-cy="paymentDate"]`).type('2024-02-29T07:14');
      cy.get(`[data-cy="paymentDate"]`).blur();
      cy.get(`[data-cy="paymentDate"]`).should('have.value', '2024-02-29T07:14');

      cy.get(`[data-cy="paymentStatus"]`).select('DECLINED');

      cy.get(`[data-cy="paymentReference"]`).type('wetly under');
      cy.get(`[data-cy="paymentReference"]`).should('have.value', 'wetly under');

      cy.get(`[data-cy="cloudTransactionId"]`).type('athwart');
      cy.get(`[data-cy="cloudTransactionId"]`).should('have.value', 'athwart');

      cy.get(`[data-cy="viewer"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paymentTransaction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paymentTransactionPageUrlPattern);
    });
  });
});
