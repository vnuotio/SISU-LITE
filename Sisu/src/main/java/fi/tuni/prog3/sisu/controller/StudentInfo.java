package fi.tuni.prog3.sisu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.tuni.prog3.sisu.api.SisuApiHandler;
import fi.tuni.prog3.sisu.models.Student;
import fi.tuni.prog3.sisu.models.StudentDataHolder;
import fi.tuni.prog3.sisu.models.StudyModule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.ComboBox;
/**
 * Acts as the controller for the login and registration screens of the program.
 */
public class StudentInfo implements Initializable {
    private static final Map<String, String> lblUserNameText = Map.of(
            "fi", "Nimi",
            "en", "Name");
    private static final Map<String, String> lblUserNumberText = Map.of(
            "fi", "Opiskelijanumero",
            "en", "Student Number");
    private static final Map<String, String> lblStudyProgramText = Map.of(
            "fi", "Opintosuunnitelma",
            "en", "Study Programme");
    private static final Map<String, String> lblGraduationDateText = Map.of(
            "fi", "Valmistumispvm.",
            "en", "Graduation Date");
    private static final Map<String, String> lblErrorUsedNumberText = Map.of(
            "fi", "Opiskelijanumero on jo käytössä!",
            "en", "Student number is already in use!");
    private static final Map<String, String> lblErrorNoNumberText = Map.of(
            "fi", "Virheellinen opiskelijanumero!",
            "en", "Student number not found!");
    private static final Map<String, String> lblErrorNullFieldText = Map.of(
            "fi", "Täytä kaikki kentät.",
            "en", "Please fill all fields!");
    private static final Map<String, String> lblErrorIllegalNumberText = Map.of(
            "fi", "Opiskelijanumero saa sisältää vain numeroita 0-9 sekä kirjaimia.",
            "en", "Student number may only include numbers 0-9 and letters");
    private static final Map<String, String> btnSigninText = Map.of(
            "fi", "Kirjaudu",
            "en", "Sign In");
    private static final Map<String, String> btnOpenRegisterFormText = Map.of(
            "fi", "Uusi käyttäjä",
            "en", "New Student");
    private static final Map<String, String> btnRegisterText = Map.of(
            "fi", "Rekisteröidy",
            "en", "Register");
    
    private List<StudyModule> degrees = new ArrayList<>();
    private static String lang = "fi"; // Sets the default language
    
    @FXML
    private ComboBox boxStudyPrograms;
    
    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUserName;
    
    @FXML
    private Label lblUserName;
    
    @FXML
    private Label lblUserNumber;
    
    @FXML
    private Label lblStudyProgram;
    
    @FXML
    private Label lblGraduationDate;

    @FXML
    private TextField txtUserNumber;

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnRegister;
    
    @FXML
    private Button btnOpenRegisterForm;
    
    @FXML
    private DatePicker dateGraduationDate;
    
    @FXML
    private Button btnSwitchLangFI;
    
    @FXML
    private Button btnSwitchLangEN;
    
    /**
     * String containing the location of the Students' data files. Contains %s 
     *  in the place of the student number.
     */
    public static final String STUDENTJSONPATH = "src/main/java/fi/tuni/prog3/sisu/resources/%s.json";

    /**
     * Acts as the handler for mouse button events for JavaFX files using 
     *  this class as controller.
     * @param event the MouseEvent that triggered this call
     */
    @FXML
    public void handleButtonAction(MouseEvent event) 
    {
        Object src = event.getSource();
        if (src == btnRegister) 
        {
            if (txtUserName.getText() == null || txtUserName.getText().trim().length() == 0
                    || txtUserNumber.getText() == null || txtUserNumber.getText().trim().length() == 0 ||
                    boxStudyPrograms.getValue() == null || dateGraduationDate.getValue() == null)
            {
                lblErrors.setText(lblErrorNullFieldText.get(lang));
                return;
            }
            String program = boxStudyPrograms.getValue().toString(); 
            String degId = null;
            for(StudyModule m : degrees)
            {
                if(m.getNameByLang(lang).equals(program))
                {
                    degId = m.getGroupId();
                }
            }
            if (createJsonFile(txtUserName.getText(), txtUserNumber.getText(), dateGraduationDate.getValue(), degId)) 
            {
                Student student = loadJsonData(txtUserNumber.getText());
                sendData(event, student);
            }
        }
        
        else if (src == btnOpenRegisterForm)
        {
            try
            {
                File file = new File("src/main/java/fi/tuni/prog3/sisu/views/StudentInfo.fxml");
                Scene scene = new Scene(FXMLLoader.load(file.toURI().toURL()));
                changeScene(scene, event);
            }
            catch (IOException ex)
            {
                // add proper error handling
                System.err.println(ex.getMessage());
            }
            
        }
        
        else if (src == btnSignin)
        {
            Student student = loadJsonData(txtUserNumber.getText()); // add proper error handling
            if(student != null)
            {
                sendData(event, student);
            }
        }
        
        else if (src == btnSwitchLangFI && !"fi".equals(lang))
        {
            changeLanguage("fi", true);
        }
        
        else if (src == btnSwitchLangEN && !"en".equals(lang))
        {
            changeLanguage("en", true);
        }
    }
    
