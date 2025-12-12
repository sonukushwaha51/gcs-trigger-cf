package com.gcp.labs.gcs.trigger.firestore;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.util.Properties;

public class FirestoreModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(FirestoreService.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Named("firestoreDatabaseCollection")
    public String provideFirestoreDatabaseCollection(Properties properties) {
        return properties.getProperty("firestore.database.collection");
    }

    @Provides
    @Singleton
    public Firestore provideFireStore(@Named("firestoreDatabaseId") String firestoreDatabaseId) {
        return FirestoreOptions.newBuilder()
                .setDatabaseId(firestoreDatabaseId)
                .build().getService();
    }
}
