package old;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Form implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();

        // Send a form if it wasn't submitted yet.
        if(method.equals("GET")){
            response = "<html><body>" +
                    "<form method=\"POST\">\n" +

                    "  First name:<br>\n" +
                    "  <input type=\"text\" name=\"firstname\" value=\"Kacor\">\n" +
                    "  <br>\n" +

                    "  Last name:<br>\n" +
                    "  <input type=\"text\" name=\"lastname\" value=\"Donald\">\n" +
                    "  <br><br>\n" +

                    "  Notes:<br>\n" +
                    "  <input type=\"text\" name=\"notes\" value=\"...\">\n" +
                    "  <br><br>\n" +

                    "  <input type=\"submit\" value=\"Submit\">\n" +
                    "</form> " +
                    "</body></html>";
        }

        // If the form was submitted, retrieve it's content.
        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            System.out.println(formData);
            Map inputs = parseFormData(formData);

            response = "<html><body>" +
                    "<h1>Hello " +
                    inputs.get("firstname") + " " + inputs.get("lastname") +" " + inputs.get("notes") +
                    "!</h1>" +
                    "</body><html>";
        }


        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }


    /**
     * Form data is sent as a urlencoded string. Thus we have to parse this string to get data that we want.
     * See: https://en.wikipedia.org/wiki/POST_(HTTP)
     */
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