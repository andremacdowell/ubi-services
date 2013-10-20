package lac.puc.ubi.services.modellibrary;

import java.io.Serializable;
import java.util.UUID;

public class ResponseInfo implements Serializable {

	/**
	 * Default Java serial version UID
	 **/
	private static final long serialVersionUID = 1L;
	
	/** Client UUID */
	private UUID _uuid;
	private String _type;
	private String _payload;
	private String _status;
	private String _message;

	/**
	 * Constructor.
	 **/
	public ResponseInfo(UUID id, String type, String payload) {
		_uuid = id;
		_payload = payload;
		_type = payload;
	}
	
	public UUID getUuid() {
		return _uuid;
	}

	public String getType() {
		return _type;
	}
	
	public String getPayload() {
		return _payload;
	}
	
	public String getStatus() {
		return _status;
	}
	
	public void setStatus(String status) {
		_status = status;
	}
	
	public String getMessage() {
		return _message;
	}
	
	public void setMessage(String message) {
		_message = message;
	}
	
	/**
     * {@inheritDoc}
     **/
    @Override
    public String toString() {
    	return "Response";
	}
}
