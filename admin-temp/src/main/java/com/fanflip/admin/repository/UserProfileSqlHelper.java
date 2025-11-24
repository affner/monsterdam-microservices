package com.monsterdam.admin.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserProfileSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("email_contact", table, columnPrefix + "_email_contact"));
        columns.add(Column.aliased("profile_photo", table, columnPrefix + "_profile_photo"));
        columns.add(Column.aliased("profile_photo_content_type", table, columnPrefix + "_profile_photo_content_type"));
        columns.add(Column.aliased("cover_photo", table, columnPrefix + "_cover_photo"));
        columns.add(Column.aliased("cover_photo_content_type", table, columnPrefix + "_cover_photo_content_type"));
        columns.add(Column.aliased("profile_photo_s_3_key", table, columnPrefix + "_profile_photo_s_3_key"));
        columns.add(Column.aliased("cover_photo_s_3_key", table, columnPrefix + "_cover_photo_s_3_key"));
        columns.add(Column.aliased("main_content_url", table, columnPrefix + "_main_content_url"));
        columns.add(Column.aliased("mobile_phone", table, columnPrefix + "_mobile_phone"));
        columns.add(Column.aliased("website_url", table, columnPrefix + "_website_url"));
        columns.add(Column.aliased("amazon_wishlist_url", table, columnPrefix + "_amazon_wishlist_url"));
        columns.add(Column.aliased("last_login_date", table, columnPrefix + "_last_login_date"));
        columns.add(Column.aliased("biography", table, columnPrefix + "_biography"));
        columns.add(Column.aliased("is_free", table, columnPrefix + "_is_free"));
        columns.add(Column.aliased("created_date", table, columnPrefix + "_created_date"));
        columns.add(Column.aliased("last_modified_date", table, columnPrefix + "_last_modified_date"));
        columns.add(Column.aliased("created_by", table, columnPrefix + "_created_by"));
        columns.add(Column.aliased("last_modified_by", table, columnPrefix + "_last_modified_by"));
        columns.add(Column.aliased("is_deleted", table, columnPrefix + "_is_deleted"));

        columns.add(Column.aliased("user_lite_id", table, columnPrefix + "_user_lite_id"));
        columns.add(Column.aliased("settings_id", table, columnPrefix + "_settings_id"));
        columns.add(Column.aliased("country_of_birth_id", table, columnPrefix + "_country_of_birth_id"));
        columns.add(Column.aliased("state_of_residence_id", table, columnPrefix + "_state_of_residence_id"));
        return columns;
    }
}
