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

describe('Feedback e2e test', () => {
  const feedbackPageUrl = '/feedback';
  const feedbackPageUrlPattern = new RegExp('/feedback(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const feedbackSample = {"content":"who","feedbackDate":"2024-02-29T08:50:28.382Z","createdDate":"2024-02-29T13:17:52.723Z","isDeleted":true};

  let feedback;
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
      body: {"emailContact":"yw40bp","profilePhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","profilePhotoContentType":"unknown","coverPhoto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","coverPhotoContentType":"unknown","profilePhotoS3Key":"team divine usefully","coverPhotoS3Key":"warlock aha","mainContentUrl":"popular","mobilePhone":"69320627616562","websiteUrl":"K#@2.,","amazonWishlistUrl":"EWQ;=@%8g.&./'[p","lastLoginDate":"2024-02-29T12:58:47.373Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2024-02-29T16:12:27.845Z","lastModifiedDate":"2024-02-29T10:52:58.250Z","createdBy":"valiantly and teammate","lastModifiedBy":"granular rationale","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/feedbacks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/feedbacks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/feedbacks/*').as('deleteEntityRequest');
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
    if (feedback) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/feedbacks/${feedback.id}`,
      }).then(() => {
        feedback = undefined;
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

  it('Feedbacks menu should load Feedbacks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Feedback').should('exist');
    cy.url().should('match', feedbackPageUrlPattern);
  });

  describe('Feedback page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(feedbackPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Feedback page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/feedback/new$'));
        cy.getEntityCreateUpdateHeading('Feedback');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/feedbacks',
          body: {
            ...feedbackSample,
            creator: userProfile,
          },
        }).then(({ body }) => {
          feedback = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/feedbacks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/feedbacks?page=0&size=20>; rel="last",<http://localhost/api/feedbacks?page=0&size=20>; rel="first"',
              },
              body: [feedback],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(feedbackPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(feedbackPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Feedback page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('feedback');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackPageUrlPattern);
      });

      it('edit button click should load edit Feedback page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Feedback');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackPageUrlPattern);
      });

      it('edit button click should load edit Feedback page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Feedback');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Feedback', () => {
        cy.intercept('GET', '/api/feedbacks/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('feedback').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackPageUrlPattern);

        feedback = undefined;
      });
    });
  });

  describe('new Feedback page', () => {
    beforeEach(() => {
      cy.visit(`${feedbackPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Feedback');
    });

    it.skip('should create an instance of Feedback', () => {
      cy.get(`[data-cy="content"]`).type('gee');
      cy.get(`[data-cy="content"]`).should('have.value', 'gee');

      cy.get(`[data-cy="feedbackDate"]`).type('2024-02-29T04:49');
      cy.get(`[data-cy="feedbackDate"]`).blur();
      cy.get(`[data-cy="feedbackDate"]`).should('have.value', '2024-02-29T04:49');

      cy.get(`[data-cy="feedbackRating"]`).type('12114');
      cy.get(`[data-cy="feedbackRating"]`).should('have.value', '12114');

      cy.get(`[data-cy="feedbackType"]`).select('ERROR');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T09:24');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T09:24');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T23:32');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T23:32');

      cy.get(`[data-cy="createdBy"]`).type('from lone');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'from lone');

      cy.get(`[data-cy="lastModifiedBy"]`).type('speedily incidentally curse');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'speedily incidentally curse');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creator"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        feedback = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', feedbackPageUrlPattern);
    });
  });
});
