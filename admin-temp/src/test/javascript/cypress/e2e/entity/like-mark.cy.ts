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

describe('LikeMark e2e test', () => {
  const likeMarkPageUrl = '/like-mark';
  const likeMarkPageUrlPattern = new RegExp('/like-mark(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const likeMarkSample = { emojiTypeId: 10415, createdDate: '2024-02-29T04:22:22.982Z', isDeleted: true };

  let likeMark;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/like-marks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/like-marks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/like-marks/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (likeMark) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/like-marks/${likeMark.id}`,
      }).then(() => {
        likeMark = undefined;
      });
    }
  });

  it('LikeMarks menu should load LikeMarks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('like-mark');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LikeMark').should('exist');
    cy.url().should('match', likeMarkPageUrlPattern);
  });

  describe('LikeMark page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(likeMarkPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LikeMark page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/like-mark/new$'));
        cy.getEntityCreateUpdateHeading('LikeMark');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', likeMarkPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/like-marks',
          body: likeMarkSample,
        }).then(({ body }) => {
          likeMark = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/like-marks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/like-marks?page=0&size=20>; rel="last",<http://localhost/api/like-marks?page=0&size=20>; rel="first"',
              },
              body: [likeMark],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(likeMarkPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LikeMark page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('likeMark');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', likeMarkPageUrlPattern);
      });

      it('edit button click should load edit LikeMark page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LikeMark');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', likeMarkPageUrlPattern);
      });

      it('edit button click should load edit LikeMark page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LikeMark');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', likeMarkPageUrlPattern);
      });

      it('last delete button click should delete instance of LikeMark', () => {
        cy.intercept('GET', '/api/like-marks/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('likeMark').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', likeMarkPageUrlPattern);

        likeMark = undefined;
      });
    });
  });

  describe('new LikeMark page', () => {
    beforeEach(() => {
      cy.visit(`${likeMarkPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LikeMark');
    });

    it('should create an instance of LikeMark', () => {
      cy.get(`[data-cy="emojiTypeId"]`).type('2073');
      cy.get(`[data-cy="emojiTypeId"]`).should('have.value', '2073');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T18:43');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T18:43');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T02:51');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T02:51');

      cy.get(`[data-cy="createdBy"]`).type('forenenst dull whoever');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'forenenst dull whoever');

      cy.get(`[data-cy="lastModifiedBy"]`).type('corrupt familiar where');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'corrupt familiar where');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="multimediaId"]`).type('5852');
      cy.get(`[data-cy="multimediaId"]`).should('have.value', '5852');

      cy.get(`[data-cy="messageId"]`).type('7517');
      cy.get(`[data-cy="messageId"]`).should('have.value', '7517');

      cy.get(`[data-cy="postId"]`).type('28470');
      cy.get(`[data-cy="postId"]`).should('have.value', '28470');

      cy.get(`[data-cy="commentId"]`).type('961');
      cy.get(`[data-cy="commentId"]`).should('have.value', '961');

      cy.get(`[data-cy="likerUserId"]`).type('23556');
      cy.get(`[data-cy="likerUserId"]`).should('have.value', '23556');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        likeMark = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', likeMarkPageUrlPattern);
    });
  });
});
