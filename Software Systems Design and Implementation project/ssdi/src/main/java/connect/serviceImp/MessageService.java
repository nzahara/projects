package connect.serviceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import connect.daoImp.MessageDao;
import connect.pojo.Message;
import connect.pojo.Notification;

@Service
public class MessageService {

	@Autowired
	MessageDao messageDao;

	/**
	 * @see MessageDao#insertMessage(Notification, String)
	 * 
	 * @param request
	 * @param attachment
	 * @return
	 */
	public boolean insertMessage(Notification request, String attachment) {
		return messageDao.insertMessage(request, attachment);
	}

	/**
	 * @see MessageDao#getUnreadMessages(int)
	 * 
	 * @param userId
	 * @return
	 */
	public List<Message> getUnReadMessages(int userId) {
		return messageDao.getUnreadMessages(userId);
	}

	/**
	 * @see MessageDao#markMessageAsRead(int)
	 * 
	 * @param messageId
	 * @return
	 */
	public boolean markMessageAsRead(int messageId) {
		return messageDao.markMessageAsRead(messageId);
	}

}
