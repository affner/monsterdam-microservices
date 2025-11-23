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

describe('SingleDocument e2e test', () => {
  const singleDocumentPageUrl = '/single-document';
  const singleDocumentPageUrlPattern = new RegExp('/single-document(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const singleDocumentSample = {"title":"whenever","documentFile":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","documentFileContentType":"unknown","documentFileS3Key":"phooey next","createdDate":"2024-02-29T09:43:56.089Z","isDeleted":true};

  let singleDocument;
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
      body: {"emailContact":"b","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"wetly vacuum","coverPhotoS3Key":"puzzling","mainContentUrl":"reasoning who","mobilePhone":"911050030356191","websiteUrl":"f@GLgw.d~Mr0C","amazonWishlistUrl":"l1??@#K}.}`Db","lastLoginDate":"2024-02-29T14:29:00.046Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T02:35:14.736Z","lastModifiedDate":"2024-02-29T04:18:39.613Z","createdBy":"handmade splosh","lastModifiedBy":"fund","isDeleted":true},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/single-documents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/single-documents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/single-documents/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

  });
   */

  afterEach(() => {
    if (singleDocument) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/single-documents/${singleDocument.id}`,
      }).then(() => {
        singleDocument = undefined;
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

  it('SingleDocuments menu should load SingleDocuments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('single-document');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SingleDocument').should('exist');
    cy.url().should('match', singleDocumentPageUrlPattern);
  });

  describe('SingleDocument page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(singleDocumentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SingleDocument page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/single-document/new$'));
        cy.getEntityCreateUpdateHeading('SingleDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleDocumentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/single-documents',
          body: {
            ...singleDocumentSample,
            user: userProfile,
          },
        }).then(({ body }) => {
          singleDocument = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/single-documents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/single-documents?page=0&size=20>; rel="last",<http://localhost/api/single-documents?page=0&size=20>; rel="first"',
              },
              body: [singleDocument],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(singleDocumentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(singleDocumentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details SingleDocument page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('singleDocument');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleDocumentPageUrlPattern);
      });

      it('edit button click should load edit SingleDocument page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleDocumentPageUrlPattern);
      });

      it('edit button click should load edit SingleDocument page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleDocument');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleDocumentPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of SingleDocument', () => {
        cy.intercept('GET', '/api/single-documents/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('singleDocument').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleDocumentPageUrlPattern);

        singleDocument = undefined;
      });
    });
  });

  describe('new SingleDocument page', () => {
    beforeEach(() => {
      cy.visit(`${singleDocumentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SingleDocument');
    });

    it.skip('should create an instance of SingleDocument', () => {
      cy.get(`[data-cy="title"]`).type('remould');
      cy.get(`[data-cy="title"]`).should('have.value', 'remould');

      cy.get(`[data-cy="description"]`).type('anenst fudge phew');
      cy.get(`[data-cy="description"]`).should('have.value', 'anenst fudge phew');

      cy.setFieldImageAsBytesOfEntity('documentFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="documentFileS3Key"]`).type('deride where quizzically');
      cy.get(`[data-cy="documentFileS3Key"]`).should('have.value', 'deride where quizzically');

      cy.get(`[data-cy="documentType"]`).type('softball which');
      cy.get(`[data-cy="documentType"]`).should('have.value', 'softball which');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T17:28');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T17:28');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T08:07');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T08:07');

      cy.get(`[data-cy="createdBy"]`).type('once for ick');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'once for ick');

      cy.get(`[data-cy="lastModifiedBy"]`).type('fooey');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'fooey');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="user"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        singleDocument = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', singleDocumentPageUrlPattern);
    });
  });
});
