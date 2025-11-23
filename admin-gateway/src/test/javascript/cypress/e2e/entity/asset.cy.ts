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

describe('Asset e2e test', () => {
  const assetPageUrl = '/asset';
  const assetPageUrlPattern = new RegExp('/asset(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assetSample = { name: 'whoa', value: 10512.06, type: 'FIXED' };

  let asset;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/assets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/assets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/assets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (asset) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assets/${asset.id}`,
      }).then(() => {
        asset = undefined;
      });
    }
  });

  it('Assets menu should load Assets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Asset').should('exist');
    cy.url().should('match', assetPageUrlPattern);
  });

  describe('Asset page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Asset page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/asset/new$'));
        cy.getEntityCreateUpdateHeading('Asset');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/assets',
          body: assetSample,
        }).then(({ body }) => {
          asset = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/assets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [asset],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Asset page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('asset');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetPageUrlPattern);
      });

      it('edit button click should load edit Asset page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Asset');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetPageUrlPattern);
      });

      it('edit button click should load edit Asset page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Asset');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetPageUrlPattern);
      });

      it('last delete button click should delete instance of Asset', () => {
        cy.intercept('GET', '/api/assets/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('asset').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assetPageUrlPattern);

        asset = undefined;
      });
    });
  });

  describe('new Asset page', () => {
    beforeEach(() => {
      cy.visit(`${assetPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Asset');
    });

    it('should create an instance of Asset', () => {
      cy.get(`[data-cy="name"]`).type('finally');
      cy.get(`[data-cy="name"]`).should('have.value', 'finally');

      cy.get(`[data-cy="value"]`).type('8412.87');
      cy.get(`[data-cy="value"]`).should('have.value', '8412.87');

      cy.get(`[data-cy="acquisitionDate"]`).type('2024-03-02');
      cy.get(`[data-cy="acquisitionDate"]`).blur();
      cy.get(`[data-cy="acquisitionDate"]`).should('have.value', '2024-03-02');

      cy.get(`[data-cy="type"]`).select('INTANGIBLE');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        asset = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', assetPageUrlPattern);
    });
  });
});
