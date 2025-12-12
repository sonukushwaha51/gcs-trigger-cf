package com.gcp.labs.gcs.trigger.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import java.io.IOException;
import java.util.UUID;

public class PubsubService {

    private final Publisher publisher;

    private final ObjectMapper objectMapper;


    @Inject
    public PubsubService(Publisher publisher, ObjectMapper objectMapper) {
        this.publisher = publisher;
        this.objectMapper = objectMapper;
    }

    public void publishErrorMessage(long imageSize) {
        ImageSizeError imageSizeError = new ImageSizeError();
        imageSizeError.setErrorMessage("Image size "+ imageSize + " is greater than allowed limit");
        imageSizeError.setStatus(400);
        imageSizeError.setImageSize(imageSize);

        try {
            publisher.publish(PubsubMessage.newBuilder()
                    .setData(ByteString.copyFrom(objectMapper.writeValueAsBytes(imageSizeError)))
                    .putAttributes("id", UUID.randomUUID().toString())
                    .build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
