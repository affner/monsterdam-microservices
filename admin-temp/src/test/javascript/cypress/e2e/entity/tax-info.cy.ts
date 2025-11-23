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

describe('TaxInfo e2e test', () => {
  const taxInfoPageUrl = '/tax-info';
  const taxInfoPageUrlPattern = new RegExp('/tax-info(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const taxInfoSample = { taxType: 'VAT', createdDate: '2024-02-29T04:39:27.740Z', isDeleted: false };

  let taxInfo;
  let country;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/countries',
      body: {
        name: 'rosy',
        alpha2Code: 'ce',
        alpha3Code: 'lik',
        phoneCode: 'round although ultimately',
        thumbnailCountry: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
        thumbnailCountryContentType: 'unknown',
        createdDate: '2024-02-29T03:33:29.632Z',
        lastModifiedDate: '2024-02-29T11:35:38.005Z',
        createdBy: 'woot',
        lastModifiedBy: 'huzzah',
        isDeleted: true,
      },
    }).then(({ body }) => {
      country = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tax-infos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tax-infos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tax-infos/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/countries', {
      statusCode: 200,
      body: [country],
    });
  });

  afterEach(() => {
    if (taxInfo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tax-infos/${taxInfo.id}`,
      }).then(() => {
        taxInfo = undefined;
      });
    }
  });

  afterEach(() => {
    if (country) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/countries/${country.id}`,
      }).then(() => {
        country = undefined;
      });
    }
  });

  it('TaxInfos menu should load TaxInfos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tax-info');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaxInfo').should('exist');
    cy.url().should('match', taxInfoPageUrlPattern);
  });

  describe('TaxInfo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taxInfoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaxInfo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tax-info/new$'));
        cy.getEntityCreateUpdateHeading('TaxInfo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taxInfoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tax-infos',
          body: {
            ...taxInfoSample,
            country: country,
          },
        }).then(({ body }) => {
          taxInfo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tax-infos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tax-infos?page=0&size=20>; rel="last",<http://localhost/api/tax-infos?page=0&size=20>; rel="first"',
              },
              body: [taxInfo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(taxInfoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaxInfo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('taxInfo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taxInfoPageUrlPattern);
      });

      it('edit button click should load edit TaxInfo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxInfo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taxInfoPageUrlPattern);
      });

      it('edit button click should load edit TaxInfo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxInfo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taxInfoPageUrlPattern);
      });

      it('last delete button click should delete instance of TaxInfo', () => {
        cy.intercept('GET', '/api/tax-infos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('taxInfo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', taxInfoPageUrlPattern);

        taxInfo = undefined;
      });
    });
  });

  describe('new TaxInfo page', () => {
    beforeEach(() => {
      cy.visit(`${taxInfoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TaxInfo');
    });

    it('should create an instance of TaxInfo', () => {
      cy.get(`[data-cy="ratePercentage"]`).type('17692.19');
      cy.get(`[data-cy="ratePercentage"]`).should('have.value', '17692.19');

      cy.get(`[data-cy="taxType"]`).select('WITHHOLDING');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T10:58');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T10:58');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T19:57');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T19:57');

      cy.get(`[data-cy="createdBy"]`).type('runway');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'runway');

      cy.get(`[data-cy="lastModifiedBy"]`).type('kite suddenly now');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'kite suddenly now');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="country"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        taxInfo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', taxInfoPageUrlPattern);
    });
  });
});
