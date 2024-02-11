package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import routes.Router;

public class Request {

	private String resource;
	private String resourceId;
	private String sub_resource;
	private String action;
	private HashMap<String, ?> query;
	private InputStream inputStream;
	private Socket client;
	public Factory factory;
	public HashMap<String, ?> requestBody;

	public Request(Socket client, Factory factory) throws IOException {
		this.client = client;
		this.inputStream = client.getInputStream();
		this.factory = factory;
		this.query = new HashMap<>();
	}

	public HashMap<String, ?> getRequestBody() {
		return this.requestBody;
	}

	public String getResource() {
		return this.resource;
	}

	public String getAction() {
		return this.action;
	}

	public HashMap<String, ?> getQuery() {
		return this.query;
	}

	public Socket getClient() {
		return this.client;
	}

	public String getResourceId() {
		return this.resourceId;
	}

	public String getSubResource() {
		return this.sub_resource;
	}

	public String getRequestedEndpoint() {
		StringBuilder requested_endpoint = new StringBuilder(this.resource);
		if (this.resourceId != null)
			requested_endpoint.append("/" + this.resourceId);
		if (this.sub_resource != null)
			requested_endpoint.append("/" + this.sub_resource);

		return requested_endpoint.toString();
	}

	public OutputStream handleIncommingRequest(Socket socket, Router router) throws IOException {

		StringBuilder stringBuilder = this.buildRequest();

		String[] requestInfo = stringBuilder.toString().split("\n")[0].split(" ");

		this.action = requestInfo[0];
		this.buildParts(requestInfo[1]);

		if (requestInfo[1].contains("?"))
			this.query = this.buildQuery(requestInfo[1].split("\\?")[1]);

		return router.forwardRequest(this);
	}

	private void buildParts(String requestInfo) {
		final String resourceString = requestInfo.split("\\?")[0];
		final String[] resource_parts = resourceString.split("/");

		this.resource = "/" + (resource_parts.length > 1 ? resource_parts[1] : "");
		this.resourceId = resource_parts.length > 2 ? resource_parts[2] : null;
		this.sub_resource = resource_parts.length > 3 ? resource_parts[3] : null;

	}

	private StringBuilder buildRequest() throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(this.inputStream);
		int contentLength = 0;
		BufferedReader br = new BufferedReader(isr);
		String content_length_key = "Content-Length:";

		String line = br.readLine();

		// read request
		while (!line.isBlank()) {
			if (line.toLowerCase().startsWith(content_length_key.toLowerCase())) {
				contentLength = Integer.parseInt(line.substring(content_length_key.length()).trim());
			}
			stringBuilder.append(line);
			line = br.readLine();
		}

		// read requestBody
		StringBuilder requestBody = new StringBuilder();
		int read;
		int totalRead = 0;
		char[] buffer = new char[1024];
		while ((read = br.read(buffer, 0, Math.min(buffer.length, contentLength - totalRead))) != -1
				&& totalRead < contentLength) {
			requestBody.append(buffer, 0, read);
			totalRead += read;
		}
		this.requestBody = buildRequestBody(requestBody);

		return stringBuilder;
	}

	private HashMap<String, ?> buildQuery(String queryString) {
		final String[] queryStrings = queryString.split("&");
		final HashMap<String, String> query = new HashMap<String, String>();

		for (int i = 0; i < queryStrings.length; i++) {
			final String[] queryParam = queryStrings[i].split("=");
			final String queryKey = queryParam[0];
			final String queryValue = queryParam[1];

			query.put(queryKey, queryValue);

		}

		return query;
	}

	private HashMap<String, ?> buildRequestBody(StringBuilder requestBody) {
		HashMap<String, String> body = new HashMap<>();

		String[] entries = requestBody.toString().split(",");

		for (String entry : entries) {
			String[] parts = entry.split(":");
			String key = parts[0].split("\n")[1].trim();
			String value = parts[1].split("\n")[0].trim();

			body.put(key, value);

		}

		return body;
	}
}
