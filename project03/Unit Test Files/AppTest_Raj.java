package project03;

import java.util.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Cinchoo, czuppe, Bella458, cjarret1
 */
public class AppTest_Raj {

    GEDCOMData GEDCOMDataObj;

    public AppTest_Raj() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        GEDCOMDataObj = App.parseGEDFromFile("C:\\Temp\\MS\\SSW -555-WS\\My-Family-7-Feb-2019-240.ged");
    }

    @After
    public void tearDown() {
    }

    @Test //(Raj US15) There should be fewer than 15 siblings in a family
    public void testFewerThan15Siblings() throws Exception {
        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataObj.getFamilies().forEach((k, v) -> {
            FamilyEntityValidator.fewerThan15SiblingsCheck(v, results);
        });
        assertTrue(results.isEmpty());
    }

    @Test //(Raj US16) All male members of a family should have the same last name
    public void testAllMaleMembersShouldHaveSameLastName() throws Exception {
        List<ValidationResult> results = new ArrayList<>();
        GEDCOMDataValidator.maleLastNameCheck(GEDCOMDataObj, results);
        assertTrue(results.isEmpty());
    }
}
