package com.example.alex.balance.custom.realm;

import io.realm.RealmConfiguration;

/**
 * Created by alex on 10.02.17.
 */

public class BalanceRealmConfig {
    private static final String REAL_FILE_NAME = "balance.realm";
    private static long REALM_SCHEMA_VERSION = 1L;

    public static RealmConfiguration setRealmConfiguration() {
        return new RealmConfiguration.Builder()
                .name(REAL_FILE_NAME)
                .schemaVersion(REALM_SCHEMA_VERSION)
                .migration(new Migration())
                .build();
    }
}
