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

describe('HelpQuestion e2e test', () => {
  const helpQuestionPageUrl = '/help-question';
  const helpQuestionPageUrlPattern = new RegExp('/help-question(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const helpQuestionSample = { title: 'gah inwardly mammoth', content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', isDeleted: true };

  let helpQuestion;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/help-questions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/help-questions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/help-questions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (helpQuestion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/help-questions/${helpQuestion.id}`,
      }).then(() => {
        helpQuestion = undefined;
      });
    }
  });

  it('HelpQuestions menu should load HelpQuestions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('help-question');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HelpQuestion').should('exist');
    cy.url().should('match', helpQuestionPageUrlPattern);
  });

  describe('HelpQuestion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(helpQuestionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HelpQuestion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/help-question/new$'));
        cy.getEntityCreateUpdateHeading('HelpQuestion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpQuestionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/help-questions',
          body: helpQuestionSample,
        }).then(({ body }) => {
          helpQuestion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/help-questions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/help-questions?page=0&size=20>; rel="last",<http://localhost/api/help-questions?page=0&size=20>; rel="first"',
              },
              body: [helpQuestion],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(helpQuestionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HelpQuestion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('helpQuestion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpQuestionPageUrlPattern);
      });

      it('edit button click should load edit HelpQuestion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HelpQuestion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpQuestionPageUrlPattern);
      });

      it('edit button click should load edit HelpQuestion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HelpQuestion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpQuestionPageUrlPattern);
      });

      it('last delete button click should delete instance of HelpQuestion', () => {
        cy.intercept('GET', '/api/help-questions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('helpQuestion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpQuestionPageUrlPattern);

        helpQuestion = undefined;
      });
    });
  });

  describe('new HelpQuestion page', () => {
    beforeEach(() => {
      cy.visit(`${helpQuestionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HelpQuestion');
    });

    it('should create an instance of HelpQuestion', () => {
      cy.get(`[data-cy="title"]`).type('pace quizzically');
      cy.get(`[data-cy="title"]`).should('have.value', 'pace quizzically');

      cy.get(`[data-cy="content"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="content"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        helpQuestion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', helpQuestionPageUrlPattern);
    });
  });
});
