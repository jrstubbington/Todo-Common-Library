package org.example.todo.common.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaderMapper;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

// Basic Setup https://www.baeldung.com/spring-kafka
// Advanced https://docs.spring.io/spring-kafka/reference/html/
// Dynamic Consumer groupIds https://dzone.com/articles/kafka-tutorial-generate-multiple-consumer-groups-with-spring-kafka
// The above is not necessary as the ConsumerFactory handles this assignment from the application.yml

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "${kafka.groupId}")
	private String groupId;

	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		JsonDeserializer<Object> deserializer = new JsonDeserializer<>(Object.class);
		deserializer.addTrustedPackages("org.example.*");

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
				deserializer);
	}

	//Create a new Header mapper to allow custom object headers in Kafka messages.
	//Allow all packages within the listed packages to be treated as allowable headers
	@Bean("kafkaBinderHeaderMapper")
	public KafkaHeaderMapper kafkaBinderHeaderMapper() {
		DefaultKafkaHeaderMapper mapper = new DefaultKafkaHeaderMapper();
		mapper.addTrustedPackages("*");
		return mapper;
	}

	//Create the message converter with the kafkaBinderHeaderMapper
	@Bean
	public MessagingMessageConverter messagingMessageConverter() {
		MessagingMessageConverter messagingMessageConverter = new MessagingMessageConverter();
		messagingMessageConverter.setHeaderMapper(kafkaBinderHeaderMapper());
		return messagingMessageConverter;
	}

	@Bean
	public <T> ConcurrentKafkaListenerContainerFactory<String, T>
	kafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, T> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		//Add the custom message converter to the factory
		factory.setMessageConverter(messagingMessageConverter());
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		return factory;
	}
}
