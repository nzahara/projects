package connect.serviceImp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

/**
 * Refer :
 * https://medium.com/oril/uploading-files-to-aws-s3-bucket-using-spring-boot-483fcb6f8646
 *
 */
@Component
public class AmazonClient {

	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

	@Value("${amazonProperties.accessKey}")
	private String accessKey;

	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	/**
	 * The function uploads the given file to S3.
	 * 
	 * @param multipartFile
	 * @return
	 */
	public String uploadFile(MultipartFile multipartFile) {

		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl + "/" + bucketName + "/" + fileName; // bucketName
			System.out.println(fileUrl);
			uploadFileTos3bucket(fileName, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	/**
	 * The function uploads the given file to the specified folder path in S3.
	 * 
	 * @param category
	 * @param studentId
	 * @param file
	 * @param fileName
	 * @return
	 */
	public String uploadFile(String category, int studentId, File file, String fileName) {
		String fileUrl = "";
		try {
			fileUrl = endpointUrl + "/" + bucketName + "/" + file.getName(); // bucketName
			System.out.println(fileUrl);
			uploadFileTos3bucket(file.getName(), file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	/**
	 * The function uploads the given file to the specified folder path in S3.
	 * 
	 * @param category
	 * @param studentId
	 * @param multipartFile
	 * @param fileName
	 * @return
	 */
	public String uploadPost(String category, int studentId, MultipartFile multipartFile, String fileName) {

		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			fileUrl = endpointUrl + "/" + bucketName + "/" + studentId + "/" + category + "/"
					+ multipartFile.getOriginalFilename();

			System.out.println(fileUrl);
			String key = studentId + "/" + category + "/" + multipartFile.getOriginalFilename();
			uploadFileTos3bucket(key, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	/**
	 * The function deletes s3 object.
	 * 
	 * @param fileUrl
	 * @return
	 */
	public String deleteFileFromS3Bucket(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
		return "Successfully deleted";
	}

	/**
	 * The function gets the s3 object from the key specified.
	 * 
	 * @param fileName
	 * @return
	 */
	public S3Object getObjectFromS3(String fileName) {
		return s3client.getObject(new GetObjectRequest(bucketName, fileName));
	}
}
