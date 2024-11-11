package com.raul.truckmanagement.infrastructure.configuration;

import com.raul.truckmanagement.domain.model.TruckEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ReactiveKafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public ReactiveKafkaProducerTemplate<String, TruckEvent> reactiveKafkaProducerTemplate() {
    Map<String, Object> producerProps = new HashMap<>();
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    producerProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

    SenderOptions<String, TruckEvent> senderOptions = SenderOptions.create(producerProps);
    return new ReactiveKafkaProducerTemplate<>(senderOptions);
  }
}