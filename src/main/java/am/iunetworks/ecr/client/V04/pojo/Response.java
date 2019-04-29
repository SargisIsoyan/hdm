package am.iunetworks.ecr.client.V04.pojo;

public class Response<TData> {
    private int statusCode;
    private TData data;

    public Response(int statusCode, TData data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public TData getData() {
        return data;
    }
}
