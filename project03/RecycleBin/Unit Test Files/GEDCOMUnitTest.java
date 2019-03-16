package project03;

import java.io.*;
import java.net.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Cinchoo, czuppe, Bella458, cjarret1
 */
public class GEDCOMUnitTest {

    GEDCOMData GEDCOMDataObj_Raj;
    GEDCOMData GEDCOMDataObj;

    public GEDCOMUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        GEDCOMDataObj_Raj = App.parseGEDFromURI(getClass().getResource("./Input Files/Raj.ged").toURI());
        //GEDCOMDataObj = App.parseGEDFile("/Users/Bella/eclipse-workspace/project03/src/project03/BellaManoim.ged");
        //GEDCOMDataObj = App.parseGEDFile("C\\:\\\\Users\\Charles\\\\Documents\\\\NetBeansProjects\\\\project03\\\\src\\\\project03\\\\test.ged");
        GEDCOMDataObj = App.parseGEDFromURI(getClass().getResource("./Input Files/test.ged").toURI());
    }

    @After
    public void tearDown() {
    }

    //BEGIN: *** Raj Test Cases ***
    //PLEASE DO NOT EDIT THIS SECTION
    
    @Test // (Raj US01) Dates before current date
    public void testDatesBeforeCurrentDate() throws Exception {
        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.datesBeforeCurrentDateCheck(GEDCOMDataObj_Raj, results);
        assertTrue(results.isEmpty());
    }

    @Test // (Raj US02) Birth before marriage
    public void testBirthBeforeMarriage() throws Exception {
        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.birthBeforeMarriageCheck(GEDCOMDataObj_Raj, results);
        assertTrue(results.isEmpty());
    }

    @Test //(Raj US15) There should be fewer than 15 siblings in a family
    public void testFewerThan15Siblings() throws Exception {
        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.fewerThan15SiblingsCheck(GEDCOMDataObj_Raj, results);
        assertTrue(results.isEmpty());
    }

    @Test //(Raj US16) All male members of a family should have the same last name
    public void testAllMaleMembersShouldHaveSameLastName() throws Exception {
        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.maleLastNameCheck(GEDCOMDataObj_Raj, results);
        assertTrue(results.isEmpty());
    }
    
    //END: *** Raj Test Cases ***

    public void testParentsNotTooOldCaseFather80YearsOlder() throws Exception {
        GEDCOMData gcd = new GEDCOMData();

        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1980"};

        String[] parentElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1900"};

        FamilyEntity family = FamilyEntity.create(familyElements);
        PersonEntity child = PersonEntity.create(childElements);

        gcd.addIndividual(child);
        PersonEntity parent = PersonEntity.create(parentElements);

        family.Husband = parent;
        family.ChildrenId.add(child.getId());
        gcd.addFamily(family);

        List<ValidationResult> results = new ArrayList<ValidationResult>();
        FamilyEntityValidator.parentsNotTooOldCheck(family, results);

        assertTrue(results.isEmpty());

    }

    @Test //(Charles US12) Parents not too old        
    public void testParentsNotTooOldCaseFather30YearsOlder() throws Exception {
        GEDCOMData gcd = new GEDCOMData();

        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1930"};

        String[] parentElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1900"};

        FamilyEntity family = FamilyEntity.create(familyElements);
        PersonEntity child = PersonEntity.create(childElements);

        gcd.addIndividual(child);
        PersonEntity parent = PersonEntity.create(parentElements);

        family.Husband = parent;
        family.ChildrenId.add(child.getId());
        gcd.addFamily(family);

        List<ValidationResult> results = new ArrayList<ValidationResult>();
        FamilyEntityValidator.parentsNotTooOldCheck(family, results);

        assertTrue(results.isEmpty());

    }

    @Test //(Charles US12) Parents not too old. Edge case: Mother is exactly 20 years older than child
    public void testParentsNotTooOldCaseMother20YearsOlder() throws Exception {
        GEDCOMData gcd = new GEDCOMData();

        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1920"};

        String[] parentElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1900"};

        FamilyEntity family = FamilyEntity.create(familyElements);
        PersonEntity child = PersonEntity.create(childElements);

        gcd.addIndividual(child);
        PersonEntity parent = PersonEntity.create(parentElements);

        family.Wife = parent;
        family.ChildrenId.add(child.getId());
        gcd.addFamily(family);

        List<ValidationResult> results = new ArrayList<ValidationResult>();
        FamilyEntityValidator.parentsNotTooOldCheck(family, results);

        assertTrue(results.isEmpty());

    }

    @Test //(Charles US12) Parents not too old. Edge case: Mother is exactly 60 years older than child
    public void testParentsNotTooOldCaseMother60YearsOlder() throws Exception {
        GEDCOMData gcd = new GEDCOMData();

        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1960"};

        String[] parentElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1900"};

        FamilyEntity family = FamilyEntity.create(familyElements);
        PersonEntity child = PersonEntity.create(childElements);

        gcd.addIndividual(child);
        PersonEntity parent = PersonEntity.create(parentElements);

        family.Wife = parent;
        family.ChildrenId.add(child.getId());
        gcd.addFamily(family);

        List<ValidationResult> results = new ArrayList<ValidationResult>();
        FamilyEntityValidator.parentsNotTooOldCheck(family, results);

        assertTrue(results.isEmpty());

    }

    @Test //(Charles US12) Parents not too old. Edge case: Mother is exactly 61 years older than child
    public void testParentsNotTooOldCaseMother61YearsOlder() throws Exception {
        GEDCOMData gcd = new GEDCOMData();

        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1961"};

        String[] parentElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1900"};

        FamilyEntity family = FamilyEntity.create(familyElements);
        PersonEntity child = PersonEntity.create(childElements);

        gcd.addIndividual(child);
        PersonEntity parent = PersonEntity.create(parentElements);

        family.Wife = parent;
        family.ChildrenId.add(child.getId());
        gcd.addFamily(family);

        List<ValidationResult> results = new ArrayList<ValidationResult>();
        FamilyEntityValidator.parentsNotTooOldCheck(family, results);

        assertFalse(results.isEmpty());

    }

    @Test //(Charles US12) Parents not too old. Edge case: Father is exactly 81 years older than child
    public void testParentsNotTooOldCaseFather81YearsOlder() throws Exception {
        GEDCOMData gcd = new GEDCOMData();

        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1981"};

        String[] parentElements = {"0 @I2@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1900"};

        FamilyEntity family = FamilyEntity.create(familyElements);
        PersonEntity child = PersonEntity.create(childElements);

        gcd.addIndividual(child);
        PersonEntity parent = PersonEntity.create(parentElements);

        family.Husband = parent;
        family.ChildrenId.add(child.getId());
        gcd.addFamily(family);

        List<ValidationResult> results = new ArrayList<ValidationResult>();
        FamilyEntityValidator.parentsNotTooOldCheck(family, results);

        assertFalse(results.isEmpty());
    }

    @Test //(Charles US12) Parents not too old.
    public void testParentsNotTooOldCaseInGEDFile() throws Exception {
        
    	URL testGed = getClass().getResource("C:\\Users\\Craig\\eclipse-workspace\\project03\\src\\project03\\test.ged");
        URL testGedOut = getClass().getResource("C:\\Users\\Craig\\eclipse-workspace\\project03\\src\\project03\\test.ged.out");
        
        String[] args = {testGed.getPath()};
        App.main(args);
        File gedOutputFile = new File(testGedOut.getPath());
        Scanner gedOutput = new Scanner(gedOutputFile);        
        boolean foundExpected = false;

        while(gedOutput.hasNext()) {
            String nextLine = gedOutput.nextLine();            
            if(nextLine.contains("Mother should be less than 60 years older than her children and father should be less than 80 years older than his children.")){
                foundExpected = true;
                break;
            }            
        }
        assertTrue(foundExpected);
        gedOutput.close();        
    } 

    @Test //(Bella US03) test valid birth date is before valid death date
    public void testValidDatesBeforeDeathDate() throws Exception {
        PersonEntity entity = new PersonEntity();

        entity.BirthDate = new Date(2010, 3, 30);
        entity.DeathDate = new Date(2013, 2, 2);
        List<ValidationResult> results = new ArrayList<ValidationResult>();

        PersonEntityValidator.birthBeforeDeathDateCheck(entity, results);
        assertTrue(results.isEmpty());
    }

    @Test //(Bella US03) Check if invalid birth date is before valid death date
    public void testInvalidDatesBeforeDeathDate() throws Exception {
        PersonEntity entity = new PersonEntity();

        entity.BirthDate = new Date(2014, 3, 30);
        entity.DeathDate = new Date(2010, 2, 2);
        List<ValidationResult> results = new ArrayList<ValidationResult>();

        PersonEntityValidator.birthBeforeDeathDateCheck(entity, results);
        assertFalse(results.get(0).Message.contentEquals("Birth date must be before death date."));
    }

    @Test //(Bella US03) Check if birth date is before NULL death date
    public void testDatesBeforeNullDeathDate() throws Exception {
        PersonEntity entity = new PersonEntity();

        entity.BirthDate = new Date(2014, 3, 30);
        entity.DeathDate = null;
        List<ValidationResult> results = new ArrayList<ValidationResult>();

        PersonEntityValidator.birthBeforeDeathDateCheck(entity, results);
        assertTrue(results.isEmpty());
    }

    @Test //(Bella US03) Check if Null birth date is before death date
    public void testNullBirthDatesBeforeDeathDate() throws Exception {
        PersonEntity entity = new PersonEntity();

        entity.BirthDate = null;
        entity.DeathDate = new Date(2014, 5, 1);
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

        entity.Husband.DeathDate = new Date(2043, 3, 30);
        entity.MarriageDate = new Date(2000, 2, 4);
        List<ValidationResult> results = new ArrayList<ValidationResult>();

        FamilyEntityValidator.marriageBeforeDeathCheck(entity, results);
        assertTrue(results.isEmpty());
    }

    // /*
    @Test //Bella US07: Less then 150 years old (birth and death date)
    public void testlessthan150YearsOldCheck() throws Exception {
        PersonEntity entity = new PersonEntity();
        //date constructor requires me to subtract year from 1900
        entity.BirthDate = new Date(-50, 3, 30);
        entity.DeathDate = new Date(119, 3, 30);
        List<ValidationResult> results = new ArrayList<ValidationResult>();

        PersonEntityValidator.lessthan150YearsOldCheck(entity, results);
        assertTrue(results.get(0).Message.contains("Age"));
    }
    // */

    @Test //Bella US07: Test Less then 150 years old (no death date)
    public void testlessthan150YearsOldNoDeathDateCheck() throws Exception {
        PersonEntity entity = new PersonEntity();

        //date constructor requires me to subtract year from 1900
        entity.BirthDate = new Date(-50, 5, 4);
        entity.DeathDate = null;
        List<ValidationResult> results = new ArrayList<ValidationResult>();

        PersonEntityValidator.lessthan150YearsOldCheck(entity, results);
        assertTrue(results.get(0).Message.contains("Age"));
    }

    @Test //Bella US07: Test Less then 150 years old (valid)
    public void testvalidlessthan150YearsOldCheck() throws Exception {
        PersonEntity entity = new PersonEntity();

        //date constructor requires me to subtract year from 1900
        entity.BirthDate = new Date(50, 1, 6);
        entity.DeathDate = new Date(100, 2, 3);
        List<ValidationResult> results = new ArrayList<ValidationResult>();

        PersonEntityValidator.lessthan150YearsOldCheck(entity, results);
        assertTrue(results.isEmpty());
    }

    @Test //Bella US04:	Marriage before divorce (Valid)
    public void testvalidmarriageBeforeDivorceCheck() throws Exception {

        FamilyEntity entity = new FamilyEntity();
        entity.Husband = new PersonEntity();
        entity.Wife = new PersonEntity();

        entity.Divorce = new FactEntity();
        entity.Marriage = new FactEntity();

        //date constructor requires me to subtract year from 1900
        entity.Marriage.Date = entity.MarriageDate = new Date(10, 3, 30);
        entity.Divorce.Date = entity.DivorceDate = new Date(20, 2, 4);
        List<ValidationResult> results = new ArrayList<ValidationResult>();

        FamilyEntityValidator.marriageBeforeDivorceCheck(entity, results);
        assertTrue(results.isEmpty());
        //System.out.println(results); //should print message, just for additional testing
    }

    @Test //Bella US04:	Marriage before divorce (invalid)
    public void testinvalidmarriageBeforeDivorceCheck() throws Exception {

        FamilyEntity entity = new FamilyEntity();
        entity.Husband = new PersonEntity();
        entity.Wife = new PersonEntity();

        //date constructor requires me to subtract year from 1900
        entity.Marriage = new FactEntity();
        entity.Marriage.Date = entity.MarriageDate = new Date(99, 3, 30);

        entity.Divorce = new FactEntity();
        entity.Divorce.Date = entity.DivorceDate = new Date(97, 2, 4);

        List<ValidationResult> results = new ArrayList<ValidationResult>();
        FamilyEntityValidator.marriageBeforeDivorceCheck(entity, results);

        assertTrue(results.get(0).Message.contains("Marriage"));
        //System.out.println(results); //should print empty list, just for additional testing
    }

    @Test //(Craig US22) Unique ID
    public void testIndividualsUnqiueID() throws Exception {
        GEDCOMDataObj.getIndividualsDuplicates().forEach((k, v) -> {
            assertFalse(GEDCOMDataObj.getIndividuals().containsKey(k));
        });

    }

    @Test //(Craig US22) Unique ID
    public void testFamiliesUniqueID() throws Exception {

        GEDCOMDataObj.getFamiliesDuplicates().forEach((k, v) -> {
            assertFalse(GEDCOMDataObj.getFamilies().containsKey(k));
        });

    }

    @Test //(Craig US23) Unique name and birth date
    public void testIndividualNamesUnique() throws Exception {

        GEDCOMDataObj.getIndividuals().forEach((k, v) -> {
            assertSame(v, v);
        });

    }

    @Test //(Craig US23) Unique name and birth date
    public void testCount() throws Exception {

        GEDCOMDataObj.getIndividuals().forEach((k, v) -> {
            assertFalse(GEDCOMDataObj.getIndividuals().containsValue(v.FullName + v.BirthDate.toString()));

        });

    }

}