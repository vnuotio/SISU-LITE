package fi.tuni.prog3.sisu;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.tuni.prog3.sisu.api.SisuApiHandler;
import fi.tuni.prog3.sisu.models.StudyModule;
import fi.tuni.prog3.sisu.utility.Urls;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Sisu extends Application 
{

    @Override
    public void start(Stage stage) throws IOException 
    {
        ArrayList<StudyModule> programs = SisuApiHandler.getAllDegrees(Urls.DEGREES_URL.getUrl());
        ObjectMapper objmapper = new ObjectMapper();
        try
        {
            objmapper.writeValue(Paths.get(SisuApiHandler.getDegreeJsonPath()).toFile(), programs);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        Parent root = null;
        
        URL url = new File("src/main/java/fi/tuni/prog3/sisu/views/LoginWindow.fxml").toURI().toURL();
        try
        {           
            root = FXMLLoader.load(url);
        }
        catch(IOException e)
        {
            System.out.println("Bad Url");
        }
        
        if(root != null)
        {
            //String css = this.getClass().getResource("src/main/java/fi/tuni/prog3/sisu/styles/style.css").toExternalForm();
            var scene = new Scene(root);
            //scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } 
    }

    public static void main(String[] args) 
    {
        launch(args);
    }

}