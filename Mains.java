package com.filemanipulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mains {

	public static void main(String[] args) throws IOException {
		//taking file name from the command line or path
		//System.out.println(args[0]);
		String excelfilepath = args[0];
		String line = "";
		//printing the line
	    //System.out.printf("The input file path you entered is " + excelfilepath);
	    //getting the filenaem
	    //getting latest time
	    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	    File inputfile = new File(excelfilepath);
	    String outputfilename = inputfile.getName();
	    String outputfilename1 = outputfilename.replaceFirst("[.][^.]+$", "");
	    String outputfilefullname = timeStamp +"_"+ outputfilename1 + "_CALC.csv";
	    Integer lineno = 0;
	    //creating hash
	    ////hash for uniqid and its frequency
	    Map <String, Integer> freq = new HashMap <String, Integer>();
	    //hash for uniqid and line number
	    Map <Integer,String> linePosition = new HashMap <Integer, String>();
	    try {
			BufferedReader br = new BufferedReader(new FileReader(excelfilepath));
			while ((line =br.readLine()) != null) {
				lineno++;
				if (lineno < 2) {
					continue;
				}
				//string[] is string array
				String[] values = line.split(",");
				//System.out.println(values[0]+","+values[1]);
				String samplenames = values[0];
				//System.out.println(samplenames);
				String pats = "(....+).{6}$";
				//create a pattern object
				Pattern r = Pattern.compile(pats);
				//create a matcher object
				Matcher m = r.matcher(samplenames);
				if (m.find()) {
					String partsamp  = m.group(1);
					String uniqid = partsamp + "_" + values[2] + "_" + values[3] + "_" + values[5];
					linePosition.put(lineno, uniqid);
					Integer count = freq.get(uniqid);
					if (count == null) {
						freq.put(uniqid, 1);
					} else {
						freq.put(uniqid, count + 1);
					}
					
					//System.out.println(partsamp);
					//System.out.println(uniqid);
				}else {
					//System.out.println("Sample names are not in expected pattern\n");
					throw new RuntimeException("Sample names are not in expected pattern");	
				}
				//Map <uniqid,Integer> map = new Hashmap<>();
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//
		}
	    //printing the hash freq
	    //for (Map.Entry<String, Integer> entry : freq.entrySet()) {
	    //	String key = entry.getKey();
	    //	Integer value = entry.getValue();
	    //	System.out.println(key + " " + value);
	    //}
	    //hash for line numbers as key and repeats within the sample
	    Map <Integer,Integer> finalHash = new HashMap <Integer, Integer>();
	    //count hash
	    //Integer counti = 0;
	    linePosition.forEach((k, v) -> {
	    	Integer repeats = freq.get(v);
	    	//putting line number and repeats
	    	finalHash.put(k,repeats);
	    	//System.out.println(k +" "+ repeats);
	    	//System.out.println(counti);
	    	});
	    //System.out.println("finalhashSize : " + finalHash.size());
//	}
//}
	    	FileOutputStream out = null;
	    	out = new FileOutputStream(outputfilefullname);
	 		Integer linecount = 0;
        	try {
    		BufferedReader re = new BufferedReader(new FileReader(excelfilepath));
	    	out = new FileOutputStream(outputfilefullname);
	    		while ((line =re.readLine()) != null) {			
	    			linecount++;
	    			String finalline;
	    			if (linecount == 1) {
	    				finalline = "," + line + "\n";
	    			}else{
	    				Integer repeatsc = finalHash.get(linecount);
	    				finalline = repeatsc + "," + line + "\n";
	    			}
	    			//System.out.println(linecount + " " + finalline);
	    			byte[] byteline = finalline.getBytes();
	    			out.write(byteline);
				}
	    	}catch (FileNotFoundException e) {
	    	//
	    	}
			finally {
				//			
				out.close();    	
	    	}
}
}