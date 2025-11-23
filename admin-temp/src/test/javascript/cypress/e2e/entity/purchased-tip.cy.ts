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

describe('PurchasedTip e2e test', () => {
  const purchasedTipPageUrl = '/purchased-tip';
  const purchasedTipPageUrlPattern = new RegExp('/purchased-tip(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const purchasedTipSample = {"amount":3273.42,"createdDate":"2024-02-29T04:13:14.461Z","isDeleted":false};

  let purchasedTip;
  // let creatorEarning;
  // let directMessage;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/creator-earnings',
      body: {"amount":30976.83,"createdDate":"2024-02-29T22:44:18.979Z","lastModifiedDate":"2024-02-29T16:46:01.474Z","createdBy":"zowie psst hm","lastModifiedBy":"boohoo","isDeleted":false},
    }).then(({ body }) => {
      creatorEarning = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/direct-messages',
      body: {"messageContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","readDate":"2024-02-29T04:38:29.271Z","likeCount":12700,"isHidden":false,"createdDate":"2024-02-29T16:07:27.235Z","lastModifiedDate":"2024-02-28T23:52:46.958Z","createdBy":"supposing excluding","lastModifiedBy":"which","isDeleted":true},
    }).then(({ body }) => {
      directMessage = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/purchased-tips+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/purchased-tips').as('postEntityRequest');
    cy.intercept('DELETE', '/api/purchased-tips/*').as('deleteEntityRequest');
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

    cy.intercept('GET', '/api/direct-messages', {
      statusCode: 200,
      body: [directMessage],
    });

  });
   */

  afterEach(() => {
    if (purchasedTip) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/purchased-tips/${purchasedTip.id}`,
      }).then(() => {
        purchasedTip = undefined;
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
    if (directMessage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/direct-messages/${directMessage.id}`,
      }).then(() => {
        directMessage = undefined;
      });
    }
  });
   */

  it('PurchasedTips menu should load PurchasedTips page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('purchased-tip');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PurchasedTip').should('exist');
    cy.url().should('match', purchasedTipPageUrlPattern);
  });

  describe('PurchasedTip page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(purchasedTipPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PurchasedTip page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/purchased-tip/new$'));
        cy.getEntityCreateUpdateHeading('PurchasedTip');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedTipPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/purchased-tips',
          body: {
            ...purchasedTipSample,
            creatorEarning: creatorEarning,
            message: directMessage,
          },
        }).then(({ body }) => {
          purchasedTip = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/purchased-tips+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/purchased-tips?page=0&size=20>; rel="last",<http://localhost/api/purchased-tips?page=0&size=20>; rel="first"',
              },
              body: [purchasedTip],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(purchasedTipPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(purchasedTipPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PurchasedTip page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('purchasedTip');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedTipPageUrlPattern);
      });

      it('edit button click should load edit PurchasedTip page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedTip');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedTipPageUrlPattern);
      });

      it('edit button click should load edit PurchasedTip page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedTip');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedTipPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of PurchasedTip', () => {
        cy.intercept('GET', '/api/purchased-tips/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('purchasedTip').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedTipPageUrlPattern);

        purchasedTip = undefined;
      });
    });
  });

  describe('new PurchasedTip page', () => {
    beforeEach(() => {
      cy.visit(`${purchasedTipPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PurchasedTip');
    });

    it.skip('should create an instance of PurchasedTip', () => {
      cy.get(`[data-cy="amount"]`).type('21027.12');
      cy.get(`[data-cy="amount"]`).should('have.value', '21027.12');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T01:27');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T01:27');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T04:52');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T04:52');

      cy.get(`[data-cy="createdBy"]`).type('loop');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'loop');

      cy.get(`[data-cy="lastModifiedBy"]`).type('unless produce');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'unless produce');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creatorEarning"]`).select(1);
      cy.get(`[data-cy="message"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        purchasedTip = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', purchasedTipPageUrlPattern);
    });
  });
});
