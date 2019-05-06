package connect.rabbit.producer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import connect.pojo.Notification;

/**
 * 
 * Refer : https://dzone.com/articles/spring-boot-messaging-with-rabbitmq-in-pcf
 *
 */
@Component
public class MessageProducer {

	private final RabbitTemplate rabbitTemplate;

	public MessageProducer(ConnectionFactory connectionFactory) {
		this.rabbitTemplate = new RabbitTemplate(connectionFactory);
	}

	/**
	 * The function pushes the request to the rabbit server so that it can be
	 * consumed and processed by the consumer.
	 * 
	 * @param file
	 * @param sampleRequest
	 * @throws IOException
	 */
	public void sendMessage(MultipartFile file, Notification sampleRequest) throws IOException {
		
		String originalFilename = (file == null) ? null : file.getOriginalFilename();
		sampleRequest.setFileName(originalFilename);
		String request = sampleRequest.toString();
		
		byte[] fileBytes = (file == null) ? new byte[0] : file.getBytes();
		byte[] messageArray = new byte[4 + fileBytes.length + request.length()];
		
		ByteBuffer fileSize = ByteBuffer.allocate(4);
		fileSize.putInt(fileBytes.length);

		System.arraycopy(fileSize.array(), 0, messageArray, 0, 4);
		System.arraycopy(fileBytes, 0, messageArray, 4, fileBytes.length);
		System.arraycopy(request.toString().getBytes(), 0, messageArray, fileBytes.length + 4,
				request.toString().getBytes().length);

		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType(MessageProperties.DEFAULT_CONTENT_TYPE);

		Message message = new Message(messageArray, messageProperties);
		rabbitTemplate.convertAndSend("SAMPLE-INPUT000", message);
	}
}
