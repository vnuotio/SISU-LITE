package fi.tuni.prog3.sisu.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class for holding degree programme groupIDs from Sisu API.
 *  Used to fetch more detailed data as {@link StudyModule StudyModules}.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DegreeId{
    private String groupId;
    
    /**
     * Empty contructor. Actual attributes are set by Jackson API deserializer.
     */
    public DegreeId()
    {
        
    }
    
    /**
     * Sets the groupID of this module. Used when getting more 
     *  detailed StudyModules.
     * @param id the groupID for Sisu API calls
     */
    public void setGroupId(String id)
    {
        this.groupId = id;
    }
    
    /**
     * Gets the groupID of this degree used for Sisu API calls.
     * @return the groupID of this degree
     */
    public String getGroupId()
    {
        return groupId;
    }
}
