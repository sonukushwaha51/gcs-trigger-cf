package com.gcp.labs.gcs.trigger.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(PropertiesService.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    public Properties provideProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
            log.info("Properties: {}", properties.elements());
        } catch (IOException e) {
            log.error("Error reading properties: {}", properties);
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
