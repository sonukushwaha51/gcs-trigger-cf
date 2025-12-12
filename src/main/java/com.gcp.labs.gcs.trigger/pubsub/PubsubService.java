package com.gcp.labs.gcs.trigger.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
import java.util.UUID;

public class PubsubService {

    private final String pubsubTopicName;

    private final String projectId;

    private final Publisher publisher;

    private final ObjectMapper objectMapper;


    @Inject
    public PubsubService(@Named("pubsubTopic") String pubsubTopicName, @Named("projectId") String projectId, Publisher publisher, ObjectMapper objectMapper) {
        this.pubsubTopicName = pubsubTopicName;
        this.projectId = projectId;
        this.publisher = publisher;
        this.objectMapper = objectMapper;
    }

    public void publishErrorMessage(long imageSize) {
        ImageSizeError imageSizeError = new ImageSizeError();
        imageSizeError.setErrorMessage("Image size "+ imageSize + " is greater than allowed limit");
        imageSizeError.setStatus(400);
        imageSizeError.setImageSize(imageSize);

        try {
            Publisher publisher = Publisher.newBuilder(TopicName.of(projectId, pubsubTopicName)).build();
            publisher.publish(PubsubMessage.newBuilder()
                    .setData(ByteString.copyFrom(objectMapper.writeValueAsBytes(imageSizeError)))
                    .putAttributes("id", UUID.randomUUID().toString())
                    .build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
