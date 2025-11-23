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

describe('SingleVideo e2e test', () => {
  const singleVideoPageUrl = '/single-video';
  const singleVideoPageUrlPattern = new RegExp('/single-video(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const singleVideoSample = {
    thumbnail: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    thumbnailContentType: 'unknown',
    thumbnailS3Key: 'as',
    contentS3Key: 'shelter above abnormally',
    createdDate: '2024-02-29T22:41:00.728Z',
    isDeleted: true,
  };

  let singleVideo;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/single-videos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/single-videos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/single-videos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (singleVideo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/single-videos/${singleVideo.id}`,
      }).then(() => {
        singleVideo = undefined;
      });
    }
  });

  it('SingleVideos menu should load SingleVideos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('single-video');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SingleVideo').should('exist');
    cy.url().should('match', singleVideoPageUrlPattern);
  });

  describe('SingleVideo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(singleVideoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SingleVideo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/single-video/new$'));
        cy.getEntityCreateUpdateHeading('SingleVideo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/single-videos',
          body: singleVideoSample,
        }).then(({ body }) => {
          singleVideo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/single-videos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/single-videos?page=0&size=20>; rel="last",<http://localhost/api/single-videos?page=0&size=20>; rel="first"',
              },
              body: [singleVideo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(singleVideoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SingleVideo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('singleVideo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });

      it('edit button click should load edit SingleVideo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleVideo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });

      it('edit button click should load edit SingleVideo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleVideo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });

      it('last delete button click should delete instance of SingleVideo', () => {
        cy.intercept('GET', '/api/single-videos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('singleVideo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);

        singleVideo = undefined;
      });
    });
  });

  describe('new SingleVideo page', () => {
    beforeEach(() => {
      cy.visit(`${singleVideoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SingleVideo');
    });

    it('should create an instance of SingleVideo', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="thumbnailS3Key"]`).type('authentication slot balance');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'authentication slot balance');

      cy.setFieldImageAsBytesOfEntity('content', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="contentS3Key"]`).type('feline');
      cy.get(`[data-cy="contentS3Key"]`).should('have.value', 'feline');

      cy.get(`[data-cy="duration"]`).type('PT33M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT33M');

      cy.get(`[data-cy="likeCount"]`).type('25847');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '25847');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T07:04');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T07:04');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T05:17');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T05:17');

      cy.get(`[data-cy="createdBy"]`).type('detour valiantly except');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'detour valiantly except');

      cy.get(`[data-cy="lastModifiedBy"]`).type('banquette how rage');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'banquette how rage');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        singleVideo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', singleVideoPageUrlPattern);
    });
  });
});
