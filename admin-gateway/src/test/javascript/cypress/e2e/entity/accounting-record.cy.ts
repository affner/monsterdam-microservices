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

describe('AccountingRecord e2e test', () => {
  const accountingRecordPageUrl = '/accounting-record';
  const accountingRecordPageUrlPattern = new RegExp('/accounting-record(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const accountingRecordSample = {
    date: '2024-03-02T09:18:51.455Z',
    description: 'limited geez',
    balance: 32105.25,
    accountType: 'LIABILITY',
  };

  let accountingRecord;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/accounting-records+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/accounting-records').as('postEntityRequest');
    cy.intercept('DELETE', '/api/accounting-records/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accountingRecord) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/accounting-records/${accountingRecord.id}`,
      }).then(() => {
        accountingRecord = undefined;
      });
    }
  });

  it('AccountingRecords menu should load AccountingRecords page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('accounting-record');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountingRecord').should('exist');
    cy.url().should('match', accountingRecordPageUrlPattern);
  });

  describe('AccountingRecord page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountingRecordPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountingRecord page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/accounting-record/new$'));
        cy.getEntityCreateUpdateHeading('AccountingRecord');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingRecordPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/accounting-records',
          body: accountingRecordSample,
        }).then(({ body }) => {
          accountingRecord = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/accounting-records+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [accountingRecord],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountingRecordPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountingRecord page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountingRecord');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingRecordPageUrlPattern);
      });

      it('edit button click should load edit AccountingRecord page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountingRecord');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingRecordPageUrlPattern);
      });

      it('edit button click should load edit AccountingRecord page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountingRecord');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingRecordPageUrlPattern);
      });

      it('last delete button click should delete instance of AccountingRecord', () => {
        cy.intercept('GET', '/api/accounting-records/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('accountingRecord').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingRecordPageUrlPattern);

        accountingRecord = undefined;
      });
    });
  });

  describe('new AccountingRecord page', () => {
    beforeEach(() => {
      cy.visit(`${accountingRecordPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AccountingRecord');
    });

    it('should create an instance of AccountingRecord', () => {
      cy.get(`[data-cy="date"]`).type('2024-03-02T04:19');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-03-02T04:19');

      cy.get(`[data-cy="description"]`).type('woot bustle bury');
      cy.get(`[data-cy="description"]`).should('have.value', 'woot bustle bury');

      cy.get(`[data-cy="debit"]`).type('23143.06');
      cy.get(`[data-cy="debit"]`).should('have.value', '23143.06');

      cy.get(`[data-cy="credit"]`).type('15098.8');
      cy.get(`[data-cy="credit"]`).should('have.value', '15098.8');

      cy.get(`[data-cy="balance"]`).type('18706.59');
      cy.get(`[data-cy="balance"]`).should('have.value', '18706.59');

      cy.get(`[data-cy="accountType"]`).select('ASSET');

      cy.get(`[data-cy="paymentId"]`).type('21786');
      cy.get(`[data-cy="paymentId"]`).should('have.value', '21786');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        accountingRecord = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', accountingRecordPageUrlPattern);
    });
  });
});
