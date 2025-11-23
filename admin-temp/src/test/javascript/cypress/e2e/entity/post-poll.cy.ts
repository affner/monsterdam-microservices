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

describe('PostPoll e2e test', () => {
  const postPollPageUrl = '/post-poll';
  const postPollPageUrlPattern = new RegExp('/post-poll(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const postPollSample = {"question":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isMultiChoice":false,"endDate":"2024-02-29","postPollDuration":643,"createdDate":"2024-02-29T02:03:06.539Z","isDeleted":false};

  let postPoll;
  // let postFeed;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/post-feeds',
      body: {"postContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isHidden":true,"pinnedPost":false,"likeCount":13245,"createdDate":"2024-02-29T13:54:23.611Z","lastModifiedDate":"2024-02-29T21:49:55.834Z","createdBy":"tangle","lastModifiedBy":"whose despite","isDeleted":true},
    }).then(({ body }) => {
      postFeed = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/post-polls+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-polls').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-polls/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/poll-options', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/post-feeds', {
      statusCode: 200,
      body: [postFeed],
    });

  });
   */

  afterEach(() => {
    if (postPoll) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-polls/${postPoll.id}`,
      }).then(() => {
        postPoll = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (postFeed) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-feeds/${postFeed.id}`,
      }).then(() => {
        postFeed = undefined;
      });
    }
  });
   */

  it('PostPolls menu should load PostPolls page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('post-poll');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PostPoll').should('exist');
    cy.url().should('match', postPollPageUrlPattern);
  });

  describe('PostPoll page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postPollPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PostPoll page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/post-poll/new$'));
        cy.getEntityCreateUpdateHeading('PostPoll');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postPollPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/post-polls',
          body: {
            ...postPollSample,
            post: postFeed,
          },
        }).then(({ body }) => {
          postPoll = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/post-polls+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/post-polls?page=0&size=20>; rel="last",<http://localhost/api/post-polls?page=0&size=20>; rel="first"',
              },
              body: [postPoll],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(postPollPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(postPollPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PostPoll page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('postPoll');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postPollPageUrlPattern);
      });

      it('edit button click should load edit PostPoll page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostPoll');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postPollPageUrlPattern);
      });

      it('edit button click should load edit PostPoll page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostPoll');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postPollPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of PostPoll', () => {
        cy.intercept('GET', '/api/post-polls/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('postPoll').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', postPollPageUrlPattern);

        postPoll = undefined;
      });
    });
  });

  describe('new PostPoll page', () => {
    beforeEach(() => {
      cy.visit(`${postPollPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PostPoll');
    });

    it.skip('should create an instance of PostPoll', () => {
      cy.get(`[data-cy="question"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="question"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isMultiChoice"]`).should('not.be.checked');
      cy.get(`[data-cy="isMultiChoice"]`).click();
      cy.get(`[data-cy="isMultiChoice"]`).should('be.checked');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T16:47');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T16:47');

      cy.get(`[data-cy="endDate"]`).type('2024-02-29');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="postPollDuration"]`).type('PT20M');
      cy.get(`[data-cy="postPollDuration"]`).blur();
      cy.get(`[data-cy="postPollDuration"]`).should('have.value', 'PT20M');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T11:59');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T11:59');

      cy.get(`[data-cy="createdBy"]`).type('dapper prelude because');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'dapper prelude because');

      cy.get(`[data-cy="lastModifiedBy"]`).type('commonly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'commonly');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="post"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        postPoll = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', postPollPageUrlPattern);
    });
  });
});
