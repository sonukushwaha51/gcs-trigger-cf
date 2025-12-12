package com.gcp.labs.gcs.trigger.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.events.cloud.storage.v1.StorageObjectData;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Slf4j
public class FirestoreService {

    private final Firestore firestore;

    private final String imageCollection;


    @Inject
    public FirestoreService(Firestore firestore, @Named("firestoreDatabaseCollection") String imageCollection) {
        this.firestore = firestore;
        this.imageCollection = imageCollection;
    }

    public void saveImageDataInFireStore(StorageObjectData storageObjectData) {
        ImageModel imageModel = new ImageModel();
        imageModel.setImageSize(storageObjectData.getSize());
        imageModel.setImageName(storageObjectData.getName());
        imageModel.setImageType(storageObjectData.getContentType());
        imageModel.setCreatedTime(Date.from(Instant.ofEpochMilli(storageObjectData.getTimeCreated().getSeconds() * 1000)));

        ApiFuture<WriteResult> future = firestore.collection(imageCollection).document(storageObjectData.getName()).set(imageModel);
        try {
            WriteResult result = future.get();
            log.info("Successfully wrote the data to firestore: {}", result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Exception occurred while writing to firestore", e);
            throw new RuntimeException(e);
        }

    }
}
