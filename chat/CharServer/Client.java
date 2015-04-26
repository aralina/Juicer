/**
 * Created by home on 26.04.15.
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class Client implements Runnable {

    private List<JSONObject> history = new ArrayList<JSONObject>();
    private MessageExchange messageExchange = new MessageExchange();
    private String host;
    private Integer port;
    private PrintWriter out;
    private String username;
    private Date date;

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;
    }


    public static void main(String[] args) throws IOException{
        if (args.length != 2)
            System.out.println("Usage: java ChatClient host port");
        else {
            System.out.println("Connection to server...");
            String serverHost = args[0];
            Integer serverPort = Integer.parseInt(args[1]);
            Client client = new Client(serverHost, serverPort);
            client.usernameInput();
            new Thread(client).start();
            System.out.println("Connected to server: " + serverHost + ":" + serverPort);
            client.listen();
        }
    }

    private void usernameInput() throws IOException {
        System.out.println("Input your name: ");
        Scanner sc = new Scanner(System.in);
        username = sc.nextLine();
    }

    private HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("http://" + host + ":" + port + "/chat?token=" + messageExchange.getToken(history.size()));
        date = new Date();
        out.println(date.toString() + " request parameters: url: " + url);
        out.flush();
        return (HttpURLConnection) url.openConnection();
    }

    public List<JSONObject> getMessages() {
        List<JSONObject> list = new ArrayList<JSONObject>();
        HttpURLConnection connection = null;
        try {
            date = new Date();
            out.println(date.toString() + " request begin");
            out.println(date.toString() + " request method: GET");
            out.flush();
            connection = getHttpURLConnection();
            connection.connect();
            String response = messageExchange.inputStreamToString(connection.getInputStream());
            JSONObject jsonObject = messageExchange.getJSONObject(response);
            JSONArray jsonArray = (JSONArray) jsonObject.get("messages");
            date = new Date();
            out.println(date.toString() + " server response parameters: messages: " + jsonArray + " token: " + jsonObject.get("token"));
            out.flush();
            for (Object o : jsonArray) {
                System.out.println(o.toString());
                list.add((JSONObject)o);
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return list;
    }

    public void sendMessage(String message) {
        HttpURLConnection connection = null;
        try {
            date = new Date();
            out.println(date.toString() + " request begin");
            out.println(date.toString() + " request method: POST");
            out.flush();
            connection = getHttpURLConnection();
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            byte[] bytes = messageExchange.getClientSendMessageRequest(message, username).getBytes();
            wr.write(bytes, 0, bytes.length);
            wr.flush();
            wr.close();

            date = new Date();
            out.println(date.toString() + " request parameters: username: " + username + " message: " + message);
            out.flush();
            connection.getInputStream();

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
                date = new Date();
                out.println(date.toString() + " request end");
                out.flush();
            }
        }
    }

    public void listen() {
        while (true) {
            List<JSONObject> list = getMessages();

            if (list.size() > 0) {
                history.addAll(list);
            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String message = scanner.nextLine();
            sendMessage(message);
        }
    }
}