    /**
     * Changes the language of this windows UI elements.
     * @param lang a two-letter short code for the language to switch to
     * @param reload whether or not to reload the degree tree. Should be true 
     *  when reloading during runtime, and false when starting up this window 
     *  as it reduces time to load.
     */
    private void changeLanguage(String lang, boolean reload)
    {
        StudentInfo.lang = lang;
        lblErrors.setText("");
        if (reload && boxStudyPrograms != null)
        {
            boxStudyPrograms.getItems().clear();
            addDegreeNames();
        }
        // Yes we have to use if's as LoginScreen and StudentInfoScreen 
        //  both use this class as controller.
        if (lblUserName != null)
        {
            lblUserName.setText(lblUserNameText.get(lang));
        }
        if (lblUserNumber != null)
        {
            lblUserNumber.setText(lblUserNumberText.get(lang));
        }
        if (lblStudyProgram != null)
        {
            lblStudyProgram.setText(lblStudyProgramText.get(lang));
        }
        if (lblGraduationDate != null)
        {
            lblGraduationDate.setText(lblGraduationDateText.get(lang));
        }
        if (btnRegister != null)
        {
            btnRegister.setText(btnRegisterText.get(lang));
        }
        if (btnOpenRegisterForm != null)
        {
            btnOpenRegisterForm.setText(btnOpenRegisterFormText.get(lang));
        }
        if(btnSignin != null)
        {
            btnSignin.setText(btnSigninText.get(lang));
        }
        if (btnRegister != null)
        {
            btnRegister.setText(btnRegisterText.get(lang));
        }
    }
    
    /**
     * Adds the names of all degrees to the drop-down menu in the registration 
     *  screen. Loads the degrees from a JSON file.
     */
    private void addDegreeNames()
    {
        degrees = SisuApiHandler.getAllDegreesFromJson();
        for(StudyModule m : degrees)
        {
            boxStudyPrograms.getItems().add(m.getNameByLang(lang));
        }
    }
    
    /**
     * Saves relevant data to a static object when switching windows to 
     *  {@link MainWindow MainWindow}. Relevant data includes language selection 
     *  and Student data.
     * @param event the MouseEvent that triggered this call. Used to get the 
     *  current stage, as Java does not support finding that without an object 
     *  belonging to the stage.
     * @param student the Student whose data to save and later load
     */
    private void sendData(MouseEvent event, Student student)
    {
        Node node = (Node) event.getSource();
        
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        try
        {
            StudentDataHolder holder = StudentDataHolder.getInstance();
            holder.setStudent(student);
            holder.setLang(lang);
            
            File file = new File("src/main/java/fi/tuni/prog3/sisu/views/MainWindow.fxml");
            Scene scene = new Scene(FXMLLoader.load(file.toURI().toURL()));
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e)
        {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }
    
    /**
     * Changes the scene to a new window. Used when opening the registration 
     *  screen from the login screen.
     * @param scene the scene to switch to
     * @param event the MouseEvent that triggered this call. Used to get the 
     *  current stage, as Java does not support finding that without an object 
     *  belonging to the stage.
     */
    private void changeScene(Scene scene, MouseEvent event)
    {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        
        stage.setScene(scene);
        stage.show();
    }
    
    private Student loadJsonData(String studentNumber) {
        Student student = null;
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            String j = String.format(STUDENTJSONPATH, studentNumber);
            File f = new File(j);
            if (!f.exists()) {
                lblErrors.setText(lblErrorNoNumberText.get(lang));
                return null;
            }
            student = mapper.readValue(f, Student.class);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return student;
    }
    
    /**
     * Saves the data, such as completed courses and degree programme of a 
     *  given Student and writes it to disk in JSON form.
     * @param student the Student whose data to save
     */
    public static void saveStudentData(Student student)
    {
        String studentNumber = student.getStudentNumber();
        ObjectMapper mapper = new ObjectMapper();
        Path path = Paths.get(String.format(STUDENTJSONPATH, studentNumber));
        try
        {
            mapper.writeValue(path.toFile(), student);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a JSON file for saving Student data if one does not already exist.
     * @param studentName name of the new Student
     * @param studentNumber unique id for the new Student, used as filename part
     * @param graduationDate expected graduation date the student has selected
     * @param degreeGroupId the ID of the degree programme the student has selected
     * @return true if creation succeeded, or false if it failed
     */
    private boolean createJsonFile(String studentName, String studentNumber,
            LocalDate graduationDate, String degreeGroupId)
    {
        File file = new File(String.format(STUDENTJSONPATH, studentNumber));

        if (!checkAlphaNumeric(studentNumber))
        {
            lblErrors.setText(lblErrorIllegalNumberText.get(lang));
            return false;
        }
        else if(file.exists())
        {
            lblErrors.setText(lblErrorUsedNumberText.get(lang));
            return false;
        }

        Student student = new Student(studentName, studentNumber,
                graduationDate.toString(), degreeGroupId);
        saveStudentData(student);
        return true;
    }
    
    /**
     * Checks if a given string only contains letters or numbers, no special 
     *  characters.
     * @param str the String to check
     * @return true if all characters are either letters or numbers, or false 
     *  not all are
     */
    private boolean checkAlphaNumeric(String str)
    {
        int len = str.length();
        for (int i = 0; i < len; i++)
        {
            char c = str.charAt(i);
            if (!Character.isLetterOrDigit(c))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(boxStudyPrograms != null)
        {
            addDegreeNames();
            changeLanguage(lang, false);
        }
    }
}