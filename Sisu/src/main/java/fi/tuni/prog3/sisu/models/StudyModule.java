package fi.tuni.prog3.sisu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fi.tuni.prog3.sisu.utility.HttpRequestHandler;
import fi.tuni.prog3.sisu.api.SisuApiHandler;
import java.util.ArrayList;

/**
 * A class for StudyModules and DegreeProgrammes from the Sisu API. 
 *  Can hold other StudyModules or {@link CourseModule CourseModules}(courses) 
 *  under itself. Instantiated from JSON data using Jackson deserializer.
 */
public class StudyModule extends SisuModule{
    private final String CHILDSTUDYMODULES_NULL_MSG = "ChildStudyModules is null! Module tree possibly not generated using buildModuleTree()?";
    private final String CHILDCOURSES_NULL_MSG = "ChildCourses is null! Module tree possibly not generated using buildModuleTree()?";
    
    @JsonProperty
    private RuleModule rule;
    @JsonIgnore
    private ArrayList<StudyModule> childStudyModules;
    @JsonIgnore
    private ArrayList<CourseModule> childCourses;
    
    /**
     * Empty contructor. Actual attributes are set by Jackson API deserializer.
     */
    public StudyModule()
    {

    }
    
    /**
     * Sets the 'rule' field from Sisu API on this module. This is used to 
     *  map the StudyModules and CourseModules(courses) under this StudyModule. 
     *  Should only be used by Jackson.
     * @param rule the rule field from Sisu API as a RuleModule instance
     */
    public void setRule(RuleModule rule)
    {
        this.rule = rule;
    }

    /**
     * Gets the rule associated with this StudyModule. Used when building a
     *  module tree starting from this StudyModule.
     * @return the rule under this StudyModule
     */
    private RuleModule getRule()
    {
        return rule;
    }
    
    /**
     * Gets the StudyModules directly under this module.
     * @return an ArrayList containg all StudyModules under this one if the 
     *  list is not null
     * @throws NullPointerException if the list containing child StudyModules 
     *  is null
     */
    public ArrayList<StudyModule> getChildStudyModules() throws NullPointerException
    {
        if (childStudyModules == null)
        {
            throw new NullPointerException(CHILDSTUDYMODULES_NULL_MSG);
        }
        return childStudyModules;
    }
    
    /**
     * Gets the CourseModules(courses) directly under this StudyModule.
     * @return an ArrayList containing all CourseModules under this module if 
     *  the list is not null
     * @throws NullPointerException if the list containing child CourseModules 
     *  is null
     */
    public ArrayList<CourseModule> getChildCourses() throws NullPointerException
    {
        if (childCourses == null)
        {
            throw new NullPointerException(CHILDCOURSES_NULL_MSG);
        }
        return childCourses;
    }
    
    /**
     * Builds a SisuModule tree starting from this StudyModule. Populates the 
     *  child StudyModule and child CourseModules lists for this module and 
     *  its children and their children, until all StuduModules and CourseModules 
     *  starting from this have been generated and mapped. Should be called before 
     *  trying to use {@link #getChildStudyModules() getChildStudyModules} 
     *  or {@link #getChildCourses() getChildCourses} methods.
     */
    public void buildModuleTree()
    {
        childStudyModules = new ArrayList<>();
        
        HttpRequestHandler httphandler = new HttpRequestHandler();
        
        ArrayList<String> childModuleIds = getRule().getChildIds(RuleModule.MODULERULE);
        
        for (String mId : childModuleIds)
        {
            StudyModule sm = SisuApiHandler.getStudyModule(mId, httphandler);
            childStudyModules.add(sm);
            sm.buildModuleTree();
        }
        buildCourseList(httphandler);
    }

    /**
     * Populates the child CourseModules list directly under this StudyModule.
     * @param httphandler the {@link HttpRequestHandler HttpRequestHandler} 
     *  used to get the course's data by a Sisu API call
     */
    private void buildCourseList(HttpRequestHandler httphandler)
    {
        childCourses = new ArrayList<>();
        
        ArrayList<String> childCourseIds = getRule().getChildIds(RuleModule.COURSEUNITRULE);
        for (String cId : childCourseIds)
        {
            CourseModule cm = SisuApiHandler.getCourseModule(cId, httphandler);
            childCourses.add(cm);
        }
    }
    
    /**
     * Clears the child StudyModules and child CourseModules lists, 
     *  destroying the tree starting from this StudyModule. Used to free up 
     *  memory or otherwise clear unnecessary trees.
     */
    public void clearModuleTree()
    {
        try
        {
            for (StudyModule sm : getChildStudyModules())
            {
                sm.clearModuleTree();
            }
            childCourses.clear();
            childStudyModules.clear();
        }
        catch (NullPointerException ex)
        {
            // Returns
        }
        
    }
}
