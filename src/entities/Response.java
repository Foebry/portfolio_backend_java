package entities;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import annotations.Column;
import annotations.Serialize;

public class Response extends Serializable {

    @Column(type = "number")
    @Serialize()
    protected int code;

    @Column(type = "string")
    @Serialize()
    protected String status;

    @Column(type = "String")
    @Serialize()
    protected String message;

    private String responseData;
    private Map<String, String> schemaError = new HashMap<>();
    private Socket client;

    public Response(Socket client) {
        this.client = client;
    }

    public void setStatus(int statusCode) {
        this.code = statusCode;
    }

    public void setData(String responseData) {
        this.responseData = responseData;
    }

    public Map<String, String> getSchemaError() {
        return this.schemaError;
    }

    public OutputStream respond(Boolean serialize) throws IOException {
        OutputStream response = client.getOutputStream();
        this.responseData = serialize != false ? this.serialize((Class<?>) null) : this.responseData;

        response.write(("HTTP/1.1 " + this.code + " " + this.status + "\r\n").getBytes());
        response.write(("\r\n").getBytes());
        response.write((this.responseData).getBytes());

        return response;

    }

    public OutputStream ok(String responseData) throws IOException {
        this.code = 200;
        this.status = "OK";
        this.responseData = responseData;
        return this.respond(false);
    }

    public OutputStream badSchema() throws IOException {
        this.code = 400;
        this.status = "Bad Request";
        this.responseData = this.serialize(this.schemaError);
        return this.respond(false);
    }

    public OutputStream badRequest(String responseData) throws IOException {
        this.code = 400;
        this.status = "Bad Request";
        this.message = responseData;
        return this.respond(true);
    }

    public OutputStream notFound() throws IOException {
        this.code = 404;
        this.status = "Not Found";
        this.message = "The resource you tried to access does not exist";
        return this.respond(true);
    }

    public OutputStream routeNotFound() throws IOException {
        this.code = 404;
        this.status = "Not Found";
        this.message = "Are you lost? The requested route does not exist.";
        return this.respond(true);
    }

    public OutputStream resourceNotFound() throws IOException {
        this.code = 404;
        this.status = "Not Found";
        this.message = "The requested resource was not found";

        return this.respond(true);
    }

    public OutputStream notAllowed() throws IOException {
        this.code = 405;
        this.status = "Not Allowed";
        this.message = "Not Allowed";
        return this.respond(true);
    }

    public OutputStream noContent() throws IOException {
        this.code = 204;
        this.status = "noContent";
        this.responseData = "";
        return this.respond(true);
    }

    public OutputStream internalServerError() throws IOException {
        this.code = 500;
        this.status = "Internal Server Error";
        this.message = "Something went wrong...";
        return this.respond(true);

    }
}
