package com.example.alex.balance.custom.realm;

import io.realm.RealmConfiguration;

public class BalanceRealmConfig {
    private static final String REAL_FILE_NAME = "balance.realm";
    private static long REALM_SCHEMA_VERSION = 3L;

    public static RealmConfiguration getRealmConfiguration() {
        return new RealmConfiguration.Builder()
                .name(REAL_FILE_NAME)
                .schemaVersion(REALM_SCHEMA_VERSION)
                .migration(new Migration())
                .build();
    }
}
