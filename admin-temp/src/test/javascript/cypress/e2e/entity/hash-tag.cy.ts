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

describe('HashTag e2e test', () => {
  const hashTagPageUrl = '/hash-tag';
  const hashTagPageUrlPattern = new RegExp('/hash-tag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const hashTagSample = { tagName: 'badly', hashtagType: 'POST', createdDate: '2024-02-29T00:03:28.251Z', isDeleted: true };

  let hashTag;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/hash-tags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/hash-tags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/hash-tags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (hashTag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/hash-tags/${hashTag.id}`,
      }).then(() => {
        hashTag = undefined;
      });
    }
  });

  it('HashTags menu should load HashTags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('hash-tag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HashTag').should('exist');
    cy.url().should('match', hashTagPageUrlPattern);
  });

  describe('HashTag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(hashTagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HashTag page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/hash-tag/new$'));
        cy.getEntityCreateUpdateHeading('HashTag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', hashTagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/hash-tags',
          body: hashTagSample,
        }).then(({ body }) => {
          hashTag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/hash-tags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/hash-tags?page=0&size=20>; rel="last",<http://localhost/api/hash-tags?page=0&size=20>; rel="first"',
              },
              body: [hashTag],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(hashTagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HashTag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('hashTag');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', hashTagPageUrlPattern);
      });

      it('edit button click should load edit HashTag page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HashTag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', hashTagPageUrlPattern);
      });

      it('edit button click should load edit HashTag page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HashTag');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', hashTagPageUrlPattern);
      });

      it('last delete button click should delete instance of HashTag', () => {
        cy.intercept('GET', '/api/hash-tags/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('hashTag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', hashTagPageUrlPattern);

        hashTag = undefined;
      });
    });
  });

  describe('new HashTag page', () => {
    beforeEach(() => {
      cy.visit(`${hashTagPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HashTag');
    });

    it('should create an instance of HashTag', () => {
      cy.get(`[data-cy="tagName"]`).type('imprint supposing');
      cy.get(`[data-cy="tagName"]`).should('have.value', 'imprint supposing');

      cy.get(`[data-cy="hashtagType"]`).select('USER');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T20:30');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T20:30');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T06:12');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T06:12');

      cy.get(`[data-cy="createdBy"]`).type('sparse rubbery epauliere');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'sparse rubbery epauliere');

      cy.get(`[data-cy="lastModifiedBy"]`).type('thankful');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'thankful');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        hashTag = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', hashTagPageUrlPattern);
    });
  });
});
