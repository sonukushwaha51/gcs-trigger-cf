package com.gcp.labs.gcs.trigger.firestore;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
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

    @Provides
    @Singleton
    public Publisher publisher(@Named("projectId") String projectId, @Named("pubsubTopic") String pubsubTopic) throws IOException {
        return Publisher.newBuilder(TopicName.of(projectId, pubsubTopic)).build();
    }
}
