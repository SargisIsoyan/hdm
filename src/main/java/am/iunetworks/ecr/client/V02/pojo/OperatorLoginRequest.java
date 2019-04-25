package am.iunetworks.ecr.client.V02.pojo;

/**
 * Created with IntelliJ IDEA.
 * User: vmomjyan
 * Date: 12/19/13
 * Time: 1:36 AM
 */
public class OperatorLoginRequest {
    String password;
    int cashier;
    String pin;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCashier() {
        return cashier;
    }

    public void setCashier(int cashier) {
        this.cashier = cashier;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
