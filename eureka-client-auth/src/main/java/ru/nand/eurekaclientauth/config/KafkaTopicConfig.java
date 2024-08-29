package ru.nand.eurekaclientauth.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userRegistrationTopic(){
        return TopicBuilder.name("user-registration-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2")) // Минимальное количество синхронизированных реплик)
                .build();
    }

    @Bean
    public NewTopic userLoginTopic(){
        return TopicBuilder.name("user-login-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
