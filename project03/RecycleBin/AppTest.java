/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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