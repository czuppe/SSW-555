/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Charles
 */
public class AppTest {
    
    public AppTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
        
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
      
    @Test        
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
    
    
    @Test // edge case: Mother is exactly 20 years older than child
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
    
    @Test // edge case: Mother is exactly 60 years older than child
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
    
    @Test // edge case: Mother is exactly 61 years older than child
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
    
    @Test // edge case: Father is exactly 81 years older than child
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

    @Test
    public void testParentsNotTooOldCaseInGEDFile() throws Exception {
        URL testGed = getClass().getResource("test.ged");
        URL testGedOut = getClass().getResource("test.ged.out");
        
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
    
    @Test
    public void testDivorceBeforeDeath() throws Exception {
        URL testGed = getClass().getResource("test.ged");
        URL testGedOut = getClass().getResource("test.ged.out");
        
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
}
