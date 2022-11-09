package org.mocken.email;

public class FileMetaData {
	private final String filename;
	private final int documentTypeId;
	private String contentType = null;
	private String customerEmail = null;
	private String title = null;
	private long defaultDeletionEpochMS = -1;
	private long documentDateEpochMS = -1;

	public FileMetaData(String filename, int documentTypeId) {
		this.documentTypeId=documentTypeId;
		this.filename=filename;
		// TODO Auto-generated constructor stub
	}

	public String getFilename() {
		return filename;
	}

	public long getDocumentTypeId() {
		return documentTypeId;
	}


	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	
	public long getDefaultDeletionEpochMS() {
		return defaultDeletionEpochMS;
	}

	public void setDefaultDeletionEpochMS(long defaultDeletionEpochMS) {
		this.defaultDeletionEpochMS = defaultDeletionEpochMS;
	}


	public boolean isValidConstraint() {
		return  customerEmail != null ;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getDocumentDateEpochMS() {
		return documentDateEpochMS;
	}

	public void setDocumentDateEpochMS(long documentDateEpochMS) {
		this.documentDateEpochMS = documentDateEpochMS;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
