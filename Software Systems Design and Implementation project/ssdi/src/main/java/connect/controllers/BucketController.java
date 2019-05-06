package connect.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;

import connect.serviceImp.AmazonClient;



/**
 * 
 * Reference Code : https://medium.com/oril/uploading-files-to-aws-s3-bucket-using-spring-boot-483fcb6f8646
 *
 */

@RestController
@RequestMapping("storage")
@CrossOrigin
public class BucketController {

		@Autowired
		private AmazonClient amazonClient;
		

	    @PostMapping("/uploadFile")
	    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
	        return this.amazonClient.uploadFile(file);
	    }

	    @DeleteMapping("/deleteFile")
	    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
	        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
	    }
	    
	    @GetMapping("/getFile")
	    public S3Object getFileUrl(@RequestParam(value = "file_name") String fileName) {
	    	return this.amazonClient.getObjectFromS3(fileName);
	    }
	    
	    @PostMapping("/uploadPost/{student_id}/{category}")
	    public String uploadFile(@PathVariable(value = "student_id") int studentId, 
	    		@PathVariable(value = "category") String category, @RequestPart(value = "file") MultipartFile file,
	    		@RequestParam(value = "file_name") String fileName) {
	    	
	    	return this.amazonClient.uploadPost(category, studentId, file,fileName);
	    }
}
