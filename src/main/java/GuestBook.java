import DAO.NoteDAO;
import MODEL.Note;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuestBook implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();
        JtwigTemplate layout = JtwigTemplate.classpathTemplate("templates/layout.twig");
        JtwigModel model = JtwigModel.newModel();

        if(method.equals("GET")){
            String message = makeMessage();
            model.with("message", message);
            response = layout.render(model);
        }
        // If the form was submitted, retrieve it's content.
        if(method.equals("POST")){


            String message = makeMessage();

            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String formData = bufferedReader.readLine();
            System.out.println(formData);
            Map inputs = parseFormData(formData);

            SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
            NoteDAO guestnote = new NoteDAO();
            List<Note> allNotes = guestnote.getAllNotes();
            int counter = allNotes.size();
            counter++;
            String note = (String)inputs.get("notes");
            String name = (String)inputs.get("firstname");
            String currentTime = time_formatter.format(System.currentTimeMillis());

            message =  message + "<br>"+ counter + "<br>"+
                    note +"<br>"+ name +"<br>"+currentTime+"<br><br>";

            // add note toDB via postgres
            Boolean addigResult = guestnote.addNote(counter,note,name,currentTime);
            System.out.println(addigResult);

            model.with("message", message);
            response = layout.render(model);

        }
        sendResponse( httpExchange ,response);
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

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }


    private String makeMessage() {
        NoteDAO guestnote = new NoteDAO();
        String message = "";
        List<Note> allNotes = guestnote.getAllNotes();
        for (int i = 0; i < allNotes.size(); i++) {
            Integer id = allNotes.get(i).getId();
            String note = allNotes.get(i).getGuestNote();
            String name = allNotes.get(i).getName();
            String time = allNotes.get(i).getTime();
                message = message + "<br>"+
                        "<font color=\"red\" size=\"3\">"+id+" "+"</font>"+
                        note+"<br>"+name+"<br>"+time+"<br>";
        }
        return message;
    }

}