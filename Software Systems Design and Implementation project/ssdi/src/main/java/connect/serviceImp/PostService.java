package connect.serviceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import connect.daoImp.PostDao;
import connect.pojo.PostDetails;

@Service
public class PostService {

	@Autowired
	private PostDao postDao;

	@Autowired
	private AmazonClient amazonClient;

	public List<PostDetails> getRecentPostDetails(int studentId) {
		return postDao.getRecentPostDetails(studentId);
	}

	public boolean postData(MultipartFile file, String message, String category, int studentId) {
		String s3Url = null;
		if (file != null) {
			s3Url = amazonClient.uploadPost(category, studentId, file, file.getOriginalFilename());
		}
		return postDao.postData(studentId, category, s3Url, message);
	}

	public boolean deletedPost(int postId) {
		return postDao.deletedPost(postId);
	}

	public List<PostDetails> getRecentPostDetails(String category) {
		return postDao.getRecentPostDetails(category);
	}
}
