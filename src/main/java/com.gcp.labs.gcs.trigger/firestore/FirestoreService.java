package com.gcp.labs.gcs.trigger.firestore;

import com.google.cloud.firestore.Firestore;
import com.google.events.cloud.storage.v1.StorageObjectData;
import com.google.inject.Inject;
import com.google.inject.name.Named;

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
        imageModel.setCreatedTime(storageObjectData.getTimeCreated());

        firestore.collection(imageCollection).document(storageObjectData.getName()).set(imageModel);
    }
}
