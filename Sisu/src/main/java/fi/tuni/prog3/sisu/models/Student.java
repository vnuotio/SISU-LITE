package fi.tuni.prog3.sisu.models;

import java.util.HashSet;

/**
 * A class for representing the information about a student, including their name, 
 *  degree programme and completed courses.
 */
public class Student {
    private String studentName;
    private String studentNumber;
    private String graduationDate;
    private HashSet<String> courses = new HashSet<>();
    private String degreeGroupId;

    /**
     * Empty constructor. Needed for Jackson API deserializer.
     */
    public Student()
    {
        
    }

    /**
     * The constructor used when manually creating files for new students for 
     *  the first time.
     * @param studentName name of this student
     * @param studentNumber unique ID for this student
     * @param graduationDate the expected graduation data the student has picked
     * @param degreeGroupId the ID of the degree programme the student 
     *  has picked
     */
    public Student(String studentName, String studentNumber, String graduationDate,
           String degreeGroupId) {
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.graduationDate = graduationDate;
        this.degreeGroupId = degreeGroupId;
    }

    /**
     * Gets the ID of this students degree
     * @return the groupId of the degree
     */
    public String getDegreeGroupId()
    {
        return degreeGroupId;
    }
    
    /**
     * Sets the ID for this students degree
     * @param degreeGroupId the groupId of the degree
     */
    public void setDegreeGroupId(String degreeGroupId)
    {
        this.degreeGroupId = degreeGroupId;
    }
    
    /**
     * Gets the name of this student
     * @return the name
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Sets the name for this student
     * @param studentName the name
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * Gets the student number of this student. Used mainly for loading/saving 
     *  data to JSON files.
     * @return the student number
     */
    public String getStudentNumber() {
        return studentNumber;
    }

    /**
     * Sets the unique student number of this student. Used mainly for loading/saving 
     *  data to JSON files.
     * @param studentNumber the student number
     */
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    /**
     * Gets the expected graduation date this student has picked.
     * @return the date, as String
     */
    public String getGraduationDate() {
        return graduationDate;
    }

    /**
     * Sets the expected graduation date this student has picked as String.
     * @param graduationDate the date, as String
     */
    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    /**
     * Gets the {@link CourseModule courses} this student has already completed.
     * @return a HashSet of completed courses' ID.
     */
    public HashSet<String> getCourses() {
        return courses;
    }

    /**
     * Sets the completed courses of this student. Used mainly by Jackson 
     *  deserializer.
     * @param courses a HashSet of completed courses' ID.
     */
    public void setCourses(HashSet<String> courses) {
        this.courses = courses;
    }
    
    /**
     * Adds a completed course's ID to the list of completed courses. Will not 
     *  yet save to disk.
     * @param id ID of the completed course
     */
    public void addCourseCompletion(String id)
    {
        courses.add(id);
    }
}
