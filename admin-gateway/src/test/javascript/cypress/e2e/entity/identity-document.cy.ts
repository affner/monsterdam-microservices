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

describe('IdentityDocument e2e test', () => {
  const identityDocumentPageUrl = '/identity-document';
  const identityDocumentPageUrlPattern = new RegExp('/identity-document(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const identityDocumentSample = {
    documentName: 'phooey',
    fileDocument: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    fileDocumentContentType: 'unknown',
    fileDocumentS3Key: 'atop',
    createdDate: '2024-03-01T18:57:27.587Z',
  };

  let identityDocument;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/identity-documents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/identity-documents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/identity-documents/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (identityDocument) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/identity-documents/${identityDocument.id}`,
      }).then(() => {
        identityDocument = undefined;
      });
    }
  });

  it('IdentityDocuments menu should load IdentityDocuments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('identity-document');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IdentityDocument').should('exist');
    cy.url().should('match', identityDocumentPageUrlPattern);
  });

  describe('IdentityDocument page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(identityDocumentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IdentityDocument page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/identity-document/new$'));
        cy.getEntityCreateUpdateHeading('IdentityDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/identity-documents',
          body: identityDocumentSample,
        }).then(({ body }) => {
          identityDocument = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/identity-documents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [identityDocument],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(identityDocumentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IdentityDocument page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('identityDocument');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentPageUrlPattern);
      });

      it('edit button click should load edit IdentityDocument page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentityDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentPageUrlPattern);
      });

      it('edit button click should load edit IdentityDocument page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentityDocument');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentPageUrlPattern);
      });

      it('last delete button click should delete instance of IdentityDocument', () => {
        cy.intercept('GET', '/api/identity-documents/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('identityDocument').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentPageUrlPattern);

        identityDocument = undefined;
      });
    });
  });

  describe('new IdentityDocument page', () => {
    beforeEach(() => {
      cy.visit(`${identityDocumentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IdentityDocument');
    });

    it('should create an instance of IdentityDocument', () => {
      cy.get(`[data-cy="documentName"]`).type('rotten');
      cy.get(`[data-cy="documentName"]`).should('have.value', 'rotten');

      cy.get(`[data-cy="documentDescription"]`).type('gee eminent');
      cy.get(`[data-cy="documentDescription"]`).should('have.value', 'gee eminent');

      cy.get(`[data-cy="documentStatus"]`).select('PENDING');

      cy.get(`[data-cy="documentType"]`).select('ID_VERIFICATION');

      cy.setFieldImageAsBytesOfEntity('fileDocument', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="fileDocumentS3Key"]`).type('inferior graceful');
      cy.get(`[data-cy="fileDocumentS3Key"]`).should('have.value', 'inferior graceful');

      cy.get(`[data-cy="createdDate"]`).type('2024-03-02T13:31');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-03-02T13:31');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-03-02T05:23');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-03-02T05:23');

      cy.get(`[data-cy="createdBy"]`).type('channel');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'channel');

      cy.get(`[data-cy="lastModifiedBy"]`).type('duh huzzah');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'duh huzzah');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        identityDocument = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', identityDocumentPageUrlPattern);
    });
  });
});
