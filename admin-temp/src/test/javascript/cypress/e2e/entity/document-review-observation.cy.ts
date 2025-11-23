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

describe('DocumentReviewObservation e2e test', () => {
  const documentReviewObservationPageUrl = '/document-review-observation';
  const documentReviewObservationPageUrlPattern = new RegExp('/document-review-observation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const documentReviewObservationSample = { comment: 'teeming darling carelessly' };

  let documentReviewObservation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/document-review-observations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/document-review-observations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/document-review-observations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (documentReviewObservation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/document-review-observations/${documentReviewObservation.id}`,
      }).then(() => {
        documentReviewObservation = undefined;
      });
    }
  });

  it('DocumentReviewObservations menu should load DocumentReviewObservations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('document-review-observation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocumentReviewObservation').should('exist');
    cy.url().should('match', documentReviewObservationPageUrlPattern);
  });

  describe('DocumentReviewObservation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(documentReviewObservationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DocumentReviewObservation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/document-review-observation/new$'));
        cy.getEntityCreateUpdateHeading('DocumentReviewObservation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentReviewObservationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/document-review-observations',
          body: documentReviewObservationSample,
        }).then(({ body }) => {
          documentReviewObservation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/document-review-observations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/document-review-observations?page=0&size=20>; rel="last",<http://localhost/api/document-review-observations?page=0&size=20>; rel="first"',
              },
              body: [documentReviewObservation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(documentReviewObservationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DocumentReviewObservation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('documentReviewObservation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentReviewObservationPageUrlPattern);
      });

      it('edit button click should load edit DocumentReviewObservation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocumentReviewObservation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentReviewObservationPageUrlPattern);
      });

      it('edit button click should load edit DocumentReviewObservation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocumentReviewObservation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentReviewObservationPageUrlPattern);
      });

      it('last delete button click should delete instance of DocumentReviewObservation', () => {
        cy.intercept('GET', '/api/document-review-observations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('documentReviewObservation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentReviewObservationPageUrlPattern);

        documentReviewObservation = undefined;
      });
    });
  });

  describe('new DocumentReviewObservation page', () => {
    beforeEach(() => {
      cy.visit(`${documentReviewObservationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DocumentReviewObservation');
    });

    it('should create an instance of DocumentReviewObservation', () => {
      cy.get(`[data-cy="commentDate"]`).type('2024-02-29T02:47');
      cy.get(`[data-cy="commentDate"]`).blur();
      cy.get(`[data-cy="commentDate"]`).should('have.value', '2024-02-29T02:47');

      cy.get(`[data-cy="comment"]`).type('shady now safely');
      cy.get(`[data-cy="comment"]`).should('have.value', 'shady now safely');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        documentReviewObservation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', documentReviewObservationPageUrlPattern);
    });
  });
});
