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

describe('VideoStory e2e test', () => {
  const videoStoryPageUrl = '/video-story';
  const videoStoryPageUrlPattern = new RegExp('/video-story(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const videoStorySample = {"thumbnail":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","thumbnailContentType":"unknown","thumbnailS3Key":"across","contentS3Key":"hm oh","createdDate":"2024-02-29T15:35:26.768Z","isDeleted":true};

  let videoStory;
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
      body: {"emailContact":"l","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"from aw defender","coverPhotoS3Key":"hence hm","mainContentUrl":"brr what foray","mobilePhone":"+382023441977","websiteUrl":".$@0!\\.kf","amazonWishlistUrl":"E@T.?","lastLoginDate":"2024-02-29T20:56:21.859Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T06:16:29.238Z","lastModifiedDate":"2024-02-29T20:03:37.681Z","createdBy":"anxiously out","lastModifiedBy":"oh aside bleakly","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/video-stories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/video-stories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/video-stories/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-reports', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/direct-messages', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

  });
   */

  afterEach(() => {
    if (videoStory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/video-stories/${videoStory.id}`,
      }).then(() => {
        videoStory = undefined;
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

  it('VideoStories menu should load VideoStories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('video-story');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VideoStory').should('exist');
    cy.url().should('match', videoStoryPageUrlPattern);
  });

  describe('VideoStory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(videoStoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VideoStory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/video-story/new$'));
        cy.getEntityCreateUpdateHeading('VideoStory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', videoStoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/video-stories',
          body: {
            ...videoStorySample,
            creator: userProfile,
          },
        }).then(({ body }) => {
          videoStory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/video-stories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/video-stories?page=0&size=20>; rel="last",<http://localhost/api/video-stories?page=0&size=20>; rel="first"',
              },
              body: [videoStory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(videoStoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(videoStoryPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details VideoStory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('videoStory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', videoStoryPageUrlPattern);
      });

      it('edit button click should load edit VideoStory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VideoStory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', videoStoryPageUrlPattern);
      });

      it('edit button click should load edit VideoStory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VideoStory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', videoStoryPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of VideoStory', () => {
        cy.intercept('GET', '/api/video-stories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('videoStory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', videoStoryPageUrlPattern);

        videoStory = undefined;
      });
    });
  });

  describe('new VideoStory page', () => {
    beforeEach(() => {
      cy.visit(`${videoStoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VideoStory');
    });

    it.skip('should create an instance of VideoStory', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="thumbnailS3Key"]`).type('uh-huh sultan');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'uh-huh sultan');

      cy.setFieldImageAsBytesOfEntity('content', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="contentS3Key"]`).type('even uh-huh');
      cy.get(`[data-cy="contentS3Key"]`).should('have.value', 'even uh-huh');

      cy.get(`[data-cy="duration"]`).type('PT16M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT16M');

      cy.get(`[data-cy="likeCount"]`).type('25142');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '25142');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T08:42');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T08:42');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T11:12');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T11:12');

      cy.get(`[data-cy="createdBy"]`).type('huzzah impractical');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'huzzah impractical');

      cy.get(`[data-cy="lastModifiedBy"]`).type('only redound');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'only redound');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creator"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        videoStory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', videoStoryPageUrlPattern);
    });
  });
});
