package lac.puc.ubi.services.modellibrary;

import java.io.Serializable;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Serializavel AuthInfo. 
 * Será visto tanto pelo Cliente Android quando pela Módulo Controlador, então 
 * recomenda-se que fique em algum tipo de projeto-library visto pelos dois.
 * 
 * @author andremd
 *
 */
public class AuthInfo implements Serializable {
	
	/**
	 * Default Java serial version UID
	 **/
	private static final long serialVersionUID = 1L;

	/** Client UUID */
	private UUID uuid;
	
	/** Authentication Info */
	private String email;
	private String password;

	/**
	 * Constructor.
	 **/
	public AuthInfo(UUID _id, String _name, String _pass) {
		uuid = _id;
		email = _name;
		password = _pass;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getTypeName() {
		return "authNode";
	}
	
	/**
     * {@inheritDoc}
     **/
    @Override
    public String toString() {

    	JSONObject result = new JSONObject();
		
		try {
			result.put("uuid", uuid.toString());
			result.put("email", email);
			result.put("pass", password);
			} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return result.toString();
	}
    
    
}
