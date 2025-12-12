package com.gcp.labs.gcs.trigger.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesModule extends AbstractModule {

    @Provides
    @Singleton
    @Named("properties")
    public Properties provideProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    @Provides
    @Singleton
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Named("projectId")
    public String provideProjectId(Properties properties) {
        return properties.getProperty("projectId");
    }

    @Provides
    @Named("firestoreDatabaseId")
    public String provideFirestoreDatabaseId(Properties properties) {
        return properties.getProperty("firestore.database.id");
    }

    @Provides
    @Named("pubsubTopic")
    public String providePubSubTopicName(Properties properties) {
        return properties.getProperty("image.pubsub.topic");
    }

}
