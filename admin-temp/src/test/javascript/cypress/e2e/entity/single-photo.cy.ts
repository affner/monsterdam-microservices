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

describe('SinglePhoto e2e test', () => {
  const singlePhotoPageUrl = '/single-photo';
  const singlePhotoPageUrlPattern = new RegExp('/single-photo(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const singlePhotoSample = {
    thumbnail: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    thumbnailContentType: 'unknown',
    thumbnailS3Key: 'nearly vend ouch',
    contentS3Key: 'after',
    createdDate: '2024-02-29T12:29:20.468Z',
    isDeleted: true,
  };

  let singlePhoto;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/single-photos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/single-photos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/single-photos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (singlePhoto) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/single-photos/${singlePhoto.id}`,
      }).then(() => {
        singlePhoto = undefined;
      });
    }
  });

  it('SinglePhotos menu should load SinglePhotos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('single-photo');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SinglePhoto').should('exist');
    cy.url().should('match', singlePhotoPageUrlPattern);
  });

  describe('SinglePhoto page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(singlePhotoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SinglePhoto page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/single-photo/new$'));
        cy.getEntityCreateUpdateHeading('SinglePhoto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singlePhotoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/single-photos',
          body: singlePhotoSample,
        }).then(({ body }) => {
          singlePhoto = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/single-photos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/single-photos?page=0&size=20>; rel="last",<http://localhost/api/single-photos?page=0&size=20>; rel="first"',
              },
              body: [singlePhoto],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(singlePhotoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SinglePhoto page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('singlePhoto');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singlePhotoPageUrlPattern);
      });

      it('edit button click should load edit SinglePhoto page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SinglePhoto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singlePhotoPageUrlPattern);
      });

      it('edit button click should load edit SinglePhoto page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SinglePhoto');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singlePhotoPageUrlPattern);
      });

      it('last delete button click should delete instance of SinglePhoto', () => {
        cy.intercept('GET', '/api/single-photos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('singlePhoto').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singlePhotoPageUrlPattern);

        singlePhoto = undefined;
      });
    });
  });

  describe('new SinglePhoto page', () => {
    beforeEach(() => {
      cy.visit(`${singlePhotoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SinglePhoto');
    });

    it('should create an instance of SinglePhoto', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="thumbnailS3Key"]`).type('whisper');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'whisper');

      cy.setFieldImageAsBytesOfEntity('content', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="contentS3Key"]`).type('carelessly boo');
      cy.get(`[data-cy="contentS3Key"]`).should('have.value', 'carelessly boo');

      cy.get(`[data-cy="likeCount"]`).type('21486');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '21486');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T13:32');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T13:32');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T02:53');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T02:53');

      cy.get(`[data-cy="createdBy"]`).type('meh awful');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'meh awful');

      cy.get(`[data-cy="lastModifiedBy"]`).type('hamburger');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'hamburger');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        singlePhoto = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', singlePhotoPageUrlPattern);
    });
  });
});
