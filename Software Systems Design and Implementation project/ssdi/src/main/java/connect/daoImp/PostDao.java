package connect.daoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import connect.database_connections.CommonDataSource;
import connect.pojo.PostDetails;
import connect.pojo.Message;
import connect.pojo.Notification;

@Repository
public class PostDao {

	private static String GET_RECENT_POST = "select post_id,u.first_name, u.middle_name,u.last_name,category,attachment,post_text "
			+ " from post p join users u on u.student_id = p.student_id ";

	private static String INSERT_POST = "insert into post (student_id,category,attachment,post_text,updated_date)"
			+ " values(?,?,?,?,now()) ";

	private static String DELETE_POST = "update post set is_deleted = 1 where post_id = ? ";

	private static String GET_CATEGORY_WISE_RECENT_POST = " select post_id,u.first_name, u.middle_name,u.last_name,category,attachment,post_text "
			+ " from post p join users u on u.student_id = p.student_id  where category like ? and is_deleted = 0 order by updated_date desc limit 5 ";

	public List<PostDetails> getRecentPostDetails(int studentId) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CommonDataSource dataSource = null;
		List<PostDetails> postDetails = new ArrayList<PostDetails>();
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			StringBuilder query = new StringBuilder(GET_RECENT_POST);
			query.append("where ");
			if (studentId != 0) {
				query.append(" p.student_id = ? and ");
			}
			query.append(" is_deleted = 0 order by updated_date desc limit 5 ");
			pstmt = conn.prepareStatement(query.toString());
			if (studentId != 0) {
				pstmt.setInt(1, studentId);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				PostDetails details = new PostDetails();
				details.setFirstName(rs.getString("first_name"));
				details.setMiddleName(rs.getString("middle_name"));
				details.setLastName(rs.getString("last_name"));
				details.setCategory(rs.getString("category"));
				details.setAttachment(rs.getString("attachment"));
				details.setPostText(rs.getString("post_text"));
				details.setPostId(rs.getInt("post_id"));
				postDetails.add(details);
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while fetching post details " + e.getMessage());
		} finally {
			dataSource.closeResources(rs, pstmt, conn);
		}
		return postDetails;
	}

	public List<PostDetails> getRecentPostDetails(String category) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CommonDataSource dataSource = null;
		List<PostDetails> postDetails = new ArrayList<PostDetails>();
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(GET_CATEGORY_WISE_RECENT_POST);
			pstmt.setString(1, category);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				PostDetails details = new PostDetails();
				details.setFirstName(rs.getString("first_name"));
				details.setMiddleName(rs.getString("middle_name"));
				details.setLastName(rs.getString("last_name"));
				details.setCategory(rs.getString("category"));
				details.setAttachment(rs.getString("attachment"));
				details.setPostText(rs.getString("post_text"));
				details.setPostId(rs.getInt("post_id"));
				postDetails.add(details);
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while fetching post details " + e.getMessage());
		} finally {
			dataSource.closeResources(rs, pstmt, conn);
		}
		return postDetails;
	}

	public boolean postData(int studentId, String category, String attachment, String postText) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CommonDataSource dataSource = null;
		int successfullyUpdated = -1;
		try {
			dataSource = CommonDataSource.getInstance();
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(INSERT_POST);
			pstmt.setLong(1, studentId);
			pstmt.setString(2, category);
			pstmt.setString(3, attachment);
			pstmt.setString(4, postText);
			successfullyUpdated = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("Exception occurred while posting  " + e.getMessage());
		} finally {
			dataSource.closeResources(rs, pstmt, conn);
		}
		return successfullyUpdated == 1;
	}

	public boolean deletedPost(int postId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(DELETE_POST);
			pstmt.setInt(1, postId);
			noOfRows = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("An exception has occured while deleting post " + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return noOfRows == 1;
	}

}
