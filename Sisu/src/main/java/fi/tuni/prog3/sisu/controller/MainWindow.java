package fi.tuni.prog3.sisu.controller;

import fi.tuni.prog3.sisu.api.SisuApiHandler;
import fi.tuni.prog3.sisu.models.CourseDataHolder;
import fi.tuni.prog3.sisu.models.CourseModule;
import fi.tuni.prog3.sisu.models.Student;
import fi.tuni.prog3.sisu.models.StudentDataHolder;
import fi.tuni.prog3.sisu.models.StudyModule;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView; 
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Acts as the controller for the main window view of this program. The main 
 *  windows main purpose is to display the degree programme of the user.
 */
public class MainWindow implements Initializable{
    private static final Map<String, String> studentInfoBtnText = Map.of(
            "fi", "Opintotiedot",
            "en", "Study Information");
    private static final Map<String, String> switchProgrammeInfoLblText = Map.of(
            "fi", "Valitse opinto-ohjelma",
            "en", "Choose a Study Programme");
    private static final Map<String, String> switchProgrammeBtnText = Map.of(
            "fi", "Vaihda",
            "en", "Switch to");
    private static final Map<String, String> viewProgrammeBtnText = Map.of(
            "fi", "Tarkastele",
            "en", "View");
    private static final Map<String, String> saveBtnText = Map.of(
            "fi", "Tallenna",
            "en", "Save");
    private static final Map<String, String> saveExitBtnText = Map.of(
            "fi", "Tallenna ja poistu",
            "en", "Save and Exit");
    private static final Map<String, String> creditText = Map.of(
            "fi", "op",
            "en", "cr");
    
    @FXML
    private TreeView TreeWeb;
    
    @FXML
    private Button studentInfoBtn;
    
    @FXML
    private Button switchLangBtnEN;
    
    @FXML
    private Button switchLangBtnFI;
    
    @FXML
    private Label switchProgrammeInfoLbl;
    
    @FXML
    private ComboBox switchProgrammeBox;
    
    @FXML
    private Button resetProgrammeBtn;
    
    @FXML
    private Button switchProgrammeBtn;
    
    @FXML
    private Button viewProgrammeBtn;
    
    @FXML
    private Button saveBtn;
    
    @FXML
    private Button saveExitBtn;
    
    private static MainWindow instance;
    
    /**
     * String to display next to completed courses in the tree.
     */
    public static final String PASSED_COURSE = "Passed";
    private static final String NOT_PASSED_COURSE = "";
    
    private ArrayList<StudyModule> degrees;
    private Student student;
    
    private String originalProgrammeId;
    private String currentProgrammeId;
    private String lang = "fi"; //Sets the default language
    
    /**
     * Gets the static MainWindow instance currently controlling the main window. 
     *  Used to access the methods and values from this controller from 
     *  {@link CourseViewController CourseViewController}.
     * @return the MainWindow instance currently controlling the main window
     */
    public static MainWindow getInstance()
    {
        return instance;
    }
    
