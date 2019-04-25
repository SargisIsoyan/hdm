package am.iunetworks.ecr.client.V04.pojo;

import java.util.List;

public class GetOperatorsAndDepsResponse extends CommonResponse {

    public List<Cashier> c;
    private List<Department> d;

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
