package com.raul.truckmanagement.infrastructure.configuration;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Collections;

@Configuration
public class KafkaAdminConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public KafkaAdmin kafkaAdmin() {
    return new KafkaAdmin(Collections.singletonMap("bootstrap.servers", bootstrapServers));
  }

  @Bean
  public AdminClient adminClient() {
    return AdminClient.create(kafkaAdmin().getConfigurationProperties());
  }

  @Bean
  public NewTopic truckEventsTopic() {
    return new NewTopic("truck-events", 1, (short) 1);
  }
}