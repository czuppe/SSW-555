/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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
        GEDCOMDataObj = App.parseGEDFile("C:\\Temp\\MS\\SSW -555-WS\\My-Family-7-Feb-2019-240.ged");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testBirthDatesMustExists() throws Exception {
        GEDCOMDataObj.Individuals.forEach((k, entity) -> {
            assertNotNull(entity.BirthDate);
        });
    }

    @Test
    public void testBirthDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.Individuals.forEach((k, entity) -> {
            assertTrue(entity.BirthDate != null && entity.BirthDate.before(todaysDate));
        });
    }

    @Test
    public void testDeathDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.Individuals.forEach((k, entity) -> {
            if (entity.DeathDate != null) {
                assertTrue(entity.DeathDate.before(todaysDate));
            }
        });
    }

    @Test
    public void testMarriageDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.Families.forEach((entity) -> {
            if (entity.MarriageDate != null) {
                assertTrue(entity.MarriageDate.before(todaysDate));
            }
        });
    }

    @Test
    public void testDivorceDatesBeforeTodaysDate() throws Exception {
        Date todaysDate = Utility.getTodaysDate();

        GEDCOMDataObj.Families.forEach((entity) -> {
            if (entity.DivorceDate != null) {
                assertTrue(entity.DivorceDate.before(todaysDate));
            }
        });
    }

    @Test //(Bella US03)
    public void testBirthDatesBeforeDeathDate() throws Exception {
        GEDCOMDataObj.Families.forEach((entity) -> {
            if (entity.Husband != null && entity.Husband.DeathDate != null && entity.Husband.BirthDate != null) {
                assertTrue(entity.Husband.BirthDate.before(entity.Husband.DeathDate));
            }
            if (entity.Wife != null && entity.Wife.DeathDate != null && entity.Wife.BirthDate != null) {
                assertTrue(entity.Wife.BirthDate.before(entity.Wife.DeathDate));
            }
        });
    }
}
