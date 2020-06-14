package org.example.todo.common.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

@Component
@Slf4j
public class KafkaProducer<T> {

	private KafkaTemplate<String, T> kafkaTemplate;

	public void simpleSendMessage(String topic, T message) {
		kafkaTemplate.send(topic, message);
	}

	public void sendMessage(String topic, KafkaOperation operation, T message) {
		Method method = null;
		UUID objectUuid = null;
		try {
			 method = message.getClass().getMethod("getUuid");
		}
		catch (SecurityException | NoSuchMethodException e) {
			log.error("Unable to find method 'getUuid' from class {}", message.getClass(), e);
		}
		try {
			objectUuid = (UUID) method.invoke(message);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			log.error("Unable to properly invoke method 'getUuid' from class {} ", message.getClass(), e);
		}

		Message<T> kafkaMessage = MessageBuilder
				.withPayload(message)
				.setHeader(KafkaHeaders.TOPIC, topic)
				//Allows the kafka partition to be set based off the object's uuid so messages for the same entity will
				// always arrive in order.
//				.setHeader(KafkaHeaders.MESSAGE_KEY, message.getUuid()) //TODO: Force models to have getUuid implementation https://github.com/swagger-api/swagger-codegen/issues/6414
				.setHeader(KafkaHeaders.MESSAGE_KEY, objectUuid)
				.setHeader("operation", operation)
				.build();

		ListenableFuture<SendResult<String, T>> future =
				kafkaTemplate.send(kafkaMessage);

		future.addCallback(new ListenableFutureCallback<SendResult<String, T>>() {

			@Override
			public void onSuccess(SendResult<String, T> result) {
				log.debug("Sent message=[" + message +
						"] with offset=[" + result.getRecordMetadata().offset() + "]");
			}
			@Override
			public void onFailure(Throwable ex) {
				log.error("Unable to send message=["
						+ message + "] due to : " + ex.getMessage());
			}
		});
	}

	@Autowired
	public void setKafkaTemplate(KafkaTemplate<String, T> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
}
