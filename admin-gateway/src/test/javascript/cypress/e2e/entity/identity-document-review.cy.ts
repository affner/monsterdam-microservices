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

describe('IdentityDocumentReview e2e test', () => {
  const identityDocumentReviewPageUrl = '/identity-document-review';
  const identityDocumentReviewPageUrlPattern = new RegExp('/identity-document-review(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const identityDocumentReviewSample = { createdDate: '2024-03-02T01:47:25.099Z' };

  let identityDocumentReview;
  let assistanceTicket;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/assistance-tickets',
      body: {
        subject: 'unnaturally photography',
        description: 'down excluding warmly',
        status: 'CLOSED',
        type: 'OTHER',
        openedAt: '2024-03-01T22:29:42.880Z',
        closedAt: '2024-03-01T17:04:22.592Z',
        comments: 'vain',
        createdDate: '2024-03-02T07:39:34.933Z',
        lastModifiedDate: '2024-03-01T19:53:27.782Z',
        createdBy: 'fooey mid',
        lastModifiedBy: 'regret aw',
        userId: 22050,
      },
    }).then(({ body }) => {
      assistanceTicket = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/identity-document-reviews+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/identity-document-reviews').as('postEntityRequest');
    cy.intercept('DELETE', '/api/identity-document-reviews/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/assistance-tickets', {
      statusCode: 200,
      body: [assistanceTicket],
    });

    cy.intercept('GET', '/api/identity-documents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/document-review-observations', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (identityDocumentReview) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/identity-document-reviews/${identityDocumentReview.id}`,
      }).then(() => {
        identityDocumentReview = undefined;
      });
    }
  });

  afterEach(() => {
    if (assistanceTicket) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assistance-tickets/${assistanceTicket.id}`,
      }).then(() => {
        assistanceTicket = undefined;
      });
    }
  });

  it('IdentityDocumentReviews menu should load IdentityDocumentReviews page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('identity-document-review');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IdentityDocumentReview').should('exist');
    cy.url().should('match', identityDocumentReviewPageUrlPattern);
  });

  describe('IdentityDocumentReview page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(identityDocumentReviewPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IdentityDocumentReview page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/identity-document-review/new$'));
        cy.getEntityCreateUpdateHeading('IdentityDocumentReview');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentReviewPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/identity-document-reviews',
          body: {
            ...identityDocumentReviewSample,
            ticket: assistanceTicket,
          },
        }).then(({ body }) => {
          identityDocumentReview = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/identity-document-reviews+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [identityDocumentReview],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(identityDocumentReviewPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IdentityDocumentReview page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('identityDocumentReview');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentReviewPageUrlPattern);
      });

      it('edit button click should load edit IdentityDocumentReview page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentityDocumentReview');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentReviewPageUrlPattern);
      });

      it('edit button click should load edit IdentityDocumentReview page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentityDocumentReview');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentReviewPageUrlPattern);
      });

      it('last delete button click should delete instance of IdentityDocumentReview', () => {
        cy.intercept('GET', '/api/identity-document-reviews/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('identityDocumentReview').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentReviewPageUrlPattern);

        identityDocumentReview = undefined;
      });
    });
  });

  describe('new IdentityDocumentReview page', () => {
    beforeEach(() => {
      cy.visit(`${identityDocumentReviewPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IdentityDocumentReview');
    });

    it('should create an instance of IdentityDocumentReview', () => {
      cy.get(`[data-cy="documentStatus"]`).select('APPROVED');

      cy.get(`[data-cy="resolutionDate"]`).type('2024-03-02T09:15');
      cy.get(`[data-cy="resolutionDate"]`).blur();
      cy.get(`[data-cy="resolutionDate"]`).should('have.value', '2024-03-02T09:15');

      cy.get(`[data-cy="reviewStatus"]`).select('REJECTED');

      cy.get(`[data-cy="createdDate"]`).type('2024-03-01T19:26');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-03-01T19:26');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-03-01T16:05');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-03-01T16:05');

      cy.get(`[data-cy="createdBy"]`).type('chill');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'chill');

      cy.get(`[data-cy="lastModifiedBy"]`).type('sadly complete nervous');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'sadly complete nervous');

      cy.get(`[data-cy="ticket"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        identityDocumentReview = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', identityDocumentReviewPageUrlPattern);
    });
  });
});
