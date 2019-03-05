package com.ssdi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;


/**
 * 
 * Reference Code : https://medium.com/oril/uploading-files-to-aws-s3-bucket-using-spring-boot-483fcb6f8646
 *
 */

@RestController
@RequestMapping("storage")
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
}
