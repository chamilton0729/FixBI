package com.kar.pacman.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FixBI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FixBI fixbi = new FixBI();
		
		//Debug Testing
		//int result = fixbi.processFiles("src/com/kar/pacman/tools/Source.yml", "src/com/kar/pacman/tools/Needs.yml", "src/com/kar/pacman/tools/Exclude.yml");
		//System.out.println("Missing Keys " + result);
		
		//Normal usage
		if (args.length > 0) {
			int result = fixbi.processFile(args[0]);
			System.out.println("============================");
			System.out.println("Missing Keys " + result);
		} else {
			System.out.println("No arguments passed to the application");
			System.out.println("FixBI <Needs File>");
		}
				
		System.exit(0);
	}
	
	private int processFile(String file1) {
		int result = 0;
		List<String> excludeKeys = new ArrayList<String>();
		List<String> sourceKeys = new ArrayList<String>();
		List<String> file1Keys = new ArrayList<String>();
		//Build Exclude Keys List
		excludeKeys = exclusionKeys("src/com/kar/pacman/tools/Exclude.yml");
		//Build Keys from Source.yml
		sourceKeys = grabKeysFromFile("src/com/kar/pacman/tools/Source.yml", excludeKeys);
		//Build Keys from file1
		file1Keys = grabKeysFromFile(file1, excludeKeys);
		//Result List
		List<String> missingKeys = new ArrayList<String>();
		
		//Check if Keys from Source exist in file1 (incomplete file)
		for (String oneKey : sourceKeys) {
			if (file1Keys.contains(oneKey) == false) {
				missingKeys.add(oneKey);
			}
		}
		
		//Check if there are any missing keys to output
		if (missingKeys.size() > 0) {
			//Output the missing keys
			System.out.println("============================");
			System.out.println("          Missing           ");
			System.out.println("============================");
			for (String missingKey : missingKeys) {
				System.out.println(missingKey);
			}
		}
		
		//Update result
		result = missingKeys.size();
		return result;
	}
	
	private List<String> exclusionKeys(String excludeFile) {
		List<String> keyList = new ArrayList<String>();
		//Read file
        try (BufferedReader br = Files.newBufferedReader(Paths.get(excludeFile))) {
            // read line by line
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] lineData1 = null;
                //Check which character the line contains
                if (line.contains(":")) {
                	lineData1 = line.split(":");
                } else if (line.contains("=")) {
                	lineData1 = line.split("=");
                } else {
                	//Add Key to List
                	if (line.length() > 0) {
                		//System.out.println("Adding Key: " + line.trim());
                    	keyList.add(line.trim());
                	}
                }
            	
            	//Add Key to List
            	if (lineData1 != null && lineData1.length > 0) {
            		//System.out.println("Adding Key: " + lineData1[0].trim());
                	keyList.add(lineData1[0].trim());
            	} 
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
		return keyList;
	}
	
	private List<String> grabKeysFromFile(String localFile, List<String> excludeKeys) {
		List<String> keyList = new ArrayList<String>();
		//Read file
        try (BufferedReader br = Files.newBufferedReader(Paths.get(localFile))) {
            // read line by line
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] lineData1 = null;
                //Check which character the line contains
                if (line.contains(":")) {
                	lineData1 = line.split(":");
                } else if (line.contains("=")) {
                	lineData1 = line.split("=");
                } else {
                	//Do Nothing
                }
            	
            	//Add Key to List
            	if (lineData1 != null && lineData1.length > 0) {
            		//Check if key is one we can exclude
            		if (excludeKeys.contains(lineData1[0].trim()) == false) {
            			//System.out.println("Adding Key: " + lineData1[0].trim());
                    	keyList.add(lineData1[0].trim());
        			}
            	}   
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
		return keyList;
	}
}
