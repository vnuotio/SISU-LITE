package fi.tuni.prog3.sisu.models;

/**
 * Holds the data for a singular {@link Student Student}.
 */
public final class StudentDataHolder {
    
    private Student student;
    private String lang;
    
    private final static StudentDataHolder INSTANCE = new StudentDataHolder();
    
    /**
     * Empty constructor.
     */
    private StudentDataHolder()
    {
    }
    
    /**
     * Gets the static StudentDataHolder instance, which is used to access the 
     *  data store in this from anywhere.
     * @return the static instance
     */
    public static StudentDataHolder getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * Sets the Student associated with this.
     * @param student the associated student
     */
    public void setStudent(Student student)
    {
        this.student = student;
    }
    
    /**
     * Gets the current Student stored.
     * @return the current Student
     */
    public Student getStudent()
    {
        return this.student;
    }
    
    /**
     * Sets the current language used in the previous window, which is used 
     *  to load the next window in the correct language
     * @param lang a two-letter short code for the language
     */
    public void setLang(String lang)
    {
        this.lang = lang;
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
