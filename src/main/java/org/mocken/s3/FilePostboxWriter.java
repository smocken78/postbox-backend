package org.mocken.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.configuration.ConfigurationHolder3;
import org.mocken.exception.ApplicationException;


public class FilePostboxWriter {
	
	
	public FilePostboxWriter() {
	
	}

	private Logger logger = LogManager.getLogger(this.getClass());
	private String filePath = ConfigurationHolder3.getConfiguration().getString("postbox.file.path","/var/tmp/postbox");

		 
	public void saveFile(String key, InputStream is) throws IOException, ApplicationException {
		
		logger.debug("Trying to save {} to S3 storage",key);
		FileOutputStream fos = new FileOutputStream(filePath + "/" + key);
		fos.write(is.readAllBytes());
		fos.close();

	}
	
	public void delete(String key) throws IOException, ApplicationException {
		
		logger.debug("Trying to delete {} from S3 storage",key);
		
		File f = new File(filePath + "/" + key);
		f.delete();
	}

}
