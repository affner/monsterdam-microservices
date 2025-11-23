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

describe('SingleAudio e2e test', () => {
  const singleAudioPageUrl = '/single-audio';
  const singleAudioPageUrlPattern = new RegExp('/single-audio(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const singleAudioSample = {"thumbnail":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","thumbnailContentType":"unknown","thumbnailS3Key":"valuable grieve","contentS3Key":"unless disgusting","createdDate":"2024-02-29T10:16:12.297Z","isDeleted":true};

  let singleAudio;
  // let contentPackage;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/content-packages',
      body: {"amount":19158.36,"videoCount":11358,"imageCount":6572,"isPaidContent":false,"createdDate":"2024-02-29T08:28:30.461Z","lastModifiedDate":"2024-02-29T23:27:29.513Z","createdBy":"gracefully","lastModifiedBy":"calculating ugh mockingly","isDeleted":false},
    }).then(({ body }) => {
      contentPackage = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/single-audios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/single-audios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/single-audios/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-reports', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/content-packages', {
      statusCode: 200,
      body: [contentPackage],
    });

  });
   */

  afterEach(() => {
    if (singleAudio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/single-audios/${singleAudio.id}`,
      }).then(() => {
        singleAudio = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (contentPackage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/content-packages/${contentPackage.id}`,
      }).then(() => {
        contentPackage = undefined;
      });
    }
  });
   */

  it('SingleAudios menu should load SingleAudios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('single-audio');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SingleAudio').should('exist');
    cy.url().should('match', singleAudioPageUrlPattern);
  });

  describe('SingleAudio page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(singleAudioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SingleAudio page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/single-audio/new$'));
        cy.getEntityCreateUpdateHeading('SingleAudio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleAudioPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/single-audios',
          body: {
            ...singleAudioSample,
            contentPackage: contentPackage,
          },
        }).then(({ body }) => {
          singleAudio = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/single-audios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/single-audios?page=0&size=20>; rel="last",<http://localhost/api/single-audios?page=0&size=20>; rel="first"',
              },
              body: [singleAudio],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(singleAudioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(singleAudioPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details SingleAudio page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('singleAudio');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleAudioPageUrlPattern);
      });

      it('edit button click should load edit SingleAudio page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleAudio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleAudioPageUrlPattern);
      });

      it('edit button click should load edit SingleAudio page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleAudio');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleAudioPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of SingleAudio', () => {
        cy.intercept('GET', '/api/single-audios/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('singleAudio').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', singleAudioPageUrlPattern);

        singleAudio = undefined;
      });
    });
  });

  describe('new SingleAudio page', () => {
    beforeEach(() => {
      cy.visit(`${singleAudioPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SingleAudio');
    });

    it.skip('should create an instance of SingleAudio', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="thumbnailS3Key"]`).type('upon in');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'upon in');

      cy.setFieldImageAsBytesOfEntity('content', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="contentS3Key"]`).type('ack hitch');
      cy.get(`[data-cy="contentS3Key"]`).should('have.value', 'ack hitch');

      cy.get(`[data-cy="duration"]`).type('PT44M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT44M');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T18:09');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T18:09');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T11:46');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T11:46');

      cy.get(`[data-cy="createdBy"]`).type('competition likewise');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'competition likewise');

      cy.get(`[data-cy="lastModifiedBy"]`).type('reluctantly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'reluctantly');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="contentPackage"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        singleAudio = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', singleAudioPageUrlPattern);
    });
  });
});
