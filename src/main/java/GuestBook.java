import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class GuestBook implements HttpHandler {

    int counter = 0;
    String message = "";
    String currentTime= null;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();

//         Send a form if it wasn't submitted yet.
        if(method.equals("GET")){

            response = "<html><body>" +

                    "<h1>Simple guestbook</h1>" +

                    "<form method=\"POST\">\n" +
                    "  First name:<br>\n" +
                    "  <input type=\"text\" name=\"firstname\" value=\"\">\n" +
                    "  <br>\n" +

                    "  Notes:<br>\n" +
                    "  <input type=\"text\" name=\"notes\" value=\"\">\n" +
                    "  <br><br>\n" +

                    "  <input type=\"submit\" value=\"Submit\">\n" +
                    "</form> " +
                    "</body></html>";
        }

        // If the form was submitted, retrieve it's content.
        if(method.equals("POST")){
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String formData = bufferedReader.readLine();
            System.out.println(formData);
            Map inputs = parseFormData(formData);

            SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");

            currentTime = time_formatter.format(System.currentTimeMillis());
            counter++;
            message =  message +    counter + "<br>"+
                    inputs.get("notes") + "<br>"+
                    "Name : "+inputs.get("firstname")+"<br>"+
                    "Date : " +currentTime +"<br><br>";

            response = "<html><body>" +

                    "<h1>Simple guestbook</h1>" +
                    "<h4>"+message+"</h4>"+

                    "<form method=\"POST\">\n" +

                    "  Notes:<br>\n" +
                    "  <input type=\"text\" name=\"notes\" value=\"\">\n" +
                    "  <br><br>\n" +

                    "  First name:<br>\n" +
                    "  <input type=\"text\" name=\"firstname\" value=\"\">\n" +
                    "  <br>\n" +

                    "  <input type=\"submit\" value=\"Submit\">\n" +
                    "</form> " +
                    "</body><html>";
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }


    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

}