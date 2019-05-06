package connect.rabbit.consumer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import connect.pojo.Notification;
import connect.serviceImp.AmazonClient;
import connect.serviceImp.MessageService;

/**
 * 
 * Refer : https://dzone.com/articles/spring-boot-messaging-with-rabbitmq-in-pcf
 *
 */
@Component
public class MessageConsumer {

	@Autowired
	private AmazonClient amazonClient;

	@Autowired
	private MessageService messageService;

	@RabbitListener(queues = "SAMPLE-INPUT000")
	public void handleForwardEngineeringRequest(Message messageRequest) throws IOException {

		byte[] messageBody = messageRequest.getBody();
		byte[] buf = new byte[4];
		
		System.arraycopy(messageBody, 0, buf, 0, 4);
		ByteBuffer fileSizeBuf = ByteBuffer.wrap(buf);
		int fileSize = fileSizeBuf.getInt();

		String jsonString = new String(messageBody, 4 + fileSize, messageBody.length - (4 + fileSize));

		Notification request = new ObjectMapper().readValue(jsonString.toString(), Notification.class);
		String s3Url = null;
		
		if (fileSize != 0) {
			File file = new File(request.getFileName());
			FileOutputStream fs = new FileOutputStream(file);
			fs.write(messageBody, 4, fileSize);
			fs.close();
			s3Url = amazonClient.uploadFile("message", request.getToStudentId(), file, request.getFileName());
			System.out.println("Url:" + s3Url);
		}

		messageService.insertMessage(request, s3Url);
	}
}
