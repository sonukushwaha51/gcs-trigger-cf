package com.gcp.labs.gcs.trigger.firestore;

import lombok.Data;

import java.util.Date;

@Data
public class ImageModel {

    private long imageSize;

    private String imageType;

    private String imageName;

    private Date createdTime;
}
