package project03;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Cinchoo, czuppe, Bella458, cjarret1
 */
public class GEDCOMUnitTest {

    //GEDCOMData GEDCOMDataObj_Raj; **DELETE THIS UNNECCESARY**
    GEDCOMData GEDCOMDataObj;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        //**METHOD NOT VALID** GEDCOMDataObj_Raj = App.parseGEDFromURI(getClass().getResource("./Input Files/Raj.ged").toURI());
        GEDCOMDataObj = App.parseGEDFromFile("/Users/Bella/eclipse-workspace/project03/src/project03/BellaManoim.ged");        
        //**METHOD NOT VALID** GEDCOMDataObj = App.parseGEDFromURI(getClass().getResource("./Input Files/test.ged").toURI());
    }

    @After
    public void tearDown() {
    }

    //BEGIN: *** Raj Test Cases ***
    //PLEASE DO NOT EDIT THIS SECTION
    
    @Test // (Raj US01) Dates before current date
    public void testDatesBeforeCurrentDate() throws Exception {
        assertTrue(null != GEDCOMDataObj);

        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.datesBeforeCurrentDateCheck(GEDCOMDataObj, results);
        assertTrue(results.isEmpty());
    }

    @Test // (Raj US02) Birth before marriage
    public void testBirthBeforeMarriage() throws Exception {
        assertTrue(null != GEDCOMDataObj);

        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.birthBeforeMarriageCheck(GEDCOMDataObj, results);
        assertTrue(results.isEmpty());
    }

    @Test //(Raj US15) There should be fewer than 15 siblings in a family
    public void testFewerThan15Siblings() throws Exception {
        assertTrue(null != GEDCOMDataObj);

        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.fewerThan15SiblingsCheck(GEDCOMDataObj, results);
        assertTrue(results.isEmpty());
    }

    @Test //(Raj US16) All male members of a family should have the same last name
    public void testAllMaleMembersShouldHaveSameLastName() throws Exception {
        assertTrue(null != GEDCOMDataObj);

        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.maleLastNameCheck(GEDCOMDataObj, results);
        assertFalse(results.isEmpty());
    }

    @Test //(Raj US17) No marriages to children
    public void testNoMarriagesToChildren() throws Exception {
        assertTrue(null != GEDCOMDataObj);

        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.noMarriagesToChildrenCheck(GEDCOMDataObj, results);
        assertTrue(results.isEmpty());
    }

    @Test //(Raj US18) Siblings should not marry
    public void testSiblingsShouldNotMarry() throws Exception {
        assertTrue(null != GEDCOMDataObj);

        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.siblingsShouldNotMarryCheck(GEDCOMDataObj, results);
        assertTrue(results.isEmpty());
    }
	

    @Test //(Raj US33) List orphans
    public void testOrphans() throws Exception {
        assertTrue(null != GEDCOMDataObj);

        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.orphanCheck(GEDCOMDataObj, results);
        assertTrue(results.isEmpty());
    }

    @Test //(Raj US37) List recent survivors
    public void testrecentSurvivors() throws Exception {
        assertTrue(null != GEDCOMDataObj);

        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.recentSurvivorsCheck(GEDCOMDataObj, results);
        assertTrue(results.isEmpty());
    }
	
    
    //END: *** Raj Test Cases ***
    
    //-------------------------------------------------------

    //Begin Charles' Unit Test   
    
    @Test //(Charles US06) Divorce Before Death
    public void testDivorceBeforeDeath() throws Exception {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        
        FamilyEntity family = new FamilyEntity();
        family.Husband = new PersonEntity();
        family.Husband.DeathDate = simpleDateFormat.parse("2014-09-09");
        family.Wife = new PersonEntity();
        family.Wife.DeathDate = simpleDateFormat.parse("2018-09-09");
        family.Divorce = new FactEntity();
        family.Divorce.Date = simpleDateFormat.parse("2018-09-09");
                
        List<ValidationResult> results = new ArrayList<ValidationResult>(); 
        FamilyEntityValidator.divorceBeforeDeathCheck(family, results);

        assertFalse(results.isEmpty());
    }    
            
    @Test //(Charles US10) Marriage After 14
    public void testMarriageAfterFourteen() throws Exception {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        
        FamilyEntity family = new FamilyEntity();
        
        family.Husband = new PersonEntity();
        family.Husband.BirthDate = simpleDateFormat.parse("2000-01-01");
        
        family.Wife = new PersonEntity();
        family.Wife.BirthDate = simpleDateFormat.parse("2000-02-01");
        
        family.Marriage = new FactEntity();
        family.MarriageDate = simpleDateFormat.parse("2014-01-02");
                
        List<ValidationResult> results = new ArrayList<ValidationResult>(); 
        FamilyEntityValidator.marriageAfterFourteen(family, results);

        assertFalse(results.isEmpty());
    }    
    
    @Test //(Charles US12) Parents not too old
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

    @Test //Charles US14 Multiple Births less than or equal to Five
    public void testMultipleBirthsLessThanOrEqualToFive() throws Exception {
        GEDCOMData gedcomData = new GEDCOMData(); 
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        
        for(int i=1; i<=6; i++){
            PersonEntity child = new PersonEntity();
            child.setId(Integer.toString(i));
            child.BirthDate = simpleDateFormat.parse("2014-01-02");
            gedcomData.addIndividual(child);
        }                
        
        FamilyEntity family = new FamilyEntity();
        family.ChildrenId = Arrays.asList("1","2","3","4","5","6");
        gedcomData.addFamily(family);
        
        List<ValidationResult> results = new ArrayList<ValidationResult>(); 
        FamilyEntityValidator.multipleBirthsLessThanOrEqualToFive(family, results);

        assertFalse(results.isEmpty());
    }           
    
    @Test //Charles US28 Order Siblings by Age
    public void testOrderSiblingsByAge() throws Exception {
        GEDCOMData gcd = new GEDCOMData();

        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElement5 = {"0 @I5@ INDI", "1 BIRT", "2 DATE 1 JAN 1985"};
        String[] childElement4 = {"0 @I4@ INDI", "1 BIRT", "2 DATE 1 JAN 1984"};
        String[] childElement2 = {"0 @I2@ INDI", "1 BIRT", "2 DATE 1 JAN 1982"};
        String[] childElement3 = {"0 @I3@ INDI", "1 BIRT", "2 DATE 1 JAN 1983"};
        String[] childElement1 = {"0 @I1@ INDI", "1 BIRT", "2 DATE 1 JAN 1981"};

        String[] parentElements = {"0 @I6@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1926"};

        FamilyEntity family = FamilyEntity.create(familyElements);
        
        PersonEntity child1 = PersonEntity.create(childElement1);
        PersonEntity child2 = PersonEntity.create(childElement2);
        PersonEntity child3 = PersonEntity.create(childElement3);
        PersonEntity child4 = PersonEntity.create(childElement4);
        PersonEntity child5 = PersonEntity.create(childElement5);

        gcd.addIndividual(child1);
        gcd.addIndividual(child2);
        gcd.addIndividual(child3);
        gcd.addIndividual(child4);
        gcd.addIndividual(child5);
        
        PersonEntity parent = PersonEntity.create(parentElements);

        family.Husband = parent;
        
        family.ChildrenId.add(child1.getId());
        family.ChildrenId.add(child2.getId());
        family.ChildrenId.add(child3.getId());
        family.ChildrenId.add(child4.getId());
        family.ChildrenId.add(child5.getId());
        
        gcd.addFamily(family);
        
        String results = gcd.toFamiliesText();

        assertTrue(results.contains("I1, I2, I3, I4, I5"));
    }
    
    @Test //Charles US29 List deceased
    public void testListDeceased() throws Exception {
        GEDCOMData gcd = new GEDCOMData();

        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElement5 = {"0 @I5@ INDI", "1 BIRT", "2 DATE 1 JAN 1985", "1 FAMS @F1@"};
        String[] childElement4 = {"0 @I4@ INDI", "1 BIRT", "2 DATE 1 JAN 1984", "1 FAMS @F1@"};
        String[] childElement2 = {"0 @I2@ INDI", "1 BIRT", "2 DATE 1 JAN 1982", "1 FAMS @F1@"};
        String[] childElement3 = {"0 @I3@ INDI", "1 BIRT", "2 DATE 1 JAN 1983", "1 FAMS @F1@"};
        String[] childElement1 = {"0 @I1@ INDI", "1 BIRT", "2 DATE 1 JAN 1981", "1 FAMS @F1@"};

        String[] parentElements = {"0 @I6@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1926"};

        FamilyEntity family = FamilyEntity.create(familyElements);
        List<FamilyEntity> families = new ArrayList<FamilyEntity>();
        families.add(family);
        
        PersonEntity child1 = PersonEntity.create(childElement1);
        child1.DeathDate = new Date(2000, 12, 1);
        child1.setFamilies(families);
        PersonEntity child2 = PersonEntity.create(childElement2);
        child2.DeathDate = new Date(2000, 12, 1);
        child2.setFamilies(families);
        PersonEntity child3 = PersonEntity.create(childElement3);
        child3.DeathDate = new Date(2000, 12, 1);
        child3.setFamilies(families);
        PersonEntity child4 = PersonEntity.create(childElement4);
        child4.setFamilies(families);
        PersonEntity child5 = PersonEntity.create(childElement5);
        child5.setFamilies(families);

        gcd.addIndividual(child1);
        gcd.addIndividual(child2);
        gcd.addIndividual(child3);
        gcd.addIndividual(child4);
        gcd.addIndividual(child5);
        
        PersonEntity parent = PersonEntity.create(parentElements);
        gcd.addIndividual(parent);
        parent.DeathDate = new Date(2000, 12, 1);
        parent.setFamilies(families);
        
        family.Husband = parent;
        
        family.ChildrenId.add(child1.getId());
        family.ChildrenId.add(child2.getId());
        family.ChildrenId.add(child3.getId());
        family.ChildrenId.add(child4.getId());
        family.ChildrenId.add(child5.getId());
        
        gcd.addFamily(family);
        
        String results = gcd.listDeceased();
        
        assertTrue(results.contains("I1"));
        assertTrue(results.contains("I2"));
        assertTrue(results.contains("I3"));
        assertTrue(results.contains("I6"));
        assertFalse(results.contains("I4"));
        assertFalse(results.contains("I5"));
    }       
    
    //End Charles' Unit Tests

    //-------------------------------------------------------

    //Begin Bella Unit Test  

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

    @Test //Bella US30 List living married
    public void testListLivingMarried() throws Exception {
    	 GEDCOMData gcd = new GEDCOMData();
         String[] familyElements = {"1 FAMS @F1@"};
         String[] childElement2 = {"0 @I2@ INDI", "1 BIRT", "2 DATE 1 JAN 1982", "1 FAMS @F1@"};
         String[] childElement1 = {"0 @I1@ INDI", "1 BIRT", "2 DATE 1 JAN 1981", "1 FAMS @F1@"};
         String[] familyElements2 = {"1 FAMS @F2@"};
         String[] childElement6 = {"0 @I6@ INDI", "1 BIRT", "2 DATE 1 JAN 1985", "1 FAMS @F2@"};
         String[] parentElements = {"0 @I9@ INDI", "1 NAME JOHN /Doe/", "1 SEX M", "1 BIRT", "2 DATE 1 JAN 1926"};
         String[] parentElements2 = {"0 @I10@ INDI", "1 NAME MARY /Doe/", "1 SEX F", "1 BIRT", "2 DATE 1 JAN 1926"};
        
         
         FamilyEntity family = new FamilyEntity();
         family = FamilyEntity.create(familyElements);
         List<FamilyEntity> families = new ArrayList<FamilyEntity>();
         families.add(family);
         
         //FamilyEntity.Marriage = new FactEntity();
         //FamilyEntity.Marriage.Date = new Date(99, 3, 30);
         //FamilyEntity.Divorce = new FactEntity();
         //FamilyEntity.Divorce.Date = new Date(96, 2, 4);
         
         PersonEntity child1 = PersonEntity.create(childElement1);
         child1.DeathDate = new Date(99, 12, 1);
         child1.setFamilies(families);
         PersonEntity child2 = PersonEntity.create(childElement2);
         child2.DeathDate = new Date(89, 12, 1);
         child2.setFamilies(families);
         PersonEntity child6 = PersonEntity.create(childElement6);
         child6.setFamilies(families);
         gcd.addIndividual(child1);
         gcd.addIndividual(child2);
         gcd.addIndividual(child6);
         
         PersonEntity parent = PersonEntity.create(parentElements);
         gcd.addIndividual(parent);
         parent.DeathDate = new Date(90, 12, 1);
         parent.setFamilies(families);
         
         PersonEntity parent2 = PersonEntity.create(parentElements2);
         gcd.addIndividual(parent2);
         parent2.setFamilies(families);
         
         family.Husband = parent;
         family.Wife = parent2;
         
         family.ChildrenId.add(child1.getId());
         family.ChildrenId.add(child2.getId());
         family.ChildrenId.add(child6.getId());
         gcd.addFamily(family);
         
         String results = gcd.listLivingMarried();
         
         assertTrue(results.contains("MARY"));
    }       
    @Test //Bella US31 List living single
    public void testListLivingSingle() throws Exception {
        GEDCOMData gcd = new GEDCOMData();
        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElement5 = {"0 @I5@ INDI", "1 BIRT", "2 DATE 1 JAN 1985", "1 FAMS @F1@"};
        String[] childElement4 = {"0 @I4@ INDI", "1 BIRT", "2 DATE 1 JAN 1984", "1 FAMS @F1@"};
        String[] childElement2 = {"0 @I2@ INDI", "1 BIRT", "2 DATE 1 JAN 1982", "1 FAMS @F1@"};
        String[] childElement3 = {"0 @I3@ INDI", "1 BIRT", "2 DATE 1 JAN 1983", "1 FAMS @F1@"};
        String[] childElement1 = {"0 @I1@ INDI", "1 BIRT", "2 DATE 1 JAN 1981", "1 FAMS @F1@"};
        String[] familyElements2 = {"1 FAMS @F2@"};
        String[] childElement6 = {"0 @I6@ INDI", "1 BIRT", "2 DATE 1 JAN 1985", "1 FAMS @F2@"};
        String[] childElement7 = {"0 @I7@ INDI", "1 BIRT", "2 DATE 1 JAN 1984", "1 FAMS @F2@"};
        String[] childElement8 = {"0 @I8@ INDI", "1 BIRT", "2 DATE 1 JAN 1982", "1 FAMS @F2@"};
        String[] parentElements = {"0 @I9@ INDI", "1 NAME JOHN /Doe/", "1 SEX M", "1 BIRT", "2 DATE 1 JAN 1926"};
        String[] parentElements2 = {"0 @I10@ INDI", "1 NAME MARY /Doe/", "1 SEX F", "1 BIRT", "2 DATE 1 JAN 1926"};
        
        FamilyEntity family = new FamilyEntity();
        family = FamilyEntity.create(familyElements);
        List<FamilyEntity> families = new ArrayList<FamilyEntity>();
        families.add(family);
        
        //FamilyEntity.Marriage = new FactEntity();
        //FamilyEntity.Marriage.Date = new Date(99, 3, 30);
        //FamilyEntity.Divorce = new FactEntity();
        //FamilyEntity.Divorce.Date = new Date(96, 2, 4);
        
        PersonEntity child1 = PersonEntity.create(childElement1);
        child1.DeathDate = new Date(99, 12, 1);
        child1.setFamilies(families);
        PersonEntity child2 = PersonEntity.create(childElement2);
        child2.DeathDate = new Date(89, 12, 1);
        child2.setFamilies(families);
        PersonEntity child3 = PersonEntity.create(childElement3);
        child3.DeathDate = new Date(80, 12, 1);
        child3.setFamilies(families);
        PersonEntity child4 = PersonEntity.create(childElement4);
        child4.setFamilies(families);
        PersonEntity child5 = PersonEntity.create(childElement5);
        child5.setFamilies(families);
        PersonEntity child6 = PersonEntity.create(childElement6);
        child6.setFamilies(families);
        PersonEntity child7 = PersonEntity.create(childElement7);
        child7.setFamilies(families);
        PersonEntity child8 = PersonEntity.create(childElement8);
        child8.DeathDate = new Date(90, 12, 1);
        child8.setFamilies(families);
        gcd.addIndividual(child1);
        gcd.addIndividual(child2);
        gcd.addIndividual(child3);
        gcd.addIndividual(child4);
        gcd.addIndividual(child5);
        gcd.addIndividual(child6);
        gcd.addIndividual(child7);
        gcd.addIndividual(child8);
        
        PersonEntity parent = PersonEntity.create(parentElements);
        gcd.addIndividual(parent);
        parent.DeathDate = new Date(90, 12, 1);
        parent.setFamilies(families);
        
        PersonEntity parent2 = PersonEntity.create(parentElements2);
        gcd.addIndividual(parent2);
        parent2.setFamilies(families);
        
        family.Husband = parent;
        family.Wife = parent2;
        
        family.ChildrenId.add(child1.getId());
        family.ChildrenId.add(child2.getId());
        family.ChildrenId.add(child3.getId());
        family.ChildrenId.add(child4.getId());
        family.ChildrenId.add(child5.getId());
        family.ChildrenId.add(child6.getId());
        family.ChildrenId.add(child7.getId());
        family.ChildrenId.add(child8.getId());
        
        gcd.addFamily(family);
        
        String results = gcd.listLivingSingle();
        
        assertTrue(results.contains("I4"));
        assertTrue(results.contains("I5"));
        assertTrue(results.contains("I6"));
        assertTrue(results.contains("I7"));
    }     
    
    @Test // US36 list recent deaths
    public void testListRecentDeaths() throws Exception {
    	GEDCOMData gcd = new GEDCOMData();

        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElement5 = {"0 @I5@ INDI", "1 BIRT", "2 DATE 5 APR 2017", "1 FAMS @F1@"};
        String[] childElement4 = {"0 @I4@ INDI", "1 BIRT", "2 DATE 1 JAN 1984", "1 FAMS @F1@"};
        String[] childElement2 = {"0 @I2@ INDI", "1 BIRT", "2 DATE 4 APR 2017", "1 FAMS @F1@"};
        String[] childElement3 = {"0 @I3@ INDI", "1 BIRT", "2 DATE 2 APR 2017", "1 FAMS @F1@"};
        String[] childElement1 = {"0 @I1@ INDI", "1 BIRT", "2 DATE 1 JAN 1982", "1 FAMS @F1@"};

        String[] parentElements = {"0 @I6@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1926"};

        FamilyEntity family = FamilyEntity.create(familyElements);
        List<FamilyEntity> families = new ArrayList<FamilyEntity>();
        families.add(family);
        
        PersonEntity child1 = PersonEntity.create(childElement1);
        child1.setFamilies(families);
        child1.DeathDate = new Date(2019, 4, 6);
        PersonEntity child2 = PersonEntity.create(childElement2);
        child2.setFamilies(families);
        child2.DeathDate = new Date(2019, 4, 7);
        PersonEntity child3 = PersonEntity.create(childElement3);
        child3.setFamilies(families);
        PersonEntity child4 = PersonEntity.create(childElement4);
        child4.setFamilies(families);
        PersonEntity child5 = PersonEntity.create(childElement5);
        child5.setFamilies(families);

        gcd.addIndividual(child1);
        gcd.addIndividual(child2);
        gcd.addIndividual(child3);
        gcd.addIndividual(child4);
        gcd.addIndividual(child5);
        
        PersonEntity parent = PersonEntity.create(parentElements);
        gcd.addIndividual(parent);
        parent.setFamilies(families);
        
        family.Husband = parent;
        
        family.ChildrenId.add(child1.getId());
        family.ChildrenId.add(child2.getId());
        family.ChildrenId.add(child3.getId());
        family.ChildrenId.add(child4.getId());
        family.ChildrenId.add(child5.getId());
        
        gcd.addFamily(family);
        
        String results = gcd.listRecentDeaths();
        
        assertTrue(results.contains("I5"));
        assertTrue(results.contains("I2"));
        assertTrue(results.contains("I3"));
        assertFalse(results.contains("I1"));
        assertFalse(results.contains("I4"));
    }

    @Test // US35 list recent births
    public void testListRecentBirths() throws Exception {
    	GEDCOMData gcd = new GEDCOMData();

        String[] familyElements = {"1 FAMS @F1@"};
        String[] childElement5 = {"0 @I5@ INDI", "1 BIRT", "2 DATE 5 APR 2019", "1 FAMS @F1@"};
        String[] childElement4 = {"0 @I4@ INDI", "1 BIRT", "2 DATE 1 JAN 1984", "1 FAMS @F1@"};
        String[] childElement2 = {"0 @I2@ INDI", "1 BIRT", "2 DATE 4 APR 2019", "1 FAMS @F1@"};
        String[] childElement3 = {"0 @I3@ INDI", "1 BIRT", "2 DATE 2 APR 2019", "1 FAMS @F1@"};
        String[] childElement1 = {"0 @I1@ INDI", "1 BIRT", "2 DATE 1 JAN 1982", "1 FAMS @F1@"};

        String[] parentElements = {"0 @I6@ INDI",
            "1 BIRT",
            "2 DATE 1 JAN 1926"};

        FamilyEntity family = FamilyEntity.create(familyElements);
        List<FamilyEntity> families = new ArrayList<FamilyEntity>();
        families.add(family);
        
        PersonEntity child1 = PersonEntity.create(childElement1);
        child1.setFamilies(families);
        PersonEntity child2 = PersonEntity.create(childElement2);
        child2.setFamilies(families);
        PersonEntity child3 = PersonEntity.create(childElement3);
        child3.setFamilies(families);
        PersonEntity child4 = PersonEntity.create(childElement4);
        child4.setFamilies(families);
        PersonEntity child5 = PersonEntity.create(childElement5);
        child5.setFamilies(families);

        gcd.addIndividual(child1);
        gcd.addIndividual(child2);
        gcd.addIndividual(child3);
        gcd.addIndividual(child4);
        gcd.addIndividual(child5);
        
        PersonEntity parent = PersonEntity.create(parentElements);
        gcd.addIndividual(parent);
        parent.setFamilies(families);
        
        family.Husband = parent;
        
        family.ChildrenId.add(child1.getId());
        family.ChildrenId.add(child2.getId());
        family.ChildrenId.add(child3.getId());
        family.ChildrenId.add(child4.getId());
        family.ChildrenId.add(child5.getId());
        
        gcd.addFamily(family);
        
        String results = gcd.listRecentBirths();
        
        assertTrue(results.contains("I1"));
        assertTrue(results.contains("I2"));
        assertFalse(results.contains("I3"));
        assertFalse(results.contains("I5"));
        assertFalse(results.contains("I4"));
    }     
    


    //End Bella Unit Tests

    //-------------------------------------------------------

    //Begin Craig Unit Test  


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

