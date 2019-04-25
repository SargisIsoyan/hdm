package am.iunetworks.ecr.client.V03.exception;

/**
 * Created with IntelliJ IDEA.
 * User: vmomjyan
 * Date: 2/21/14
 * Time: 11:22 AM
 */
public class ECRException extends RuntimeException {
    public ECRException(int responseCode) {
        super("An error occurred. Error code: "+responseCode);
    }
}
