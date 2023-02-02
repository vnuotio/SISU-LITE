package fi.tuni.prog3.sisu.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 * A class for modeling the 'rule'/'rules' fields on the Sisu API. 
 *  Can hold another RuleModel and an array of them under itself. 
 *  Instantiated from JSON data using Jackson deserializer.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class RuleModule {
    /**
     * The rule type corresponding to modules in the Sisu API.
     */
    public static final String MODULERULE = "ModuleRule";

    /**
     * The rule type corresponding to courses in the Sisu API.
     */
    public static final String COURSEUNITRULE = "CourseUnitRule";
    
    @JsonProperty
    private RuleModule rule;
    @JsonProperty
    private RuleModule[] rules;
    @JsonProperty
    private String type;
    @JsonProperty
    private String moduleGroupId;
    @JsonProperty
    private String courseUnitGroupId;
    
    /**
     * Empty contructor. Actual attributes are set by Jackson API deserializer.
     */
    public RuleModule()
    {
        
    }
    
    /**
     * Sets a child RuleModule from the 'rule' field if one is present under
     *  this on the Sisu API. Should only be used by Jackson.
     * @param rule the child RuleModule to be assigned under this
     */
    public void setRule(RuleModule rule)
    {
        this.rule = rule;
    }
    
    /**
     * Sets an array of child RuleModules from the 'rules' field if one is 
     *  present under this on the Sisu API. Should only be used by Jackson.
     * @param rules the RuleModule children to be assigned under this
     */
    public void setRules(RuleModule[] rules)
    {
        this.rules = rules;
    }
    
    /**
     * Sets the type of this RuleModule from the Sisu API. Rule types are used 
     *  to identify child {@link StudyModule StudyModules} and child
     *  {@link CourseModule CourseModules}. Should only be used by Jackson.
     * @param type the type of this rule
     */
    public void setType(String type)
    {
        this.type = type;
    }
    
    /**
     * Sets the groupID for a StudyModule if a suitable field is found 
     *  from the Sisu API data. Exclusive with the 
     *  {@link #setCourseUnitGroupId(java.lang.String) setCourseUnitGroupId} method. 
     *  Should only be used by Jackson.
     * @param id the groupID present in the field
     */
    public void setModuleGroupId(String id)
    {
        this.moduleGroupId = id;
    }
    
    /**
     * Sets the groupID for a CourseModule if a suitable field is found 
     *  from the Sisu API data. Exclusive with the 
     *  {@link #setModuleGroupId(java.lang.String) setModuleGroupId} method.
     *  Should only be used by Jackson.
     * @param id 
     */
    public void setCourseUnitGroupId(String id)
    {
        this.courseUnitGroupId = id;
    }
    
    /**
     * Gets the possible child rule under this RuleModule.
     * @return the RuleModule under this, can be null if none 
     *  has been assigned
     */
    public RuleModule getRule()
    {
        return rule;
    }
    
    /**
     * Gets the possible 'rules' children RuleModules under this RuleModule.
     * @return an array of RuleModules under this, can be null if none have 
     *  been assigned
     */
    public RuleModule[] getRules()
    {
        return rules;
    }
    
    /**
     * Gets the type of this rule.
     * @return the type
     */
    public String getType()
    {
        return type;
    }
    
    /**
     * Gets a groupID depending on the type given, can be either 
     *  StudyModule or CourseModule groupIDs.
     * @param type a type listed within this class, what type of
     *  groupID to return
     * @return StudyModule groupID if MODULERULE is given as type, or
     *  CourseModule groupID of COURSEUNITRULE is given as type
     */
    private String getGroupId(String type)
    {
        if (MODULERULE.equals(type))
        {
            return moduleGroupId;
        }
        else if (COURSEUNITRULE.equals(type))
        {
            return courseUnitGroupId;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Gets the groupIDs for either StudyModules or CourseModules 
     *  directly under this rule. Used to map StudyModules or CourseModules 
     *  under a StudyModule and build a tree from this data.
     * @param type a type listed within this class, what type of 
     *  groupID to return
     * @return all direct StudyModule children if MODULERULE is given as type, 
     *  or all direct CourseModule children if COURSEUNITRULE is given as type
     */
    public ArrayList<String> getChildIds(String type)
    {
        ArrayList<String> ids = new ArrayList<>();
        if (getRule() != null)
        {
            ids.addAll(getRule().getChildIds(type));
        }
        
        if (getRules() != null)
        {
            for (RuleModule rm : getRules())
            {
                if (type.equals(rm.getType()))
                {
                    ids.add(rm.getGroupId(type));
                }
                else if (rm.getRules() != null)
                {
                    ids.addAll(rm.getChildIds(type));
                }
            }
        }
        return ids;
    }
}
