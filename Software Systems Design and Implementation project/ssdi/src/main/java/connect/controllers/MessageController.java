package connect.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import connect.Application;
import connect.pojo.Message;
import connect.pojo.Notification;
import connect.rabbit.producer.MessageProducer;
import connect.serviceImp.MessageService;

@RestController
@CrossOrigin
public class MessageController {

	@Autowired
	private MessageProducer messageProducer;

	@Autowired
	private MessageService messageService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostMapping("/message")
	public void sendMessage(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "message_details") String requestString) throws IOException {
		Notification request = new ObjectMapper().readValue(requestString, Notification.class);
		messageProducer.sendMessage(file, request);
	}

	@GetMapping(value = "/unread/messages/{user_id}")
	public List<Message> getUnreadMessages(@PathVariable(value = "user_id") int userId) {
		return messageService.getUnReadMessages(userId);
	}

	@PostMapping(value = "/mark/read")
	public boolean markMessageAsRead(@RequestParam(value = "message_id") int messageId) {
		return messageService.markMessageAsRead(messageId);
	}

}
