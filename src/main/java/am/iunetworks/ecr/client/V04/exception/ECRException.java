package am.iunetworks.ecr.client.V04.exception;

/**
 * Created by mainserver
 */
public class ECRException extends RuntimeException {
    public ECRException(int responseCode) {
        super("An error occurred. Error code: "+responseCode);
    }
}
