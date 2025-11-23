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

describe('EmojiType e2e test', () => {
  const emojiTypePageUrl = '/emoji-type';
  const emojiTypePageUrlPattern = new RegExp('/emoji-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const emojiTypeSample = {
    thumbnail: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    thumbnailContentType: 'unknown',
    description: 'knacker aside that',
    createdDate: '2024-02-29T05:41:37.715Z',
    isDeleted: false,
  };

  let emojiType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/emoji-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/emoji-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/emoji-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (emojiType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/emoji-types/${emojiType.id}`,
      }).then(() => {
        emojiType = undefined;
      });
    }
  });

  it('EmojiTypes menu should load EmojiTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('emoji-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EmojiType').should('exist');
    cy.url().should('match', emojiTypePageUrlPattern);
  });

  describe('EmojiType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(emojiTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EmojiType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/emoji-type/new$'));
        cy.getEntityCreateUpdateHeading('EmojiType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', emojiTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/emoji-types',
          body: emojiTypeSample,
        }).then(({ body }) => {
          emojiType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/emoji-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/emoji-types?page=0&size=20>; rel="last",<http://localhost/api/emoji-types?page=0&size=20>; rel="first"',
              },
              body: [emojiType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(emojiTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EmojiType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('emojiType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', emojiTypePageUrlPattern);
      });

      it('edit button click should load edit EmojiType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EmojiType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', emojiTypePageUrlPattern);
      });

      it('edit button click should load edit EmojiType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EmojiType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', emojiTypePageUrlPattern);
      });

      it('last delete button click should delete instance of EmojiType', () => {
        cy.intercept('GET', '/api/emoji-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('emojiType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', emojiTypePageUrlPattern);

        emojiType = undefined;
      });
    });
  });

  describe('new EmojiType page', () => {
    beforeEach(() => {
      cy.visit(`${emojiTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EmojiType');
    });

    it('should create an instance of EmojiType', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="description"]`).type('outside under');
      cy.get(`[data-cy="description"]`).should('have.value', 'outside under');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T04:13');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T04:13');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T10:52');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T10:52');

      cy.get(`[data-cy="createdBy"]`).type('jealous nearly among');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'jealous nearly among');

      cy.get(`[data-cy="lastModifiedBy"]`).type('quizzically jagged');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'quizzically jagged');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        emojiType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', emojiTypePageUrlPattern);
    });
  });
});