    /**
     * Acts as the handler for mouse button events for JavaFX files using 
     *  this class as controller.
     * @param event the MouseEvent that triggered this call
     */
    @FXML
    public void handleButtonAction(MouseEvent event)
    {
        Object src = event.getSource();
        if (src == switchLangBtnEN && !"en".equals(lang))
        {
            changeLanguage("en", true);
        }
        else if (src == switchLangBtnFI && !"fi".equals(lang))
        {
            changeLanguage("fi", true);
        }
        else if (src == resetProgrammeBtn)
        {
            resetDegreeTree();
            getStudent().setDegreeGroupId(originalProgrammeId);
        }
        else if (src == viewProgrammeBtn)
        {
            switchDegreeTree();
        }
        else if (src == switchProgrammeBtn)
        {
            switchDegreeTree();
            getStudent().setDegreeGroupId(currentProgrammeId);
        }
        else if (src == saveBtn)
        {
            StudentInfo.saveStudentData(getStudent());
        }
        else if (src == saveExitBtn)
        {
            StudentInfo.saveStudentData(getStudent());
            Platform.exit();
            System.exit(0);
        }
    }
    /**
     * Loads a given degree programme and starts buildings a JavaFX TreeView 
     *  model of it. Clears the previous tree.
     * @param treeview the root under which to build the tree
     * @param programId id for the degree programme to load on this tree
     */
    @FXML
    private void loadStudies(TreeView treeview, String programId)
    { 
        currentProgrammeId = programId;
        treeview.setRoot(null);
        
        switchProgrammeBox.getItems().clear();
        for (StudyModule sm : degrees)
        {
            switchProgrammeBox.getItems().add(sm.getNameByLang(lang));
        }
  
        StudyModule module = SisuApiHandler.getStudyModule(programId, null);
        
        module.clearModuleTree();
        module.buildModuleTree();
        
        TreeItem rootItem = new TreeItem("");
        
        buildTree(module, rootItem);
        
        treeview.setShowRoot(false);
        treeview.setRoot(rootItem);
    }
    
    /**
     * Adds the {@link CourseModule CourseModules} and {@link StudyModule StudyModules} 
     * under given module under it to the JavaFX TreeView. Calculates total earned 
     *  credits for each module for completed courses. Recursively builds the tree 
     *  for its children.
     * @param studyModule current StudyModule under which to add courses and 
     *  modules, and whose children to build additional trees under
     * @param treeItem TreeItem associated with studyModule, under which to add 
     *  generated courses and modules
     * @return the amount of credits calculated from completed courses under this 
     *  module
     */
    private int buildTree(StudyModule studyModule, TreeItem treeItem)
    {
        Integer moduleCredits = 0;
        TreeItem moduleItem = new TreeItem(studyModule.getNameByLang(lang));
        Label moduleCreditLbl = new Label();
        
        moduleItem.setGraphic(moduleCreditLbl);
        treeItem.getChildren().add(moduleItem);
        
        for(StudyModule sm : studyModule.getChildStudyModules())
        {
            moduleCredits += buildTree(sm, moduleItem);
        }
        
        for(CourseModule cm : studyModule.getChildCourses())
        {   
            Button coursebtn = new Button();
            String courseStatus = NOT_PASSED_COURSE;
            boolean completed = getStudent()
                    .getCourses()
                    .contains(cm.getGroupId());
            
            if (completed)
            {
                courseStatus = PASSED_COURSE;
                moduleCredits += cm.getMinCredits();
            }
            
            TreeItem course = new TreeItem(courseStatus);
            
            String coursebtnText = String.format("(%d %s) %s",
                    cm.getMinCredits(), creditText.get(lang), cm.getNameByLang(lang));
            coursebtn.setText(coursebtnText);
            
            coursebtn.setOnAction((ActionEvent t) -> {
                loadCourseInfo(cm, course);
            });
            
            Tooltip tt = new Tooltip(cm.getShortContent(lang));
            tt.setPrefWidth(200);
            tt.setWrapText(true);
            
            coursebtn.setTooltip(tt);
            
            course.setGraphic(coursebtn);
            moduleItem.getChildren().add(course);
        }
        
        moduleCreditLbl.setText(moduleCredits.toString());
        
        return moduleCredits;
    }
    
