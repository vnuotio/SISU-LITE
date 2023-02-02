package fi.tuni.prog3.sisu.controller;

import fi.tuni.prog3.sisu.models.CourseDataHolder;
import fi.tuni.prog3.sisu.models.CourseModule;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Acts as the controller for windows containing details for {@link CourseModule courses}.
 */
public class CourseViewController implements Initializable{
    private static final Map<String, String> courseCompletedBtnText = Map.of(
            "fi", "Suoritettu",
            "en", "Completed");
    private static final Map<String, String> courseNotCompletedBtnText = Map.of(
            "fi", "Merkitse suoritetuksi",
            "en", "Mark as Completed");
    private static final Map<String, String> outcomesTitleText = Map.of(
            "fi", "Oppimistavoitteet",
            "en", "Learning Outcomes");
    private static final Map<String, String> contentTitleText = Map.of(
            "fi", "Kurssin sisältö",
            "en", "Course Contents");
    
    @FXML
    private Button courseCompletedBtn;
    
    @FXML
    private Label courseNameLbl;
    
    @FXML
    private Button closeBtn;
    
    @FXML
    private Label shortContentLbl;
    
    @FXML
    private Label outcomesTitleLbl;
    
    @FXML
    private Label contentTitleLbl;
    
    @FXML
    private WebView studyGoals;
    
    @FXML
    private WebView courseContent;
    
    private MainWindow mainWindow;
    private CourseModule course;
    private TreeItem ti;
    private boolean completed;
    private String lang;
    
    /**
     * Acts as the handler for mouse events in JavaFX files using this class 
     *  as controller.
     * @param event the MouseEvent that triggered this call
     */
    @FXML
    public void handleButtonAction(MouseEvent event)
    {
        Object src = event.getSource();
        if (src == closeBtn)
        {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        }
        else if (src == courseCompletedBtn)
        {
            markCompleted();
        }
    }
    
    /**
     * Marks the course within this window completed. Also updates this data 
     *  on the Student currently using this and the visible degree tree.
     */
    private void markCompleted()
    {
        mainWindow.getStudent().addCourseCompletion(course.getGroupId());
        
        courseCompletedBtn.setDisable(true);
        courseCompletedBtn.setText(courseCompletedBtnText.get(lang));
        
        mainWindow.updateCreditCount(course, ti);
        ti.setValue(MainWindow.PASSED_COURSE);
    }
    
    /**
     * Used to load correct information on the UI elements of this window 
     *  immediately after initialization.
     */
    private void loadView()
    {
        courseCompletedBtn.setDisable(completed);
        
        if (completed)
        {
            courseCompletedBtn.setText(courseCompletedBtnText.get(lang));
        }
        else
        {
            courseCompletedBtn.setText(courseNotCompletedBtnText.get(lang));
        }
        
        courseNameLbl.setText(course.getNameByLang(lang));
        
        shortContentLbl.setText(course.getShortContent(lang));
        
        outcomesTitleLbl.setText(outcomesTitleText.get(lang));
        studyGoals.getEngine().loadContent(course.getOutcomes(lang));
        
        contentTitleLbl.setText(contentTitleText.get(lang));
        courseContent.getEngine().loadContent(course.getContent(lang));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        mainWindow = MainWindow.getInstance();
        CourseDataHolder holder = CourseDataHolder.getInstance();
        course = holder.getCourse();
        ti = holder.getTreeItem();
        completed = holder.getCompleted();
        lang = holder.getLang();
        
        loadView();
    }

}
