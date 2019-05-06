package connect.daoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import connect.database_connections.CommonDataSource;
import connect.pojo.Message;
import connect.pojo.Notification;

@Repository
public class MessageDao {

	private static String INSERT_MESSAGE = "insert into message (from_student_id, to_student_id,message,attachment,send_time )"
			+ " values (?,?,?,?,now() ) ";

	private static String GET_UNREAD_MESSAGES = " select message_id,first_name, middle_name,last_name, message,attachment,graduation_year,linked_in_id, major, "
			+ " from_student_id,profile_pic from message m join users u on u.student_id = m.from_student_id where m.to_student_id = ? and is_read = 0 ";

	private static String MARK_MESSAGE_READ = "update message set is_read = 1 where message_id = ? ";

	/**
	 * The function inserts the message details in the DB sent by a user to another.
	 * 
	 * <pre>
	 * Table Used : message
	 * </pre>
	 * 
	 * @param request
	 * @param attachment
	 * @return
	 */
	public boolean insertMessage(Notification request, String attachment) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CommonDataSource dataSource = null;
		int successfullyUpdated = -1;
		try {
			dataSource = CommonDataSource.getInstance();
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(INSERT_MESSAGE);
			pstmt.setInt(1, request.getFromStudentId());
			pstmt.setInt(2, request.getToStudentId());
			pstmt.setString(3, request.getMessage());
			pstmt.setString(4, attachment);
			successfullyUpdated = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("Exception occurred while inserting message details  " + e.getMessage());
		} finally {
			dataSource.closeResources(rs, pstmt, conn);
		}
		return successfullyUpdated == 1;
	}

	/**
	 * The function gets all the unread messages sent to the user specified.
	 * 
	 * <pre>
	 * Table used : message
	 * </pre>
	 * 
	 * @param userId
	 * @return
	 */
	public List<Message> getUnreadMessages(int userId) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CommonDataSource dataSource = null;
		List<Message> messageDetailsList = new ArrayList<Message>();
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(GET_UNREAD_MESSAGES);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Message messageDetails = new Message();
				messageDetails.setFirstName(rs.getString("first_name"));
				messageDetails.setMiddleName(rs.getString("middle_name"));
				messageDetails.setLastName(rs.getString("last_name"));
				messageDetails.setGradationYear(rs.getInt("graduation_year"));
				messageDetails.setMajor(rs.getString("major"));
				messageDetails.setLinkedInId(rs.getString("linked_in_id"));
				messageDetails.setMessage(rs.getString("message"));
				messageDetails.setAttachment(rs.getString("attachment"));
				messageDetails.setFromStudentId(rs.getInt("from_student_id"));
				messageDetails.setProfilePic(rs.getString("profile_pic"));
				messageDetails.setMessageId(rs.getInt("message_id"));
				messageDetailsList.add(messageDetails);
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while fetching unread messages " + e.getMessage());
		} finally {
			dataSource.closeResources(rs, pstmt, conn);
		}
		return messageDetailsList;
	}

	/**
	 * The function marks the message corresponding to the message id as read i.e.
	 * changes the is_read flag to 1 in message table.
	 * 
	 * @param messageId
	 * @return
	 */
	public boolean markMessageAsRead(int messageId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(MARK_MESSAGE_READ);
			pstmt.setInt(1, messageId);
			noOfRows = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("An exception has occured while marking message as read " + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return noOfRows == 1;
	}

}
