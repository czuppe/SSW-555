package project03;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nraj39
 */
public class Sprint1Test {

    GEDCOMData GEDCOMDataObj;

    public Sprint1Test() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        //GEDCOMDataObj = App.parseGEDFile("/Users/Bella/eclipse-workspace/project03/src/project03/BellaManoim.ged");
        //GEDCOMDataObj = App.parseGEDFile("C\\:\\\\Users\\Charles\\\\Documents\\\\NetBeansProjects\\\\project03\\\\src\\\\project03\\\\test.ged");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testBirthDatesMustExists() throws Exception {
        GEDCOMDataObj.getIndividuals().forEach((k, entity) -> {
            assertNotNull(entity.BirthDate);
        });
    }

    @Test
    public void testBirthDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.getIndividuals().forEach((k, entity) -> {
            assertTrue(entity.BirthDate != null && entity.BirthDate.before(todaysDate));
        });
    }

    @Test
    public void testDeathDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.getIndividuals().forEach((k, entity) -> {
            if (entity.DeathDate != null) {
                assertTrue(entity.DeathDate.before(todaysDate));
            }
        });
    }

    @Test
    public void testMarriageDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.getFamilies().forEach((k, entity) -> {
            if (entity.MarriageDate != null) {
                assertTrue(entity.MarriageDate.before(todaysDate));
            }
        });
    }

    @Test
    public void testDivorceDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.getFamilies().forEach((k, entity) -> {
            if (entity.DivorceDate != null) {
                assertTrue(entity.DivorceDate.before(todaysDate));
            }
        });
    }

      @Test //(Bella US03) test valid birth date is before valid death date
    public void testValidDatesBeforeDeathDate() throws Exception {
    	PersonEntity entity = new PersonEntity();
    	
    	entity.BirthDate = new Date(2010,3,30);
    	entity.DeathDate = new Date(2013,2,2);
    	List<ValidationResult> results = new ArrayList<ValidationResult>();
    	
    	PersonEntityValidator.birthBeforeDeathDateCheck(entity, results);
    	assertTrue(results.isEmpty());
    }
    
    
    @Test //(Bella US03) Check if invalid birth date is before valid death date
    public void testInvalidDatesBeforeDeathDate() throws Exception {
    	PersonEntity entity = new PersonEntity();
    	
    	entity.BirthDate = new Date(2014,3,30);
    	entity.DeathDate = new Date(2010,2,2);
    	List<ValidationResult> results = new ArrayList<ValidationResult>();
    	
    	PersonEntityValidator.birthBeforeDeathDateCheck(entity, results);
    	assertTrue(results.get(0).Message.contentEquals("Birth date must be before death date."));
    }
    
    @Test //(Bella US03) Check if birth date is before NULL death date
    public void testDatesBeforeNullDeathDate() throws Exception {
    	PersonEntity entity = new PersonEntity();
    	
    	entity.BirthDate = new Date(2014,3,30);
    	entity.DeathDate = null;
    	List<ValidationResult> results = new ArrayList<ValidationResult>();
    	
    	PersonEntityValidator.birthBeforeDeathDateCheck(entity, results);
    	assertTrue(results.isEmpty());
    }
    
    @Test //(Bella US03) Check if Null birth date is before death date
    public void testNullBirthDatesBeforeDeathDate() throws Exception {
    	PersonEntity entity = new PersonEntity();
    	
    	entity.BirthDate = null;
    	entity.DeathDate = new Date(2014,5,1);
    	List<ValidationResult> results = new ArrayList<ValidationResult>();
    	
    	PersonEntityValidator.birthBeforeDeathDateCheck(entity, results);
    	assertTrue(results.isEmpty());
    	
    }
    
    @Test //(Bella US03) Check if Null birth date is before NULL death date
    public void testnullBirthDatesBeforeNullDeathDate() throws Exception {
    	PersonEntity entity = new PersonEntity();
    	
    	entity.BirthDate = null;
    	entity.DeathDate = null;
    	List<ValidationResult> results = new ArrayList<ValidationResult>();
    	
    	PersonEntityValidator.birthBeforeDeathDateCheck(entity, results);
    	assertTrue(results.isEmpty());
    }
    
    @Test //(Bella US05) 
    public void testValidMarriageBeforeDeathDate() throws Exception {
    	FamilyEntity entity = new FamilyEntity();
    	entity.Husband = new PersonEntity();
    	
    	entity.Husband.DeathDate = new Date(2043,3,30);
    	entity.MarriageDate = new Date(2000,2,4);
    	List<ValidationResult> results = new ArrayList<ValidationResult>();
    	
    	FamilyEntityValidator.marriageBeforeDeathCheck(entity, results);
       	assertTrue(results.isEmpty());
    }
}
