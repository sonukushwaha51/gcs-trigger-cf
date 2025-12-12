package com.gcp.labs.gcs.trigger;

import com.gcp.labs.gcs.trigger.firestore.FirestoreModule;
import com.gcp.labs.gcs.trigger.firestore.FirestoreService;
import com.gcp.labs.gcs.trigger.properties.PropertiesModule;
import com.gcp.labs.gcs.trigger.pubsub.PubsubService;
import com.google.cloud.functions.CloudEventsFunction;
import com.google.events.cloud.storage.v1.StorageObjectData;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.util.JsonFormat;
import io.cloudevents.CloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class GcsTriggerCloudFunction implements CloudEventsFunction {

    private final FirestoreService firestoreService;

    private final PubsubService pubsubService;

    public GcsTriggerCloudFunction() {
        Injector injector = Guice.createInjector(
                new FirestoreModule(),
                new PropertiesModule()
        );
        this.firestoreService = injector.getInstance(FirestoreService.class);
        this.pubsubService = injector.getInstance(PubsubService.class);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(GcsTriggerCloudFunction.class);

    @Override
    public void accept(CloudEvent cloudEvent) throws Exception {

        if (cloudEvent == null) {
            return;
        }

        String cloudEventsData = new String(cloudEvent.getData().toBytes(), StandardCharsets.UTF_8);
        StorageObjectData.Builder storageObjectBuilder = StorageObjectData.newBuilder();
        JsonFormat.Parser parser = JsonFormat.parser();
        parser.merge(cloudEventsData, storageObjectBuilder);
        StorageObjectData storageObjectData = storageObjectBuilder.build();

        long fileSize = storageObjectData.getSize();

        if (fileSize > 5_000_000) {
            LOGGER.info("File size is greater than threshold : {}", storageObjectData.getSize());
            pubsubService.publishErrorMessage(fileSize);
            return;
        }

        firestoreService.saveImageDataInFireStore(storageObjectData);


    }
}
