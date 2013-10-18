package lac.puc.ubi.services.modellibrary;

import java.io.Serializable;
import java.util.UUID;

/**
 * Serializavel PingInfo. 
 * Ser� visto tanto pelo Cliente Android quando pela M�dulo Controlador, ent�o 
 * recomenda-se que fique em algum tipo de projeto-library visto pelos dois.
 * 
 * @author andremd
 *
 */
public class PingInfo implements Serializable {
	
	/**
	 * Default Java serial version UID
	 **/
	private static final long serialVersionUID = 1L;

	/** Client UUID */
	private UUID uuid;

	/**
	 * Constructor.
	 **/
	public PingInfo(UUID _id) {
		uuid = _id;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	/**
     * {@inheritDoc}
     **/
    @Override
    public String toString() {
    	return "Pong!";
	}
}
