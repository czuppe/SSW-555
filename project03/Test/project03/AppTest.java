/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.io.File;
import java.net.URL;
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

    @Test
    public void testParentsNotTooOld() throws Exception {
        URL testGed = getClass().getResource("test.ged");
        URL testGedOut = getClass().getResource("test.ged.out");
        
        String[] args = {testGed.getPath()};
        App.main(args);
        File gedOutputFile = new File(testGedOut.getPath());
        Scanner gedOutput = new Scanner(gedOutputFile);        
        boolean foundExpected = false;

        while(gedOutput.hasNext()) {
            String nextLine = gedOutput.nextLine();
            System.out.println("Line = " + nextLine);
            if(nextLine.contains("Mother should be less than 60 years older than her children and father should be less than 80 years older than his children.")){
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
            if(nextLine.contains("Divorce can only occur before death of both spouses.")){
                foundExpected = true;
                break;
            }            
        }
        assertTrue(foundExpected);
        gedOutput.close();        
    }
}
