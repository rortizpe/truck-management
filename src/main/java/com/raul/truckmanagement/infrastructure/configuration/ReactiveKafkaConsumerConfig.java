package com.raul.truckmanagement.infrastructure.configuration;

import com.raul.truckmanagement.domain.model.TruckEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ReactiveKafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;

  @Bean
  public ReactiveKafkaConsumerTemplate<String, TruckEvent> reactiveKafkaConsumerTemplate() {
    Map<String, Object> consumerProps = new HashMap<>();
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
    consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
    consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TruckEvent.class.getName());
    consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.raul.truckmanagement.domain.model");

    ReceiverOptions<String, TruckEvent> receiverOptions = ReceiverOptions.create(consumerProps);
    return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
  }
}