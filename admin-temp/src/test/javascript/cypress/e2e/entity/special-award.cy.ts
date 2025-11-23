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

describe('SpecialAward e2e test', () => {
  const specialAwardPageUrl = '/special-award';
  const specialAwardPageUrlPattern = new RegExp('/special-award(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const specialAwardSample = {
    startDate: '2024-02-29',
    endDate: '2024-02-29',
    createdDate: '2024-02-29T16:44:21.052Z',
    isDeleted: true,
    viewerId: 24481,
    creatorId: 20703,
    specialTitleId: 11754,
  };

  let specialAward;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/special-awards+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/special-awards').as('postEntityRequest');
    cy.intercept('DELETE', '/api/special-awards/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (specialAward) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/special-awards/${specialAward.id}`,
      }).then(() => {
        specialAward = undefined;
      });
    }
  });

  it('SpecialAwards menu should load SpecialAwards page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('special-award');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SpecialAward').should('exist');
    cy.url().should('match', specialAwardPageUrlPattern);
  });

  describe('SpecialAward page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(specialAwardPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SpecialAward page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/special-award/new$'));
        cy.getEntityCreateUpdateHeading('SpecialAward');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialAwardPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/special-awards',
          body: specialAwardSample,
        }).then(({ body }) => {
          specialAward = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/special-awards+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/special-awards?page=0&size=20>; rel="last",<http://localhost/api/special-awards?page=0&size=20>; rel="first"',
              },
              body: [specialAward],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(specialAwardPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SpecialAward page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('specialAward');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialAwardPageUrlPattern);
      });

      it('edit button click should load edit SpecialAward page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SpecialAward');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialAwardPageUrlPattern);
      });

      it('edit button click should load edit SpecialAward page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SpecialAward');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialAwardPageUrlPattern);
      });

      it('last delete button click should delete instance of SpecialAward', () => {
        cy.intercept('GET', '/api/special-awards/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('specialAward').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialAwardPageUrlPattern);

        specialAward = undefined;
      });
    });
  });

  describe('new SpecialAward page', () => {
    beforeEach(() => {
      cy.visit(`${specialAwardPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SpecialAward');
    });

    it('should create an instance of SpecialAward', () => {
      cy.get(`[data-cy="startDate"]`).type('2024-02-29');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="endDate"]`).type('2024-02-29');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-02-29');

      cy.get(`[data-cy="reason"]`).type('geez diesel fortunately');
      cy.get(`[data-cy="reason"]`).should('have.value', 'geez diesel fortunately');

      cy.get(`[data-cy="altSpecialTitle"]`).type('ugh hm');
      cy.get(`[data-cy="altSpecialTitle"]`).should('have.value', 'ugh hm');

      cy.get(`[data-cy="createdDate"]`).type('2024-02-29T21:28');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-02-29T21:28');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-02-29T09:48');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-02-29T09:48');

      cy.get(`[data-cy="createdBy"]`).type('fork');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'fork');

      cy.get(`[data-cy="lastModifiedBy"]`).type('coaxingly modulo');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'coaxingly modulo');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="viewerId"]`).type('5296');
      cy.get(`[data-cy="viewerId"]`).should('have.value', '5296');

      cy.get(`[data-cy="creatorId"]`).type('30372');
      cy.get(`[data-cy="creatorId"]`).should('have.value', '30372');

      cy.get(`[data-cy="specialTitleId"]`).type('28258');
      cy.get(`[data-cy="specialTitleId"]`).should('have.value', '28258');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        specialAward = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', specialAwardPageUrlPattern);
    });
  });
});
