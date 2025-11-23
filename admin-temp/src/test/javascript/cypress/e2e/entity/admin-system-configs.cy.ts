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

describe('AdminSystemConfigs e2e test', () => {
  const adminSystemConfigsPageUrl = '/admin-system-configs';
  const adminSystemConfigsPageUrlPattern = new RegExp('/admin-system-configs(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const adminSystemConfigsSample = {
    configKey: 'lest amidst often',
    configValue: 'entity disabuse',
    createdDate: '2024-02-29T20:20:50.209Z',
  };

  let adminSystemConfigs;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/admin-system-configs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/admin-system-configs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/admin-system-configs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (adminSystemConfigs) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/admin-system-configs/${adminSystemConfigs.id}`,
      }).then(() => {
        adminSystemConfigs = undefined;
      });
    }
  });

  it('AdminSystemConfigs menu should load AdminSystemConfigs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('admin-system-configs');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AdminSystemConfigs').should('exist');
    cy.url().should('match', adminSystemConfigsPageUrlPattern);
  });

  describe('AdminSystemConfigs page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(adminSystemConfigsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AdminSystemConfigs page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/admin-system-configs/new$'));
        cy.getEntityCreateUpdateHeading('AdminSystemConfigs');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminSystemConfigsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/admin-system-configs',
          body: adminSystemConfigsSample,
        }).then(({ body }) => {
          adminSystemConfigs = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/admin-system-configs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/admin-system-configs?page=0&size=20>; rel="last",<http://localhost/api/admin-system-configs?page=0&size=20>; rel="first"',
              },
              body: [adminSystemConfigs],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(adminSystemConfigsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AdminSystemConfigs page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('adminSystemConfigs');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminSystemConfigsPageUrlPattern);
      });

      it('edit button click should load edit AdminSystemConfigs page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AdminSystemConfigs');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminSystemConfigsPageUrlPattern);
      });

      it('edit button click should load edit AdminSystemConfigs page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AdminSystemConfigs');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminSystemConfigsPageUrlPattern);
      });

      it('last delete button click should delete instance of AdminSystemConfigs', () => {
        cy.intercept('GET', '/api/admin-system-configs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('adminSystemConfigs').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminSystemConfigsPageUrlPattern);

        adminSystemConfigs = undefined;
      });
    });
  });

  describe('new AdminSystemConfigs page', () => {
    beforeEach(() => {
      cy.visit(`${adminSystemConfigsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AdminSystemConfigs');
    });

    it('should create an instance of AdminSystemConfigs', () => {
      cy.get(`[data-cy="configKey"]`).type('exonerate');
      cy.get(`[data-cy="configKey"]`).should('have.value', 'exonerate');

      cy.get(`[data-cy="configValue"]`).type('happily provided favorite');
      cy.get(`[data-cy="configValue"]`).should('have.value', 'happily provided favorite');

      cy.get(`[data-cy="description"]`).type('slope chicken');
      cy.get(`[data-cy="description"]`).should('have.value', 'slope chicken');

      cy.get(`[data-cy="configValueType"]`).select('BOOLEAN');

      cy.get(`[data-cy="configCategory"]`).select('NOTIFICATIONS');

      cy.setFieldImageAsBytesOfEntity('configFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T17:31');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T17:31');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T13:07');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T13:07');

      cy.get(`[data-cy="createdBy"]`).type('silver');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'silver');

      cy.get(`[data-cy="lastModifiedBy"]`).type('punctually');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'punctually');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        adminSystemConfigs = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', adminSystemConfigsPageUrlPattern);
    });
  });
});
