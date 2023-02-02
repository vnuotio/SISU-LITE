package fi.tuni.prog3.sisu.models;

import javafx.scene.control.TreeItem;

/**
 * Holds the data for a singular {@link CourseModule CourseModule}.
 */
public class CourseDataHolder {
    
    private static final CourseDataHolder INSTANCE = new CourseDataHolder();
    private CourseModule course;
    private TreeItem ti;
    private boolean completed;
    private String lang;
    
    /**
     * Empty constructor.
     */
    private CourseDataHolder()
    {
        
    }
    
    /**
     * Sets the CourseModule associated with this.
     * @param course the associated course
     */
    public void setCourse(CourseModule course)
    {
        this.course = course;
    }
    
    /**
     * Sets the {@link TreeItem TreeItem} the course is associated with 
     *  in the main window.
     * @param ti the associated TreeItem
     */
    public void setTreeItem(TreeItem ti)
    {
        this.ti = ti;
    }
    
    /**
     * Sets whether this course has already been completed
     * @param b completion status
     */
    public void setCompleted(boolean b)
    {
        completed = b;
    }
    
    /**
     * Sets the current language used in the main window, which is used 
     *  to load the details of the associated course in the correct language
     * @param lang a two-letter short code for the language
     */
    public void setLang(String lang)
    {
        this.lang = lang;
    }
    
    /**
     * Gets the static CourseDataHolder instance, which is used to access the 
     *  data store in this from anywhere.
     * @return the static instance
     */
    public static CourseDataHolder getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * Gets the current CourseModule stored.
     * @return the current CourseModule
     */
    public CourseModule getCourse()
    {
        return course;
    }
    
    /**
     * Gets the TreeItem the course is associated with in the main window.
     * @return the current TreeItem
     */
    public TreeItem getTreeItem()
    {
        return ti;
    }
    
    /**
     * Gets the completion status of this course.
     * @return the boolean completion status
     */
    public boolean getCompleted()
    {
        return completed;
    }
    
    /**
     * Gets the currently used language.
     * @return a two-letter short code for the language
     */
    public String getLang()
    {
        return lang;
    }
}
