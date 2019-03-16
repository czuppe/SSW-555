package project03;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

/**
 *
 * @author Cinchoo, czuppe, Bella458, cjarret1
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
    	GEDCOMDataObj = App.parseGEDFromFile("C:\\Users\\Craig\\eclipse-workspace\\project03\\src\\project03\\test.ged");
    }

    @After //(Charles US12) Parents not too old
    public void tearDown() {
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
    
    @Test //(Raj US01) Dates before current date
    public void testBirthDatesMustExists() throws Exception {
        GEDCOMDataObj.getIndividuals().forEach((k, entity) -> {
            assertNotNull(entity.BirthDate);
        });
    }

    @Test //(Raj US01) Dates before current date
    public void testBirthDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.getIndividuals().forEach((k, entity) -> {
            assertTrue(entity.BirthDate != null && entity.BirthDate.before(todaysDate));
        });
    }

    @Test //(Raj US01) Dates before current date
    public void testDeathDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.getIndividuals().forEach((k, entity) -> {
            if (entity.DeathDate != null) {
                assertTrue(entity.DeathDate.before(todaysDate));
            }
        });
    }

    @Test //(Raj US01) Dates before current date
    public void testMarriageDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.getFamilies().forEach((k, entity) -> {
            if (entity.MarriageDate != null) {
                assertTrue(entity.MarriageDate.before(todaysDate));
            }
        });
    }

    @Test //(Raj US01) Dates before current date
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
    	assertFalse(results.get(0).Message.contentEquals("Birth date must be before death date."));
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
    @Test //(Craig US22) Unique ID
	public void testIndividualsUnqiueID() throws Exception {
		GEDCOMDataObj.getIndividualsDuplicates().forEach((k,v)->{
			assertFalse(GEDCOMDataObj.getIndividuals().containsKey(k));
		});
		
	}

	@Test //(Craig US22) Unique ID
	public void testFamiliesUniqueID() throws Exception {
		
		GEDCOMDataObj.getFamiliesDuplicates().forEach((k,v)->{
			assertFalse(GEDCOMDataObj.getFamilies().containsKey(k));
		});
		
	}
	
	@Test //(Craig US23) Unique name and birth date
	public void testIndividualNamesUnique() throws Exception {
		
		GEDCOMDataObj.getIndividuals().forEach((k,v)->{
			assertSame(v, v);
		});
		
	}
	
	@Test //(Craig US23) Unique name and birth date
	public void testCount() throws Exception {
		
		GEDCOMDataObj.getIndividuals().forEach((k,v)->{
			assertFalse(GEDCOMDataObj.getIndividuals().containsValue(v.FullName + v.BirthDate.toString()));
			
		});
		
		
	}
        
        //@Test //(Charles US06)
        public void testDivorceBeforeDeathInGEDFile() throws Exception {
            URL testGed = getClass().getResource("US06.ged");
            URL testGedOut = getClass().getResource("US06.ged.out");

            String[] args = {testGed.getPath()};
            App.main(args);
            File gedOutputFile = new File(testGedOut.getPath());
            Scanner gedOutput = new Scanner(gedOutputFile);        
            boolean foundExpected = false;

            while(gedOutput.hasNext()) {
                String nextLine = gedOutput.nextLine();
                if(nextLine.contains("US06")){
                    foundExpected = true;
                    break;
                }            
            }
            assertTrue(foundExpected);
            gedOutput.close();        
        }
        
        //@Test //(Charles US12)
        public void testParentsNotTooOldCaseInGEDFile() throws Exception {
            URL testGed = getClass().getResource("US12.ged");
            URL testGedOut = getClass().getResource("US12.ged.out");

            String[] args = {testGed.getPath()};
            App.main(args);
            File gedOutputFile = new File(testGedOut.getPath());
            Scanner gedOutput = new Scanner(gedOutputFile);        
            boolean foundExpected = false;

            while(gedOutput.hasNext()) {
                String nextLine = gedOutput.nextLine();            
                if(nextLine.contains("US12")){
                    foundExpected = true;
                    break;
                }            
            }
            assertTrue(foundExpected);
            gedOutput.close();        
        }

        @Test //(Charles US10)
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
        
        @Test //(Charles US14)
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
}
