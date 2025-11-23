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

describe('PaymentMethod e2e test', () => {
  const paymentMethodPageUrl = '/payment-method';
  const paymentMethodPageUrlPattern = new RegExp('/payment-method(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paymentMethodSample = { methodName: 'busily', tokenText: 'apropos', createdDate: '2024-02-29T18:42:27.717Z', isDeleted: true };

  let paymentMethod;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-methods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-methods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-methods/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentMethod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-methods/${paymentMethod.id}`,
      }).then(() => {
        paymentMethod = undefined;
      });
    }
  });

  it('PaymentMethods menu should load PaymentMethods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-method');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentMethod').should('exist');
    cy.url().should('match', paymentMethodPageUrlPattern);
  });

  describe('PaymentMethod page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentMethodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentMethod page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/payment-method/new$'));
        cy.getEntityCreateUpdateHeading('PaymentMethod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentMethodPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-methods',
          body: paymentMethodSample,
        }).then(({ body }) => {
          paymentMethod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-methods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payment-methods?page=0&size=20>; rel="last",<http://localhost/api/payment-methods?page=0&size=20>; rel="first"',
              },
              body: [paymentMethod],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentMethodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentMethod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentMethod');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentMethodPageUrlPattern);
      });

      it('edit button click should load edit PaymentMethod page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentMethod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentMethodPageUrlPattern);
      });

      it('edit button click should load edit PaymentMethod page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentMethod');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentMethodPageUrlPattern);
      });

      it('last delete button click should delete instance of PaymentMethod', () => {
        cy.intercept('GET', '/api/payment-methods/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('paymentMethod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentMethodPageUrlPattern);

        paymentMethod = undefined;
      });
    });
  });

  describe('new PaymentMethod page', () => {
    beforeEach(() => {
      cy.visit(`${paymentMethodPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentMethod');
    });

    it('should create an instance of PaymentMethod', () => {
      cy.get(`[data-cy="methodName"]`).type('poor');
      cy.get(`[data-cy="methodName"]`).should('have.value', 'poor');

      cy.get(`[data-cy="tokenText"]`).type('complement given');
      cy.get(`[data-cy="tokenText"]`).should('have.value', 'complement given');

      cy.get(`[data-cy="expirationDate"]`).type('2024-02-29');
      cy.get(`[data-cy="expirationDate"]`).blur();
      cy.get(`[data-cy="expirationDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T15:48');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T15:48');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T09:25');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T09:25');

      cy.get(`[data-cy="createdBy"]`).type('drat');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'drat');

      cy.get(`[data-cy="lastModifiedBy"]`).type('who');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'who');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paymentMethod = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paymentMethodPageUrlPattern);
    });
  });
});
