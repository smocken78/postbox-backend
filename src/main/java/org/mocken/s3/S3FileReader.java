package org.mocken.s3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.configuration.ConfigurationHolder3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

public class S3FileReader {

	private Logger logger = LogManager.getLogger(this.getClass());
	private AmazonS3 s3Client = null;
	private String bucket = ConfigurationHolder3.getConfiguration().getString("s3.vzf.bucket", "congo-tuad-fe-tkg-pdf-archive");
	private String s3URL = ConfigurationHolder3.getConfiguration().getString("s3.vzf.url", "https://s3-internal.nicotuadev.de");
	private String accessKey = ConfigurationHolder3.getConfiguration().getString("s3.vzf.access_key", "EBDJ6A6OOHLD13UD17ZC");
	private String secretKey = ConfigurationHolder3.getConfiguration().getString("s3.vzf.secret_key", "+Bx1lq4ErR7QgpaqMd5gbBQbF0Jxc8lTNnSxojIF");
	 
	public S3Object getFile(String objectName) {
		
		logger.debug("Trying to get {} from S3 storage",objectName);
		
		AWSCredentials credentials = new BasicAWSCredentials(
			accessKey,
			secretKey
		);
		
		s3Client = AmazonS3ClientBuilder.standard()
              .withCredentials(new AWSStaticCredentialsProvider(credentials))
              .withPathStyleAccessEnabled(true)
              .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3URL, ""))
              .build();
      
		S3Object s3object = null;
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

		
		
	/*
	 * Notation fuer write Operationen
	 * vzf_${order_id}-check24_1.pdf 	
	 */

	}

}
