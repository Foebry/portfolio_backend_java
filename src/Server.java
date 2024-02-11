import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import routes.Router;
import services.*;

public class Server {

	public static void main(String[] args) throws IOException {

		Router router = new Router();
		Factory factory = new Factory();

		try (ServerSocket server = new ServerSocket(8080)) {
			System.out.println("Server started on port 8080.");
			System.out.println("Listening for incomming requests...");

			while (true) {
				try (Socket client = server.accept()) {

					final Request request = new Request(client, factory);
					final OutputStream response = request.handleIncommingRequest(client, router);
					response.flush();

					client.close();
				}
			}
		}
	}
}
