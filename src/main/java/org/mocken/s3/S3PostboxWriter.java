package org.mocken.s3;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.exception.ApplicationException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;


public class S3PostboxWriter {
	
	
	public S3PostboxWriter() {
	
	}

	private Logger logger = LogManager.getLogger(this.getClass());
	private AmazonS3 s3Client = null;
	
	 
	public void saveFile(String key, InputStream is) throws IOException, ApplicationException {
	
		String bucket = "congo-tuad-be-voice";
		String s3URL = "https://s3-internal.nicotuadev.de";
		String accessKey = "TDCNCLW7GICXOR9JTNTM";
		String secretKey = "qQ/BWyY5N+hmJeEagXOy+Euv9q6J1Z6IyivGDqP4";

		if (bucket==null || s3URL==null || accessKey==null || secretKey==null)
			throw new ApplicationException("All s3Entities must be set");
		
		logger.debug("Trying to save {} to S3 storage",key);
		
		AWSCredentials credentials = new BasicAWSCredentials(
			accessKey,
			secretKey
		);
		
		s3Client = AmazonS3ClientBuilder.standard()
              .withCredentials(new AWSStaticCredentialsProvider(credentials))
              .withPathStyleAccessEnabled(true)
              .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3URL, ""))
              .build();
      
		ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(is.available());

        PutObjectResult result = s3Client.putObject(bucket, key, is, metadata);
        logger.debug("File stored with eTag: {}",result.getETag());
        logger.debug("File stored with version: {}",result.getVersionId());

	}
	
	public void delete(String key) throws IOException, ApplicationException {
	
		String bucket = "congo-tuad-be-voice";
		String s3URL = "https://s3-internal.nicotuadev.de";
		String accessKey = "TDCNCLW7GICXOR9JTNTM";
		String secretKey = "qQ/BWyY5N+hmJeEagXOy+Euv9q6J1Z6IyivGDqP4";

		if (bucket==null || s3URL==null || accessKey==null || secretKey==null)
			throw new ApplicationException("All s3Entities must be set");
		
		logger.debug("Trying to delete {} from S3 storage",key);
		
		AWSCredentials credentials = new BasicAWSCredentials(
			accessKey,
			secretKey
		);
		
		s3Client = AmazonS3ClientBuilder.standard()
              .withCredentials(new AWSStaticCredentialsProvider(credentials))
              .withPathStyleAccessEnabled(true)
              .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3URL, ""))
              .build();
      
	    s3Client.deleteObject(bucket, key);

	}

}
