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

describe('ModerationAction e2e test', () => {
  const moderationActionPageUrl = '/moderation-action';
  const moderationActionPageUrlPattern = new RegExp('/moderation-action(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const moderationActionSample = { actionType: 'CONTENT_REMOVAL' };

  let moderationAction;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/moderation-actions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/moderation-actions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/moderation-actions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (moderationAction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/moderation-actions/${moderationAction.id}`,
      }).then(() => {
        moderationAction = undefined;
      });
    }
  });

  it('ModerationActions menu should load ModerationActions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('moderation-action');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ModerationAction').should('exist');
    cy.url().should('match', moderationActionPageUrlPattern);
  });

  describe('ModerationAction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(moderationActionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ModerationAction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/moderation-action/new$'));
        cy.getEntityCreateUpdateHeading('ModerationAction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moderationActionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/moderation-actions',
          body: moderationActionSample,
        }).then(({ body }) => {
          moderationAction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/moderation-actions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [moderationAction],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(moderationActionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ModerationAction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('moderationAction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moderationActionPageUrlPattern);
      });

      it('edit button click should load edit ModerationAction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ModerationAction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moderationActionPageUrlPattern);
      });

      it('edit button click should load edit ModerationAction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ModerationAction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moderationActionPageUrlPattern);
      });

      it('last delete button click should delete instance of ModerationAction', () => {
        cy.intercept('GET', '/api/moderation-actions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('moderationAction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moderationActionPageUrlPattern);

        moderationAction = undefined;
      });
    });
  });

  describe('new ModerationAction page', () => {
    beforeEach(() => {
      cy.visit(`${moderationActionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ModerationAction');
    });

    it('should create an instance of ModerationAction', () => {
      cy.get(`[data-cy="actionType"]`).select('CONTENT_REMOVAL');

      cy.get(`[data-cy="reason"]`).type('indeed');
      cy.get(`[data-cy="reason"]`).should('have.value', 'indeed');

      cy.get(`[data-cy="actionDate"]`).type('2024-03-01T23:31');
      cy.get(`[data-cy="actionDate"]`).blur();
      cy.get(`[data-cy="actionDate"]`).should('have.value', '2024-03-01T23:31');

      cy.get(`[data-cy="durationDays"]`).type('PT54M');
      cy.get(`[data-cy="durationDays"]`).blur();
      cy.get(`[data-cy="durationDays"]`).should('have.value', 'PT54M');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        moderationAction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', moderationActionPageUrlPattern);
    });
  });
});