@Test //(Craig US21) Correct gender roles
    public void testGenderRoles() throws Exception {

    	 FamilyEntity entity = new FamilyEntity();
         List<ValidationResult> results = new ArrayList<ValidationResult>();
         
         entity.Husband = new PersonEntity();
         entity.Wife = new PersonEntity();
         entity.Husband.Gender = "F";
         entity.Wife.Gender = "M";

         
         FamilyEntityValidator.genderCheck(entity, results);
         
         assertFalse(results.isEmpty());
    }


    @Test //(Craig US25) Unique first name and birth date in family
    public void testUniqueNamesinFamily() throws Exception {

        GEDCOMDataObj.getIndividuals().forEach((k, v) -> {
            assertFalse(GEDCOMDataObj.getIndividuals().containsValue(v.FullName + v.BirthDate + v.ChildhoodFamilyIds.toString()));

        });

    }
    
    @Test //(Craig US08) Birth before marriage of parents
    public void testUS08() throws Exception {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    	FamilyEntity entity = new FamilyEntity();
        PersonEntity child = new PersonEntity();
        
        child.BirthDate = dateformat.parse("1995-01-01");
        entity.MarriageDate = dateformat.parse("2000-01-01");
		
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		FamilyEntityValidator.birthBeforeMarriageParents(entity, results);
		assertTrue(results.isEmpty());

    }
    
    @Test //(Craig US09) Birth before death of parents
    public void testUS09() throws Exception {
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    	FamilyEntity entity = new FamilyEntity();
        entity.Wife = new PersonEntity();
        entity.Wife.DeathDate = dateformat.parse("2000-01-01");
        
        entity.Husband = new PersonEntity();
        entity.Husband.DeathDate = dateformat.parse("1999-01-01");
        
        PersonEntity child = new PersonEntity();
        child.BirthDate = dateformat.parse("1995-01-01");
        
		
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		FamilyEntityValidator.birthBeforeDeathParents(entity, results);
		assertTrue(results.isEmpty());

    }

}
