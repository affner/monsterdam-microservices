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

describe('WalletTransaction e2e test', () => {
  const walletTransactionPageUrl = '/wallet-transaction';
  const walletTransactionPageUrlPattern = new RegExp('/wallet-transaction(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const walletTransactionSample = {"amount":4958.38,"transactionType":"TOP_UP","createdDate":"2024-02-29T01:43:04.027Z","isDeleted":false};

  let walletTransaction;
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
      body: {"emailContact":"9_i7h_","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"grove","coverPhotoS3Key":"sanction intentional","mainContentUrl":"wrongly until to","mobilePhone":"+882154216036632","websiteUrl":"7k^0c-@<ACY.3dz","amazonWishlistUrl":")\"VHOG@%L%.h%rKC","lastLoginDate":"2024-02-29T22:26:42.116Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T19:10:04.783Z","lastModifiedDate":"2024-02-29T16:36:19.411Z","createdBy":"offensively","lastModifiedBy":"alongside","isDeleted":true},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/wallet-transactions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/wallet-transactions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/wallet-transactions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/payment-transactions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

    cy.intercept('GET', '/api/purchased-contents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchased-subscriptions', {
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
    if (walletTransaction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/wallet-transactions/${walletTransaction.id}`,
      }).then(() => {
        walletTransaction = undefined;
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

  it('WalletTransactions menu should load WalletTransactions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('wallet-transaction');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WalletTransaction').should('exist');
    cy.url().should('match', walletTransactionPageUrlPattern);
  });

  describe('WalletTransaction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(walletTransactionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WalletTransaction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/wallet-transaction/new$'));
        cy.getEntityCreateUpdateHeading('WalletTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', walletTransactionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/wallet-transactions',
          body: {
            ...walletTransactionSample,
            viewer: userProfile,
          },
        }).then(({ body }) => {
          walletTransaction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/wallet-transactions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/wallet-transactions?page=0&size=20>; rel="last",<http://localhost/api/wallet-transactions?page=0&size=20>; rel="first"',
              },
              body: [walletTransaction],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(walletTransactionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(walletTransactionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details WalletTransaction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('walletTransaction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', walletTransactionPageUrlPattern);
      });

      it('edit button click should load edit WalletTransaction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WalletTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', walletTransactionPageUrlPattern);
      });

      it('edit button click should load edit WalletTransaction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WalletTransaction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', walletTransactionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of WalletTransaction', () => {
        cy.intercept('GET', '/api/wallet-transactions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('walletTransaction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', walletTransactionPageUrlPattern);

        walletTransaction = undefined;
      });
    });
  });

  describe('new WalletTransaction page', () => {
    beforeEach(() => {
      cy.visit(`${walletTransactionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WalletTransaction');
    });

    it.skip('should create an instance of WalletTransaction', () => {
      cy.get(`[data-cy="amount"]`).type('25222.39');
      cy.get(`[data-cy="amount"]`).should('have.value', '25222.39');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T08:48');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T08:48');

      cy.get(`[data-cy="transactionType"]`).select('TOP_UP');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T17:29');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T17:29');

      cy.get(`[data-cy="createdBy"]`).type('during letter');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'during letter');

      cy.get(`[data-cy="lastModifiedBy"]`).type('like');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'like');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="viewer"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        walletTransaction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', walletTransactionPageUrlPattern);
    });
  });
});
