package project03;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.*;
import java.util.Set;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class GEDCOMDataValidatorTest {
	
	GEDCOMData GEDCOMDataObj = new GEDCOMData();

	public GEDCOMDataValidatorTest() {
		
	}
	
	
	@Test //Test #1
	public void testIndividualsUnqiueID() throws Exception {
		GEDCOMDataObj.getIndividualsDuplicates().forEach((k,v)->{
			assertFalse(GEDCOMDataObj.getIndividuals().containsKey(k));
		});
		
	}

	@Test //Test #2
	public void testFamiliesUniqueID() throws Exception {
		
		GEDCOMDataObj.getFamiliesDuplicates().forEach((k,v)->{
			assertFalse(GEDCOMDataObj.getFamilies().containsKey(k));
		});
		
	}
	
	@Test //Test # 3
	public void testIndividualNamesUnique() throws Exception {
		
		GEDCOMDataObj.getIndividuals().forEach((k,v)->{
			assertNotSame(v, v);
		});
		
	}
	
	@Test //Test # 4
	public void testCount() throws Exception {
		
		GEDCOMDataObj.getIndividuals().forEach((k,v)->{
			assertFalse(GEDCOMDataObj.getIndividuals().containsValue(v.FullName + v.BirthDate.toString()));
			
		});
		
		
	}
	

}
