package fi.tuni.prog3.sisu.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * An abstract super class for different modules from the Sisu API.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class SisuModule {
    private static final String NO_NAME_MSG = "No name given";
    private static final String MIN_CREDITS_KEY = "min";  
    
    @JsonProperty("targetCredits")
    @JsonAlias("credits")
    private Map<String, Integer> targetCredits;
    
    private Map<String, String> name;
    private String groupId;
    
    /**
     * Empty contructor. Actual attributes are set by Jackson API deserializer.
     */
    public SisuModule()
    {
        
    }
    
    /**
     * Sets the minimum and maximum credits for this module.
     *  Should only be used by Jackson.
     * @param credits a map containing the minimum and maximum credits with 
     *  'min' and 'max' as keys
     */
    public void setTargetCredits(Map<String, Integer> credits)
    {
        this.targetCredits = credits;  
    }
    
    /**
     * Sets the name of this module in all given languages.
     *  Should only be used by Jackson.
     * @param name a map containing the name of this module in all given 
     *  languages as keys
     */
    @JsonProperty
    public void setName(Map<String, String> name)
    {
        this.name = name;
    }

    /**
     * Sets the groupID of this module. The groupID can be used to 
     *  fetch the data for this module using the Sisu API.
     * @param id the groupID for Sisu API calls
     */
    public void setGroupId(String id)
    {
        this.groupId = id;
    }
    
    /**
     * Gets the minimum required credits for this module.
     * @return the minimum required credits if amount exists, otherwise 0
     */
    public Integer getMinCredits()
    {
        if (targetCredits == null)
        {
            return 0;
        }
        return targetCredits.getOrDefault(MIN_CREDITS_KEY, 0);
    }
    
    /**
     * Gets a map containing all of the names for this module with 
     *  languages as keys. {@link #getNameByLang(java.lang.String) getNameByLang} 
     *  is preferred as this is used by Jackson serializer.
     * @return the map containing all names with languages as keys
     */
    public Map<String, String> getName()
    {
        return name;
    }
    
    /**
     * Gets the name of this module in given language.
     * @param lang a two-letter short code for the wanted language
     * @return the name in the given language, or in English if that doesn't exist, 
     *  or an associated backup  message if neither exists
     */
    @JsonIgnore
    public String getNameByLang(String lang)
    {
        if (name == null)
        {
            return NO_NAME_MSG;
        }
        String backUpName = name.getOrDefault("en", NO_NAME_MSG);
        return name.getOrDefault(lang, backUpName);
    }
    
    /**
     * Gets the groupID of this module used for Sisu API calls.
     * @return the groupID of this module
     */
    public String getGroupId()
    {
        return groupId;
    }
}
