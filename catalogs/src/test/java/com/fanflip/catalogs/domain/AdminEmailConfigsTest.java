package com.fanflip.catalogs.domain;

import static com.fanflip.catalogs.domain.AdminEmailConfigsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.catalogs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminEmailConfigsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminEmailConfigs.class);
        AdminEmailConfigs adminEmailConfigs1 = getAdminEmailConfigsSample1();
        AdminEmailConfigs adminEmailConfigs2 = new AdminEmailConfigs();
        assertThat(adminEmailConfigs1).isNotEqualTo(adminEmailConfigs2);

        adminEmailConfigs2.setId(adminEmailConfigs1.getId());
        assertThat(adminEmailConfigs1).isEqualTo(adminEmailConfigs2);

        adminEmailConfigs2 = getAdminEmailConfigsSample2();
        assertThat(adminEmailConfigs1).isNotEqualTo(adminEmailConfigs2);
    }
}
