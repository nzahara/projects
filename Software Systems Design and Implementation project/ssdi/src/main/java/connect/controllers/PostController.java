package connect.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import connect.pojo.PostDetails;
import connect.serviceImp.PostService;

@RestController
@CrossOrigin
public class PostController {

	@Autowired
	private PostService postService;

	@GetMapping(value = "/fetch/recentpost")
	public List<PostDetails> getRecentPostDetails(@RequestParam(value = "student_id", required = false) int studentId) {
		return postService.getRecentPostDetails(studentId);
	}

	@GetMapping(value = "/fetch/recentpost/{category}")
	public List<PostDetails> getRecentPostDetails(@PathVariable(value = "category") String category) {
		return postService.getRecentPostDetails(category);
	}

	@PostMapping(value = "/post/{student_id}")
	public boolean postData(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "message") String message, @RequestParam(value = "category") String category,
			@PathVariable(value = "student_id") int studentId) {
		return postService.postData(file, message, category, studentId);
	}

	@PostMapping(value = "/delete/post")
	public boolean deletePost(@RequestParam(value = "post_id") int postId) {
		return postService.deletedPost(postId);
	}

}
