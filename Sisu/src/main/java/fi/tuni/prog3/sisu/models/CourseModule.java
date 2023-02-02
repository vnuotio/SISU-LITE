package fi.tuni.prog3.sisu.models;

import java.util.Map;

/**
 * A class for CourseModules(courses) from the Sisu API. Instantiated from JSON 
 *  data using Jackson deserializer.
 */
public class CourseModule extends SisuModule{
    private static final String NO_CONTENT_MSG = "No summary given";
    private static final String NO_OUTCOMES_MSG = "No learning outcomes given";
    
    private Map<String, String> content;
    private Map<String, String> tweetText;
    private Map<String, String> outcomes;
    
    /**
     * Empty contructor. Actual attributes are set by Jackson API deserializer.
     */
    public CourseModule()
    {
        
    }
    
    /**
     * Sets content description of this course in all given languages. 
     *  Should only be used by Jackson.
     * @param content a map containing the content of this course in all 
     *  given languages as keys
     */
    public void setContent(Map<String, String> content)
    {
        this.content = content;
    }
    
    /**
     * Sets the brief content description to be used when hovering
     *  over this course.
     * @param tweet the brief introduction to this course, in all
     *  given languages as keys
     */
    public void setTweetText(Map<String, String> tweet)
    {
        this.tweetText = tweet;
    }
    
    /**
     * Sets the learning outcomes of this course with all given languages as keys.
     * @param outcomes the map containing the learning outcomes with languages 
     *  as keys
     */
    public void setOutcomes(Map<String, String> outcomes)
    {
        this.outcomes = outcomes;
    }
    
    /**
     * Gets the content description of this course in the given language.
     * @param lang a two-letter short code for the wanted language
     * @return the content of this course in the given language, or in English 
     *  if that doesn't exist, or an associated backup message if neither exists
     */
    public String getContent(String lang)
    {
        if (content == null)
        {
            return NO_CONTENT_MSG;
        }
        String backUpContent = content.getOrDefault("en", NO_CONTENT_MSG);
        return content.getOrDefault(lang, backUpContent);
    }
    
    /**
     * Gets a brief content description of this course in the given language.
     * @param lang a two-letter short code for the wanted language
     * @return a brief content description of this course in the given language, 
     *  or in English if that doesn't exist, or an associated backup message if 
     *  neither exists
     */
    public String getShortContent(String lang)
    {
        if (tweetText == null)
        {
            return NO_CONTENT_MSG;
        }
        String backUpContent = tweetText.getOrDefault("en", NO_CONTENT_MSG);
        return tweetText.getOrDefault(lang, backUpContent);
    }
    
    /**
     * Gets a string depicting the learning outcomes of this module in the given 
     *  language.
     * @param lang a two-letter short code for the wanted 
     * @return the learning outcomes as a String in the given language, or in English 
     *  if one doesn't exist, or an associated backup message if neither exists
     */
    public String getOutcomes(String lang)
    {
        if (outcomes == null)
        {
            return NO_OUTCOMES_MSG;
        }
        String backupOutcome = outcomes.getOrDefault("en", NO_OUTCOMES_MSG);
        return outcomes.getOrDefault(lang, backupOutcome);
    }
}