    private void loadCourseInfo(CourseModule course, TreeItem ti)
    {
        boolean completed = getStudent()
                .getCourses()
                .contains(course.getGroupId());
        
        CourseDataHolder holder = CourseDataHolder.getInstance();
        holder.setCourse(course);
        holder.setTreeItem(ti);
        holder.setCompleted(completed);
        holder.setLang(lang);
        try
        {
            Scene courseScene = new Scene(FXMLLoader.load(
                    new File("src/main/java/fi/tuni/prog3/sisu/views/CourseInfo.fxml")
                            .toURI()
                            .toURL()));
            
            Stage courseStage = new Stage();
            courseStage.setScene(courseScene);
            courseStage.initStyle(StageStyle.UNDECORATED);
            
            courseStage.show();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Updates the module tree by working recursively upwards to have the   
     *  correct amount of credits when a course has been passed.
     * @param cm the coursemodule that was completed
     * @param ti the study module TreeItem this course is associated with. 
     *  Set automatically when generating buttons.
     */
    public void updateCreditCount(CourseModule cm, TreeItem ti)
    {
        Integer addCredits = cm.getMinCredits();
        TreeItem parent = ti.getParent();
        Object graphic = ti.getGraphic();
        Label creditLbl = null;
        
        if (parent == null || graphic == null)
        {
            return;
        }
        
        Integer credits = 0;
        try
        {
            creditLbl = (Label) graphic;
            credits = Integer.parseInt(creditLbl.getText());
        }
        catch (NumberFormatException ex)
        {
            credits = 0;
        }
        catch (ClassCastException ex)
        {
            updateCreditCount(cm, parent);
            return;
        }
        
        credits += addCredits;
        creditLbl.setText(credits.toString());
        
        updateCreditCount(cm, parent);
    }
    
    /**
     * Changes the language of the main window, including the UI elements and 
     *  the degree tree, but not including the course view window.
     * @param lang a two-letter short code for the language to switch to
     * @param reload whether or not to reload the degree tree. Should be true 
     *  when reloading during runtime, and false when starting up this window 
     *  as it reduces time to load.
     */
    private void changeLanguage(String lang, boolean reload)
    {
        this.lang = lang;
        if (reload)
        {
            loadStudies(TreeWeb, currentProgrammeId);
        }
        studentInfoBtn.setText(studentInfoBtnText.get(lang));
        switchProgrammeInfoLbl.setText(switchProgrammeInfoLblText.get(lang));
        switchProgrammeBtn.setText(switchProgrammeBtnText.get(lang));
        viewProgrammeBtn.setText(viewProgrammeBtnText.get(lang));
        saveBtn.setText(saveBtnText.get(lang));
        saveExitBtn.setText(saveExitBtnText.get(lang));
    }
    
    /**
     * Resets the degree tree to the original degree programme of the student.
     */
    private void resetDegreeTree()
    {
        if (!currentProgrammeId.equals(originalProgrammeId))
        {
            loadStudies(TreeWeb, originalProgrammeId);
        }
    }
    
    /**
     * Switches the degree tree to one selected from the drop-down menu in 
     *  the ui.
     */
    private void switchDegreeTree()
    {
        if (switchProgrammeBox.getValue() == null)
        {
            return;
        }
        String newDegName = switchProgrammeBox.getValue().toString();
        String id = getDegreeIdByName(newDegName);
        if (id != null && !currentProgrammeId.equals(id))
        {
            loadStudies(TreeWeb, id);
        }
    }
    
    /**
     * Compares all degrees and returns the ID of the first one whose name 
     *  matches given name.
     * @param name degree name to match against
     * @return id of matching degree, otherwise null
     */
    private String getDegreeIdByName(String name)
    {
        if (name == null)
        {
            return null;
        }
        for (StudyModule sm : degrees)
        {
            if (name.equals(sm.getNameByLang(lang)))
            {
                return sm.getGroupId();
            }
        }
        return null;
    }
    
    /**
     * Returns the Student object currently using the program.
     * @return the Student object
     */
    public Student getStudent()
    {
        return student;
    }
    
    /**
     * Loads data from previous window, containing the Student using this 
     *  and language.
     */
    private void receiveData()
    {
        degrees = SisuApiHandler.getAllDegreesFromJson();
        StudentDataHolder holder = StudentDataHolder.getInstance();
        student = holder.getStudent();
        lang = holder.getLang();
        originalProgrammeId = getStudent().getDegreeGroupId();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        receiveData();
        changeLanguage(lang, false);
        loadStudies(TreeWeb, getStudent().getDegreeGroupId());    
    }
    
}
