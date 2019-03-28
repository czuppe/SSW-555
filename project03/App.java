/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.util.*;
import java.io.*;
import java.net.*;
import java.text.ParseException;

/**
 *
 * @author nraj39
 */
public class App {

    static HashMap<String, String> tags = new HashMap<String, String>();

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException {
        if (args == null || args.length == 0 || Utility.isNullOrBlank(args[0])) {
            System.out.println("Missing GED file.");
            return;
        }
        String gedFilePath = args[0];

        File f = new File(gedFilePath);
        if (!f.exists()) {
            System.out.println("'" + gedFilePath + "' file not exists.");
            return;
        }

        System.out.println("Loading '" + gedFilePath + "' file...");

        initialize();

        //Parse input file
        GEDCOMData GEDCOMDataObj = parseGEDFromFile(gedFilePath);

        //Print results
        print(gedFilePath, GEDCOMDataObj);
    }

    private static void initialize() {
        tags.put("INDI", "0");
        tags.put("NAME", "1");
        tags.put("SEX", "1");
        tags.put("BIRT", "1");
        tags.put("DEAT", "1");
        tags.put("FAMC", "1");
        tags.put("FAMS", "1");
        tags.put("FAM", "0");
        tags.put("MARR", "1");
        tags.put("HUSB", "1");
        tags.put("WIFE", "1");
        tags.put("CHIL", "1");
        tags.put("DIV", "1");
        tags.put("DATE", "2");
        tags.put("HEAD", "0");
        tags.put("TSLR", "0");
        tags.put("NOTE", "0");
    }


    public static GEDCOMData parseGEDFromURI(URI gedFileURI) throws FileNotFoundException, IOException, ParseException {
        return parseGEDFromFile(new File(gedFileURI).getPath());
    }

    public static GEDCOMData parseGEDFromFile(String gedFilePath) throws FileNotFoundException, IOException, ParseException {
        String gedcomFileContent = Utility.readFileAsString(gedFilePath);
        return parseGEDFromText(gedcomFileContent);
    }

    public static GEDCOMData parseGEDFromText(String gedcomFileContent) throws ParseException {

        GEDCOMData GEDCOMDataObj = new GEDCOMData();
        String[] records = gedcomFileContent.replaceAll("0 @", "\u0646@").split("\u0646");

        for (String record : records) {
            if (record.startsWith("@") && !record.startsWith("@SUBM@ SUBM")) {
                String[] elements = record.split("\n");

                String recordType = elements[0].replaceAll(" ", "").split("@")[2];

                if (recordType.equals("FAM")) {
                    GEDCOMDataObj.addFamily(FamilyEntity.create(elements));
                } else if (recordType.equals("INDI")) {
                    GEDCOMDataObj.addIndividual(PersonEntity.create(elements));
                } else if (recordType.equals("NOTE")) {

                }
            }
        }
        
        GEDCOMDataObj.loadReferenceEntitiesToFamily();

        return GEDCOMDataObj;
    }

    private static void print(String gedFilePath, GEDCOMData GEDCOMDataObj) throws IOException {
        String gedOutFilePath = gedFilePath + ".out";
        FileWriter outfw = new FileWriter(gedOutFilePath);
        PrintWriter writer = new PrintWriter(outfw);

        //Validate entities
        List<ValidationResult> results = new ArrayList();
        GEDCOMDataObj.Validate(results);

        try {
            String msg = GEDCOMDataObj.toPersonsText();
            System.out.println();
            System.out.println("Individuals:");
            System.out.println();
            System.out.println(msg);
            writer.println(msg);
            System.out.println();

            msg = GEDCOMDataObj.toFamiliesText();
            System.out.println("Families:");
            System.out.println();
            System.out.println(msg);
            writer.println(msg);
            System.out.println();
            
            msg = GEDCOMDataObj.listLivingMarried();
            System.out.println("Living Married Individuals:");
            System.out.println();
            System.out.println(msg);
            writer.println(msg);
            System.out.println();
            
            msg = GEDCOMDataObj.listLivingSingle();
            System.out.println("Living Single Individuals:");
            System.out.println();
            System.out.println(msg);
            writer.println(msg);
            System.out.println();
            
            msg = GEDCOMDataObj.listDeceased();
            System.out.println("Deceased Individuals:");
            System.out.println();
            System.out.println(msg);
            writer.println(msg);
            System.out.println();
            

            printValidationResults(writer, results);
        } finally {
            //Close the input stream
            outfw.close();
        }
    }

    private static void printValidationResults(PrintWriter writer, List<ValidationResult> results) throws IOException {
        if (results != null && !results.isEmpty()) {
            for (ValidationResult result : results) {
                System.out.println(result.toString());
                writer.println(result.toString());
            }
        } else {
            String noResultMsg = "No errors found.";
            System.out.println(noResultMsg);
            writer.println(noResultMsg);
        }
    }
}