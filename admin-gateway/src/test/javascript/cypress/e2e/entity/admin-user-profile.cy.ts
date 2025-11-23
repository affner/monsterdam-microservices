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

describe('AdminUserProfile e2e test', () => {
  const adminUserProfilePageUrl = '/admin-user-profile';
  const adminUserProfilePageUrlPattern = new RegExp('/admin-user-profile(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const adminUserProfileSample = {
    fullName: 'ymg47s',
    emailAddress: '"8S@e?[.`wK,',
    nickName: 'yg1fctr',
    gender: 'TRANS_FEMALE',
    lastLoginDate: '2024-03-01T17:46:55.890Z',
    birthDate: '2024-03-01',
    createdDate: '2024-03-02T09:05:38.265Z',
    isDeleted: true,
  };

  let adminUserProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/admin-user-profiles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/admin-user-profiles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/admin-user-profiles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (adminUserProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/admin-user-profiles/${adminUserProfile.id}`,
      }).then(() => {
        adminUserProfile = undefined;
      });
    }
  });

  it('AdminUserProfiles menu should load AdminUserProfiles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('admin-user-profile');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AdminUserProfile').should('exist');
    cy.url().should('match', adminUserProfilePageUrlPattern);
  });

  describe('AdminUserProfile page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(adminUserProfilePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AdminUserProfile page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/admin-user-profile/new$'));
        cy.getEntityCreateUpdateHeading('AdminUserProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminUserProfilePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/admin-user-profiles',
          body: adminUserProfileSample,
        }).then(({ body }) => {
          adminUserProfile = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/admin-user-profiles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [adminUserProfile],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(adminUserProfilePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AdminUserProfile page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('adminUserProfile');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminUserProfilePageUrlPattern);
      });

      it('edit button click should load edit AdminUserProfile page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AdminUserProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminUserProfilePageUrlPattern);
      });

      it('edit button click should load edit AdminUserProfile page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AdminUserProfile');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminUserProfilePageUrlPattern);
      });

      it('last delete button click should delete instance of AdminUserProfile', () => {
        cy.intercept('GET', '/api/admin-user-profiles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('adminUserProfile').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adminUserProfilePageUrlPattern);

        adminUserProfile = undefined;
      });
    });
  });

  describe('new AdminUserProfile page', () => {
    beforeEach(() => {
      cy.visit(`${adminUserProfilePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AdminUserProfile');
    });

    it('should create an instance of AdminUserProfile', () => {
      cy.get(`[data-cy="fullName"]`).type('06');
      cy.get(`[data-cy="fullName"]`).should('have.value', '06');

      cy.get(`[data-cy="emailAddress"]`).type(" u@;Eli's.0>DB;3");
      cy.get(`[data-cy="emailAddress"]`).should('have.value', " u@;Eli's.0>DB;3");

      cy.get(`[data-cy="nickName"]`).type('e9amwqb');
      cy.get(`[data-cy="nickName"]`).should('have.value', 'e9amwqb');

      cy.get(`[data-cy="gender"]`).select('TRANS_FEMALE');

      cy.get(`[data-cy="mobilePhone"]`).type('+47959611346');
      cy.get(`[data-cy="mobilePhone"]`).should('have.value', '+47959611346');

      cy.get(`[data-cy="lastLoginDate"]`).type('2024-03-02T13:17');
      cy.get(`[data-cy="lastLoginDate"]`).blur();
      cy.get(`[data-cy="lastLoginDate"]`).should('have.value', '2024-03-02T13:17');

      cy.get(`[data-cy="birthDate"]`).type('2024-03-02');
      cy.get(`[data-cy="birthDate"]`).blur();
      cy.get(`[data-cy="birthDate"]`).should('have.value', '2024-03-02');

      cy.get(`[data-cy="createdDate"]`).type('2024-03-02T13:30');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-03-02T13:30');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-03-01T20:36');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-03-01T20:36');

      cy.get(`[data-cy="createdBy"]`).type('merrily hence below');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'merrily hence below');

      cy.get(`[data-cy="lastModifiedBy"]`).type('pollution now');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'pollution now');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        adminUserProfile = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', adminUserProfilePageUrlPattern);
    });
  });
});
