import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/admin-user-profile">
        <Translate contentKey="global.menu.entities.adminUserProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/identity-document-review">
        <Translate contentKey="global.menu.entities.identityDocumentReview" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/document-review-observation">
        <Translate contentKey="global.menu.entities.documentReviewObservation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/identity-document">
        <Translate contentKey="global.menu.entities.identityDocument" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/assistance-ticket">
        <Translate contentKey="global.menu.entities.assistanceTicket" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/moderation-action">
        <Translate contentKey="global.menu.entities.moderationAction" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/admin-announcement">
        <Translate contentKey="global.menu.entities.adminAnnouncement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-report">
        <Translate contentKey="global.menu.entities.userReport" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/accounting-record">
        <Translate contentKey="global.menu.entities.accountingRecord" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/financial-statement">
        <Translate contentKey="global.menu.entities.financialStatement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tax-declaration">
        <Translate contentKey="global.menu.entities.taxDeclaration" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/budget">
        <Translate contentKey="global.menu.entities.budget" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/asset">
        <Translate contentKey="global.menu.entities.asset" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/liability">
        <Translate contentKey="global.menu.entities.liability" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
