package org.mocken.s3;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.exception.ApplicationException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;


public class S3PostboxReader {
	
	
	public S3PostboxReader() {
	}

	private Logger logger = LogManager.getLogger(this.getClass());
	private AmazonS3 s3Client = null;
	
	 
	public S3Object getFile(String objectName) throws IOException, ApplicationException {
	
		String bucket = "congo-tuad-be-voice";
		String s3URL = "https://s3-internal.nicotuadev.de";
		String accessKey = "TDCNCLW7GICXOR9JTNTM";
		String secretKey = "qQ/BWyY5N+hmJeEagXOy+Euv9q6J1Z6IyivGDqP4";

		S3Object s3object = null;

		if (bucket==null || s3URL==null || accessKey==null || secretKey==null)
			throw new ApplicationException("All s3Entities must be set");
		
		AWSCredentials credentials = new BasicAWSCredentials(
			accessKey,
			secretKey
		);
		s3Client = AmazonS3ClientBuilder.standard()
              .withCredentials(new AWSStaticCredentialsProvider(credentials))
              .withPathStyleAccessEnabled(true)
              .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3URL, ""))
              .build();
      
		
		try {
			s3object = s3Client.getObject(bucket, objectName);
			
		}
		catch (AmazonServiceException e) {
			logger.error("Could not fetch document",e);
		}
		catch (Exception e) {
			logger.error("Error fetching document",e);
		}
		return s3object;

	}

}
