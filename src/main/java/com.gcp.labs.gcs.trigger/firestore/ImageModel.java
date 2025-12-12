package com.gcp.labs.gcs.trigger.firestore;

import com.google.protobuf.Timestamp;
import com.google.type.DateTime;
import lombok.Data;

@Data
public class ImageModel {

    private long imageSize;

    private String imageType;

    private String imageName;

    private Timestamp createdTime;
}
