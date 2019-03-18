import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class GEDCOMParser {	
	
	static List<String> validTagList = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		HashMap<String, Integer> tagHash = new HashMap<String, Integer>();
		
		tagHash.put("INDI", 0);
		tagHash.put("NAME", 1);
		tagHash.put("SEX",  1);
		tagHash.put("BIRT", 1);
		tagHash.put("DEAT", 1);
		tagHash.put("FAMC", 1);
		tagHash.put("FAMS", 1);
		tagHash.put("FAM",  0);
		tagHash.put("MARR", 1);
		tagHash.put("HUSB", 1);
		tagHash.put("WIFE", 1);
		tagHash.put("CHIL", 1);
		tagHash.put("DIV",  1);
		tagHash.put("DATE", 2);
		tagHash.put("HEAD", 0);
		tagHash.put("TRLR", 0);
		tagHash.put("NOTE", 0);

		while(true) {
			Scanner scanner = new Scanner(System.in); 
		    System.out.print("Enter name of GEDCOM file : ");   
		    String fileName = scanner.nextLine();
		    System.out.println("Parsing " + fileName + " ...");
			File file = new File(fileName);
			
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			    try {
					for(String line; (line = br.readLine()) != null; ) {
						System.out.println("--> " + line);
						String level = line.split("\\s+")[0];
						String tag = line.split("\\s+")[1];
						String argument = line.substring(level.length() + tag.length() + 1).trim();
						boolean lineIsValid = false;
		
						if(tagHash.containsKey(tag)) {
							int tagKey = tagHash.get(tag);
							
							if(level.equals("0") && tagKey == 0){
								if(!(tag.equals("INDI")||tag.equals("FAM"))) {
									lineIsValid = true;
								}
							}else if(tagKey == 1 && level.equals("1") || 
								     tagKey == 2 && level.equals("2")){
								lineIsValid = true; 
							}
						}else if(tagHash.containsKey(argument) && level.equals("0")) {
							//assuming that the identifier for INDI or FAM can be anything
							lineIsValid = true;
							//swap tag and argument for edge cases
							String temp = tag;
							tag = argument;
							argument = temp;
						}
	
						System.out.println("<-- " 
								+ level + "|" + tag + "|"
								+ ((lineIsValid) ? "Y" : "N" ) + "|"
								+ argument);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				System.out.println("File not found, please try again.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
