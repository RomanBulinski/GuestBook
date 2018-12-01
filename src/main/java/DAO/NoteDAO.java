package DAO;

import MODEL.Note;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO{


    public Connection connection () throws SQLException {
        String jdbUrl = "jdbc:postgresql://localhost:5432/guest_book";
        String user = "guestBookadmin";
        String password = "admin";
        Connection connection = DriverManager.getConnection(jdbUrl, user, password);
        return connection;
    }

    public Boolean addNote( Integer id,String note, String name, String time ) {
        PreparedStatement preparedStatement = null;
        try{
            Connection connection = connection();
            preparedStatement = connection.prepareStatement("INSERT INTO notes_store ( ID, NOTE,NAME,TIME) VALUES(?,?,?,?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, note);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, time);

            preparedStatement.executeUpdate();
            System.out.println("Inserted successfully");

            connection.close();

        } catch(Exception ex){
            ex.printStackTrace();
        }
        return true;
    }


    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = connection();
            preparedStatement = connection.prepareStatement("SELECT * FROM notes_store ");
            resultSet = preparedStatement.executeQuery();
            notes = getData(resultSet);
            resultSet.close();
            connection.close();
        }
        catch (SQLException exc) {
            exc.printStackTrace();
        }
        return notes;
    }

    private List<Note> getData(ResultSet resultSet)  {
        List<Note> notes = new ArrayList<>();
        try{
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String gusetnote = resultSet.getString("note");
                String name = resultSet.getString("name");
                String time = resultSet.getString("time");
                Note note = new Note( id ,gusetnote, name ,time);
                notes.add(note);
            }
        }catch(SQLException e ){
            e.printStackTrace();
        }
        return notes;
    }


}
