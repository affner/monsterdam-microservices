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

describe('PaymentProvider e2e test', () => {
  const paymentProviderPageUrl = '/payment-provider';
  const paymentProviderPageUrlPattern = new RegExp('/payment-provider(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paymentProviderSample = {
    providerName: 'institutionalize that',
    apiKeyText: 'even',
    apiSecretText: 'neatly',
    endpointText: 'suit shrilly enlightened',
    createdDate: '2024-02-29T12:05:09.987Z',
    isDeleted: true,
  };

  let paymentProvider;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-providers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-providers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-providers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentProvider) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-providers/${paymentProvider.id}`,
      }).then(() => {
        paymentProvider = undefined;
      });
    }
  });

  it('PaymentProviders menu should load PaymentProviders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-provider');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentProvider').should('exist');
    cy.url().should('match', paymentProviderPageUrlPattern);
  });

  describe('PaymentProvider page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentProviderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentProvider page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/payment-provider/new$'));
        cy.getEntityCreateUpdateHeading('PaymentProvider');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentProviderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-providers',
          body: paymentProviderSample,
        }).then(({ body }) => {
          paymentProvider = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-providers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payment-providers?page=0&size=20>; rel="last",<http://localhost/api/payment-providers?page=0&size=20>; rel="first"',
              },
              body: [paymentProvider],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentProviderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentProvider page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentProvider');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentProviderPageUrlPattern);
      });

      it('edit button click should load edit PaymentProvider page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentProvider');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentProviderPageUrlPattern);
      });

      it('edit button click should load edit PaymentProvider page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentProvider');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentProviderPageUrlPattern);
      });

      it('last delete button click should delete instance of PaymentProvider', () => {
        cy.intercept('GET', '/api/payment-providers/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('paymentProvider').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentProviderPageUrlPattern);

        paymentProvider = undefined;
      });
    });
  });

  describe('new PaymentProvider page', () => {
    beforeEach(() => {
      cy.visit(`${paymentProviderPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentProvider');
    });

    it('should create an instance of PaymentProvider', () => {
      cy.get(`[data-cy="providerName"]`).type('dislike');
      cy.get(`[data-cy="providerName"]`).should('have.value', 'dislike');

      cy.get(`[data-cy="description"]`).type('gadzooks');
      cy.get(`[data-cy="description"]`).should('have.value', 'gadzooks');

      cy.get(`[data-cy="apiKeyText"]`).type('liquid');
      cy.get(`[data-cy="apiKeyText"]`).should('have.value', 'liquid');

      cy.get(`[data-cy="apiSecretText"]`).type('though big-hearted');
      cy.get(`[data-cy="apiSecretText"]`).should('have.value', 'though big-hearted');

      cy.get(`[data-cy="endpointText"]`).type('speedy sun physically');
      cy.get(`[data-cy="endpointText"]`).should('have.value', 'speedy sun physically');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T23:36');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T23:36');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T05:21');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T05:21');

      cy.get(`[data-cy="createdBy"]`).type('gee thatch slash');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'gee thatch slash');

      cy.get(`[data-cy="lastModifiedBy"]`).type('band');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'band');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paymentProvider = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paymentProviderPageUrlPattern);
    });
  });
});
