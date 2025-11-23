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

describe('HelpRelatedArticle e2e test', () => {
  const helpRelatedArticlePageUrl = '/help-related-article';
  const helpRelatedArticlePageUrlPattern = new RegExp('/help-related-article(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const helpRelatedArticleSample = { title: 'via', content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let helpRelatedArticle;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/help-related-articles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/help-related-articles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/help-related-articles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (helpRelatedArticle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/help-related-articles/${helpRelatedArticle.id}`,
      }).then(() => {
        helpRelatedArticle = undefined;
      });
    }
  });

  it('HelpRelatedArticles menu should load HelpRelatedArticles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('help-related-article');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HelpRelatedArticle').should('exist');
    cy.url().should('match', helpRelatedArticlePageUrlPattern);
  });

  describe('HelpRelatedArticle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(helpRelatedArticlePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HelpRelatedArticle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/help-related-article/new$'));
        cy.getEntityCreateUpdateHeading('HelpRelatedArticle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpRelatedArticlePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/help-related-articles',
          body: helpRelatedArticleSample,
        }).then(({ body }) => {
          helpRelatedArticle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/help-related-articles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/help-related-articles?page=0&size=20>; rel="last",<http://localhost/api/help-related-articles?page=0&size=20>; rel="first"',
              },
              body: [helpRelatedArticle],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(helpRelatedArticlePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HelpRelatedArticle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('helpRelatedArticle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpRelatedArticlePageUrlPattern);
      });

      it('edit button click should load edit HelpRelatedArticle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HelpRelatedArticle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpRelatedArticlePageUrlPattern);
      });

      it('edit button click should load edit HelpRelatedArticle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HelpRelatedArticle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpRelatedArticlePageUrlPattern);
      });

      it('last delete button click should delete instance of HelpRelatedArticle', () => {
        cy.intercept('GET', '/api/help-related-articles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('helpRelatedArticle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', helpRelatedArticlePageUrlPattern);

        helpRelatedArticle = undefined;
      });
    });
  });

  describe('new HelpRelatedArticle page', () => {
    beforeEach(() => {
      cy.visit(`${helpRelatedArticlePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HelpRelatedArticle');
    });

    it('should create an instance of HelpRelatedArticle', () => {
      cy.get(`[data-cy="title"]`).type('but');
      cy.get(`[data-cy="title"]`).should('have.value', 'but');

      cy.get(`[data-cy="content"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="content"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        helpRelatedArticle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', helpRelatedArticlePageUrlPattern);
    });
  });
});
