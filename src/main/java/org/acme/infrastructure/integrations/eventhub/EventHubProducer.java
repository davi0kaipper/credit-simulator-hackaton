package org.acme.infrastructure.integrations.eventhub;

import java.util.Collections;

import org.eclipse.microprofile.config.Config;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventHubProducer {
    @Inject
    Config config;
        
    private final EventHubProducerClient producerClient;

    public EventHubProducer() {
        this.producerClient = new EventHubClientBuilder()
            .connectionString(
                this.config.getValue("event.hub.connection.string", String.class),
                this.config.getValue("event.hub.namespace", String.class)
            ).buildProducerClient();
    }

    public void sendEvent(String content) {
        EventData eventData = new EventData(content);
        this.producerClient.send(Collections.singletonList(eventData));
        this.producerClient.close();
    }
}