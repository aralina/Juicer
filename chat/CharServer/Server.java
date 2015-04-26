import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class Server implements HttpHandler {
    private List<JSONObject> history = new ArrayList<JSONObject>();
    private MessageExchange messageExchange = new MessageExchange();
    private JSONParser jsonParser = new JSONParser();
    private Date date;
    private PrintWriter out;

    public Server() throws IOException{
        out = new PrintWriter(new BufferedWriter(new FileWriter(new File("serverlog.txt"))));
    }

    public static void main(String[] args) {
        if (args.length != 1)
            System.out.println("Usage: java Server port");
        else {
            try {
                System.out.println("Server is starting...");
                Integer port = Integer.parseInt(args[0]);
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                System.out.println("Server started.");
                String serverHost = InetAddress.getLocalHost().getHostAddress();
                System.out.println("Get list of messages: GET http://" + serverHost + ":" + port + "/chat?token={token}");
                System.out.println("Send message: POST http://" + serverHost + ":" + port + "/chat provide body json in format {\"message\" : \"{message}\"} ");

                server.createContext("/chat", new Server());
                server.setExecutor(null);
                server.start();
            } catch (IOException e) {
                System.out.println("Error creating http server: " + e);
            }
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        date = new Date();
        out.flush();

        if ("GET".equals(httpExchange.getRequestMethod())) {
            response = doGet(httpExchange);
        } else if ("POST".equals(httpExchange.getRequestMethod())) {
            doPost(httpExchange);
        } else if("DELETE".equals(httpExchange.getRequestMethod())) {
            doDelete(httpExchange);
        } else {
            response = "Unsupported http method: " + httpExchange.getRequestMethod();
            date = new Date();
            out.flush();
        }

        sendResponse(httpExchange, response);
        date = new Date();
        out.flush();
    }

    private String doGet(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            Map<String, String> map = queryToMap(query);
            String token = map.get("token");
            if (token != null && !"".equals(token)) {
                date = new Date();
                out.println(date.toString() + " request method: GET");
                out.println(date.toString() + " request parameters: token: " + token);
                out.flush();
                int index = messageExchange.getIndex(token);
                return messageExchange.getServerResponse(history.subList(index, history.size()));
            } else {
                return "Token query parameter is absent in url: " + query;
            }
        }
        date = new Date();
        out.println(date.toString() + " Absent query in url");
        out.flush();
        return  "Absent query in url";
    }

    private void doPost(HttpExchange httpExchange) {
        try {
            JSONObject message = messageExchange.getClientMessage(httpExchange.getRequestBody());
            date = new Date();
            out.println(date.toString() + " request method: POST");
            out.println(date.toString() + " request parameters: " + message);
            out.flush();
            System.out.println("Get Message from User : " + message);
            history.add(message);
        } catch (ParseException e) {
            date = new Date();
            out.println(date.toString() + " Invalid user message");
            out.flush();
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void doDelete(HttpExchange httpExchange) {
        try {
            JSONObject message = messageExchange.getClientMessage(httpExchange.getRequestBody());
            for (JSONObject obj : history)
                if (obj.get("id").equals(message.get("id"))) {
                    obj.put("message", "DELETED");
                    obj.put("deleted","true");
                    out.println(date.toString() + " request method: DELETE");
                }
        } catch(ParseException e) {
            System.err.println("Invalid request message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void sendResponse(HttpExchange httpExchange, String response) {
        try {
            date = new Date();
            try {
                JSONObject resp = (JSONObject) jsonParser.parse(response.trim());
                out.println(date.toString() + " server response");
                out.println(date.toString() + "response parameters: token: " + resp.get("token")
                        + " messages: " + resp.get("messages"));
                out.flush();
            } catch (ParseException e) {

            }
            byte[] bytes = response.getBytes();
            Headers headers = httpExchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin","*");
            httpExchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = httpExchange.getResponseBody();
            os.write( bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
