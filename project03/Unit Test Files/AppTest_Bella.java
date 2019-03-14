/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bella458
 */
public class AppTest {

    GEDCOMData GEDCOMDataObj;

    public AppTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() {
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
    	assertTrue(results.get(0).Message.contains("Birth"));
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
    
    // /*
    @Test //US07: Less then 150 years old (birth and death date)
    public void testlessthan150YearsOldCheck() throws Exception {
        PersonEntity entity = new PersonEntity();
        //date constructor requires me to subtract year from 1900
        entity.BirthDate = new Date(-50,3,30);
    	entity.DeathDate = new Date(119,3,30);
    	List<ValidationResult> results = new ArrayList<ValidationResult>();
    	
    	PersonEntityValidator.lessthan150YearsOldCheck(entity, results);
    	assertTrue(results.get(0).Message.contains("Age"));
    }
	// */
    
    
    @Test //US07: Test Less then 150 years old (no death date)
    public void testlessthan150YearsOldNoDeathDateCheck() throws Exception {
        PersonEntity entity = new PersonEntity();

        //date constructor requires me to subtract year from 1900
        entity.BirthDate = new Date(-50,5,4);
    	entity.DeathDate = null;
    	List<ValidationResult> results = new ArrayList<ValidationResult>();
    	
    	PersonEntityValidator.lessthan150YearsOldCheck(entity, results);
    	assertTrue(results.get(0).Message.contains("Age"));
    }
	
    
    // /*
    @Test //US07: Test Less then 150 years old (valid)
    public void testvalidlessthan150YearsOldCheck() throws Exception {
        PersonEntity entity = new PersonEntity();

        //date constructor requires me to subtract year from 1900
        entity.BirthDate = new Date(50,1,6);
    	entity.DeathDate = new Date(100,2,3);
    	List<ValidationResult> results = new ArrayList<ValidationResult>();
    	
    	PersonEntityValidator.lessthan150YearsOldCheck(entity, results);
    	assertTrue(results.isEmpty());
    }
	// */
	 @Test //US04:	Marriage before divorce (Valid)
    public void testvalidmarriageBeforeDivorceCheck() throws Exception {
    
    	FamilyEntity entity = new FamilyEntity();
    	entity.Husband = new PersonEntity();
    	entity.Wife = new PersonEntity();
    	
    	entity.Divorce = new FactEntity();
    	entity.Marriage = new FactEntity();
    	
    	//date constructor requires me to subtract year from 1900
    	entity.Marriage.Date = entity.MarriageDate = new Date(10,3,30);
    	entity.Divorce.Date = entity.DivorceDate = new Date(20,2,4);
    	List<ValidationResult> results = new ArrayList<ValidationResult>();
    	
    	FamilyEntityValidator.marriageBeforeDivorceCheck(entity, results);
       	assertTrue(results.isEmpty());
       	//System.out.println(results); //should print message, just for additional testing
    }
	 
	 @Test //US04:	Marriage before divorce (invalid)
	   public void testinvalidmarriageBeforeDivorceCheck() throws Exception {
	    
	    FamilyEntity entity = new FamilyEntity();
	    entity.Husband = new PersonEntity();
	    entity.Wife = new PersonEntity();
	    
	   //date constructor requires me to subtract year from 1900
	    entity.Marriage = new FactEntity();
	    entity.Marriage.Date = entity.MarriageDate = new Date(99,3,30);
	    
	    entity.Divorce = new FactEntity();
	    entity.Divorce.Date = entity.DivorceDate = new Date(97,2,4);
	    
	    List<ValidationResult> results = new ArrayList<ValidationResult>();
	    FamilyEntityValidator.marriageBeforeDivorceCheck(entity, results);
	    	
	    assertTrue(results.get(0).Message.contains("Marriage"));
	    //System.out.println(results); //should print empty list, just for additional testing
	 }
}
