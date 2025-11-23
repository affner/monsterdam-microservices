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

describe('PollOption e2e test', () => {
  const pollOptionPageUrl = '/poll-option';
  const pollOptionPageUrlPattern = new RegExp('/poll-option(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const pollOptionSample = {"optionDescription":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","voteCount":4492};

  let pollOption;
  // let postPoll;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/post-polls',
      body: {"question":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isMultiChoice":false,"lastModifiedDate":"2024-02-29T22:21:54.022Z","endDate":"2024-02-29","postPollDuration":5764,"createdDate":"2024-02-29T12:01:21.975Z","createdBy":"where painfully incidentally","lastModifiedBy":"quirky concerning gag","isDeleted":false},
    }).then(({ body }) => {
      postPoll = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/poll-options+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/poll-options').as('postEntityRequest');
    cy.intercept('DELETE', '/api/poll-options/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/post-polls', {
      statusCode: 200,
      body: [postPoll],
    });

    cy.intercept('GET', '/api/poll-votes', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (pollOption) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/poll-options/${pollOption.id}`,
      }).then(() => {
        pollOption = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('PollOptions menu should load PollOptions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('poll-option');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PollOption').should('exist');
    cy.url().should('match', pollOptionPageUrlPattern);
  });

  describe('PollOption page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pollOptionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PollOption page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/poll-option/new$'));
        cy.getEntityCreateUpdateHeading('PollOption');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pollOptionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/poll-options',
          body: {
            ...pollOptionSample,
            poll: postPoll,
          },
        }).then(({ body }) => {
          pollOption = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/poll-options+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/poll-options?page=0&size=20>; rel="last",<http://localhost/api/poll-options?page=0&size=20>; rel="first"',
              },
              body: [pollOption],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(pollOptionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(pollOptionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PollOption page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pollOption');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pollOptionPageUrlPattern);
      });

      it('edit button click should load edit PollOption page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PollOption');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pollOptionPageUrlPattern);
      });

      it('edit button click should load edit PollOption page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PollOption');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pollOptionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of PollOption', () => {
        cy.intercept('GET', '/api/poll-options/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('pollOption').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pollOptionPageUrlPattern);

        pollOption = undefined;
      });
    });
  });

  describe('new PollOption page', () => {
    beforeEach(() => {
      cy.visit(`${pollOptionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PollOption');
    });

    it.skip('should create an instance of PollOption', () => {
      cy.get(`[data-cy="optionDescription"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="optionDescription"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="voteCount"]`).type('26664');
      cy.get(`[data-cy="voteCount"]`).should('have.value', '26664');

      cy.get(`[data-cy="poll"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        pollOption = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', pollOptionPageUrlPattern);
    });
  });
});
