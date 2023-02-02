package fi.tuni.prog3.sisu.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.tuni.prog3.sisu.models.CourseModule;
import fi.tuni.prog3.sisu.models.DegreeId;
import fi.tuni.prog3.sisu.models.StudyModule;
import fi.tuni.prog3.sisu.utility.HttpRequestHandler;
import fi.tuni.prog3.sisu.utility.Urls;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * A class for handling Sisu API requests. Handles getting 
 *  {@link StudyModule StudyModules} and {@link CourseModule CourseModules(courses)}
 *  with {@link HttpRequestHandler HttpRequestHandler}. Should not be
 *  instantiated but rather used only by calling methods.
 */
public class SisuApiHandler {
    public static final String getDegreeJsonPath()
    {
        return Urls.DEGREEJSONPATH.getUrl();
    }
    
    /**
     * Gets all found degree programmes from the Sisu API under Tampere University.
     * @param url Url from where the function gets the degeree info.
     * @return an ArrayList containing all found degrees as StudyModules
     */
    public static final ArrayList<StudyModule> getAllDegrees(String url)
    {
        DegreeId[] degreeIds;
        ArrayList<StudyModule> degrees = new ArrayList<>();
        try
        {
            HttpRequestHandler handler = new HttpRequestHandler();
            String content = handler.getHttpContent(url);
            
            ObjectMapper objmapper = new ObjectMapper();
            JsonNode root = objmapper.readTree(content);
            
            degreeIds = objmapper.treeToValue(root.at("/searchResults"),
                                        DegreeId[].class);
            
            for (DegreeId dId : degreeIds)
            {
                String id = dId.getGroupId();
                StudyModule degree = getStudyModule(id, handler);
                degrees.add(degree);
            }
        }
        catch (IOException ex)
        {
            System.err.println("Bad data!\n" + ex.getMessage());
            // TODO: Display error message on UI and don't proceed
        }
        return degrees;
    }
    
    /**
     * Gets all degree programmes from a JSON file at specified path. If no valid 
     *  path or file can be found, returns all degrees from the Sisu API using
     * @return
     */
    public static final ArrayList<StudyModule> getAllDegreesFromJson()
    {
        String path = getDegreeJsonPath();
        ArrayList<StudyModule> degrees = new ArrayList<>();
        ObjectMapper objmapper = new ObjectMapper();
        try
        {
            degrees = objmapper.readValue(Paths.get(path).toFile(),
                    new TypeReference<ArrayList<StudyModule>>(){});
        }
        catch (IOException ex)
        {
            System.err.println("Invalid path for getAllDegreesFromJson, fetching from getAllDegrees...");
            degrees = getAllDegrees(Urls.DEGREES_URL.getUrl());
        }
        finally
        {
            return degrees;
        }
    }
    
    /**
     * Gets a StudyModule instance built from JSON data from the Sisu API 
     *  using Jackson API deserializer.
     * @param groupId the Sisu groupID corresponding to this StudyModule
     * @param httphandler the handler used to send HTTP requests to the Sisu API
     * @return a StudyModule generated from data of given ID, or null if an 
     *  error occurs
     */
    public static final StudyModule getStudyModule(String groupId, HttpRequestHandler httphandler)
    {
        if (httphandler == null)
        {
            httphandler = new HttpRequestHandler();
        }
        StudyModule module = null;
        
        try
        {
            String url = Urls.MODULES_PREFIX.getUrl() + groupId +Urls.UNI_ID.getUrl();
            String content = httphandler.getHttpContent(url);
            
            ObjectMapper objmapper = new ObjectMapper();
            JsonNode root = objmapper.readTree(content);
            
            module = objmapper.convertValue(root.at("/0"), StudyModule.class);
            
        }
        catch (IOException ex)
        {
            System.err.println("Bad data!\n" + ex.getMessage());
            // TODO: Display error message on UI and don't proceed
        } 
        return module;
    }
    
    /**
     * Gets a CourseModule instance built from JSON data from the Sisu API 
     *  using Jackson API deserializer.
     * @param groupId the Sisu groupID corresponding to this course
     * @param httphandler the handler used to send HTTP requests to the Sisu API
     * @return a CourseModule generated from data of given ID, or null if an 
     *  error occurs
     */
    public static final CourseModule getCourseModule(String groupId, HttpRequestHandler httphandler)
    {
        if (httphandler == null)
        {
            httphandler = new HttpRequestHandler();
        }
        CourseModule course = null;
        
        try
        {
            String url = Urls.COURSES_PREFIX.getUrl() + groupId + Urls.UNI_ID.getUrl();
            
            String content = httphandler.getHttpContent(url);
            
            ObjectMapper objmapper = new ObjectMapper();
            JsonNode root = objmapper.readTree(content);
            
            course = objmapper.convertValue(root.at("/0"), CourseModule.class);
            
        }
        catch (IOException ex)
        {
            System.err.println("Bad data!\n" + ex.getMessage());
            // TODO: Display error message on UI and don't proceed
        }
        return course;
    }
    
}
