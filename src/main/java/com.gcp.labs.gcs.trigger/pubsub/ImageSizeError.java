package com.gcp.labs.gcs.trigger.pubsub;

import lombok.Data;

@Data
public class ImageSizeError {

    private int status;

    private long imageSize;

    private String errorMessage;
}
