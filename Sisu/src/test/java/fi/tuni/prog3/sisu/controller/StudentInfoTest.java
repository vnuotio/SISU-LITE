package fi.tuni.prog3.sisu.controller;


import fi.tuni.prog3.sisu.api.SisuApiHandler;
import fi.tuni.prog3.sisu.models.StudyModule;
import fi.tuni.prog3.sisu.utility.Urls;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class StudentInfoTest{

    SisuApiHandler sisuApiHandler;

    @Test(expected = IllegalArgumentException.class)
    public void getListOfAllDegreeForWrongUrl() throws IOException {
        ArrayList<StudyModule>  listofCourses = SisuApiHandler.getAllDegrees("RandomUrl");
    }
    @Test
    public void getListOfAllCoursesHappyCase() throws IOException {
        ArrayList<StudyModule>  listofCourses = SisuApiHandler.getAllDegrees(Urls.DEGREES_URL.getUrl());
        Assert.assertNotNull(listofCourses);
   }

    @Test
    public void getStudyModuleForUnknownGroupID() throws IOException {
        StudyModule listofCourses = SisuApiHandler.getStudyModule("Urls.DEGREES_URL.getUrl()",null);
        Assert.assertNull(listofCourses);
    }

}