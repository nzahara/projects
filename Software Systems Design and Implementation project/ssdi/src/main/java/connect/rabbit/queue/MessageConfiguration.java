package connect.rabbit.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * Refer : https://dzone.com/articles/spring-boot-messaging-with-rabbitmq-in-pcf
 *
 */
@Configuration
public class MessageConfiguration {

	@Bean
	public Queue queue() {
		return new Queue("SAMPLE-INPUT000");
	}

}
