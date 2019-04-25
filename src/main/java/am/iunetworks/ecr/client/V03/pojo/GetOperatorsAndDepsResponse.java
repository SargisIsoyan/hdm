package am.iunetworks.ecr.client.V03.pojo;

import java.util.List;

public class GetOperatorsAndDepsResponse extends CommonResponse {

    private List<Department> d;

    public List<Cashier> c;

    public List<Department> getD() {
        return d;
    }

    public void setD(List<Department> d) {
        this.d = d;
    }

    public List<Cashier> getC() {
        return c;
    }

    public void setC(List<Cashier> c) {
        this.c = c;
    }
}
