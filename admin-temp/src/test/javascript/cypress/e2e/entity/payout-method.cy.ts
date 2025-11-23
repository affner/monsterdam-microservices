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

describe('PayoutMethod e2e test', () => {
  const payoutMethodPageUrl = '/payout-method';
  const payoutMethodPageUrlPattern = new RegExp('/payout-method(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const payoutMethodSample = {
    methodName: 'win meh',
    tokenText: 'among meh switch',
    createdDate: '2024-02-29T09:38:30.306Z',
    isDeleted: false,
  };

  let payoutMethod;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payout-methods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payout-methods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payout-methods/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (payoutMethod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payout-methods/${payoutMethod.id}`,
      }).then(() => {
        payoutMethod = undefined;
      });
    }
  });

  it('PayoutMethods menu should load PayoutMethods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payout-method');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PayoutMethod').should('exist');
    cy.url().should('match', payoutMethodPageUrlPattern);
  });

  describe('PayoutMethod page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(payoutMethodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PayoutMethod page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/payout-method/new$'));
        cy.getEntityCreateUpdateHeading('PayoutMethod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', payoutMethodPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payout-methods',
          body: payoutMethodSample,
        }).then(({ body }) => {
          payoutMethod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payout-methods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payout-methods?page=0&size=20>; rel="last",<http://localhost/api/payout-methods?page=0&size=20>; rel="first"',
              },
              body: [payoutMethod],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(payoutMethodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PayoutMethod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('payoutMethod');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', payoutMethodPageUrlPattern);
      });

      it('edit button click should load edit PayoutMethod page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PayoutMethod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', payoutMethodPageUrlPattern);
      });

      it('edit button click should load edit PayoutMethod page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PayoutMethod');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', payoutMethodPageUrlPattern);
      });

      it('last delete button click should delete instance of PayoutMethod', () => {
        cy.intercept('GET', '/api/payout-methods/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('payoutMethod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', payoutMethodPageUrlPattern);

        payoutMethod = undefined;
      });
    });
  });

  describe('new PayoutMethod page', () => {
    beforeEach(() => {
      cy.visit(`${payoutMethodPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PayoutMethod');
    });

    it('should create an instance of PayoutMethod', () => {
      cy.get(`[data-cy="methodName"]`).type('understand');
      cy.get(`[data-cy="methodName"]`).should('have.value', 'understand');

      cy.get(`[data-cy="tokenText"]`).type('than');
      cy.get(`[data-cy="tokenText"]`).should('have.value', 'than');

      cy.get(`[data-cy="expirationDate"]`).type('2024-02-29');
      cy.get(`[data-cy="expirationDate"]`).blur();
      cy.get(`[data-cy="expirationDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T15:39');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T15:39');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T12:45');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T12:45');

      cy.get(`[data-cy="createdBy"]`).type('encircle');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'encircle');

      cy.get(`[data-cy="lastModifiedBy"]`).type('unlike aw vegetarian');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'unlike aw vegetarian');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        payoutMethod = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', payoutMethodPageUrlPattern);
    });
  });
});
