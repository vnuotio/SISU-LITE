package fi.tuni.prog3.sisu.utility;

public enum Urls{
    DEGREES_URL("https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000"),
    MODULES_PREFIX("https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId="),
    COURSES_PREFIX("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId="),
    UNI_ID("&universityId=tuni-university-root-id"),
    DEGREEJSONPATH("src/main/java/fi/tuni/prog3/sisu/data/DegreesInfo.json");

    private final String url;

     Urls(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
