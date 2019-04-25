package am.iunetworks.ecr.client.V03.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vahemomjyan
 * Date: 3/20/14
 * Time: 11:34 AM
 */
public class SetupHeaderFooterRequest {
    //Array of HeaderFooter as Receipt Header Lines
    List<HeaderFooter> headers = new ArrayList<HeaderFooter>();

    //Array of HeaderFooter as Receipt Footer Lines
    List<HeaderFooter> footers = new ArrayList<HeaderFooter>();

    public List<HeaderFooter> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HeaderFooter> headers) {
        this.headers = headers;
    }

    public List<HeaderFooter> getFooters() {
        return footers;
    }

    public void setFooters(List<HeaderFooter> footers) {
        this.footers = footers;
    }
}
