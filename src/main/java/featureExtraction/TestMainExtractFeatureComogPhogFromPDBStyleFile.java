
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featureExtraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.apache.lucene.analysis.standard.StandardTokenizer;
//import org.apache.log4j.*;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
/**
 * 
 * @author acer
 */
public class TestMainExtractFeatureComogPhogFromPDBStyleFile {

	
	public static void main(String[] args) {
	
		File fin=new File("input\\d3rmkd_.txt");
		ComogPhogFeatureExtractor extractor=new ComogPhogFeatureExtractor();
		String comogPhog=extractor.runFeatureExtraction(fin);

		File outDir=new File("output");
		if(!outDir.exists()||!outDir.isDirectory())
		{
			outDir.mkdir();
		}
		File fout=new File(outDir,fin.getName());
		try {
			PrintWriter pw = new PrintWriter(fout);
			pw.println(comogPhog);
			pw.flush();
			pw.close();
		} catch (Exception e) {
	e.printStackTrace();
		}
	}
	
	

}
