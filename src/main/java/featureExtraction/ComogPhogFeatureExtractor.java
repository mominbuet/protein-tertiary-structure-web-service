package featureExtraction;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;


/**
 *
 * @author rezaul karim
 */
public class ComogPhogFeatureExtractor {

//    public String camatDirName="D:\\camat";
    static Logger logger = Logger.getLogger(ComogPhogFeatureExtractor.class);
	
    int maxCaCount = 2000;
    double xyz[][];

    public String runFeatureExtraction(String fileName) {
        return runFeatureExtraction(new File(fileName));
    }

    public String runFeatureExtraction(File pdbFormatFile) {
        xyz = new double[3][maxCaCount];
        String comogphog = "";
        if (!(pdbFormatFile.getName().endsWith(".pdb") || pdbFormatFile.getName().endsWith(".ent") || pdbFormatFile.getName().endsWith(".txt"))) {
            return comogphog;
        }
        try {
            Scanner sc = new Scanner(pdbFormatFile);
            int seqNo;
            seqNo = 0;
            while (sc.hasNext()) {
                String line = sc.nextLine();
                StringTokenizer strTok = new StringTokenizer(line, " ");
                int numOfTokens;
                numOfTokens = strTok.countTokens();
                String tokens[] = new String[numOfTokens];
                int tokenid = 0;
                while (strTok.hasMoreTokens()) {
                    tokens[tokenid++] = strTok.nextToken();
                }
                if (tokens[0].equalsIgnoreCase("ENDMDL")) {
                    // System.out.println(" first model of multiple model is considered ");
                    break;
                }
                if (tokens[0].equalsIgnoreCase("END")) {
                    break;
                }
                if (numOfTokens < 8) {
                    continue;
                }
                if (!tokens[0].equalsIgnoreCase("atom")) {
                    continue;
                }
                if (tokens[0].equalsIgnoreCase("atom")) {
                    if (tokens[2].equalsIgnoreCase("ca")) {
                        if (numOfTokens >= 12) {
                            xyz[0][seqNo] = Double.parseDouble(tokens[6]);
                            xyz[1][seqNo] = Double.parseDouble(tokens[7]);
                            xyz[2][seqNo] = Double.parseDouble(tokens[8]);
                        }
                        if (numOfTokens < 12) {
                            if (tokens[6].substring(1).contains("-")) {
                                int xendsAt = tokens[6].indexOf("-", 1);
                                xyz[0][seqNo] = Double.parseDouble(tokens[6].substring(0, xendsAt));
                                if (tokens[6].substring(xendsAt + 1).contains("-")) {
                                    int yendsAt = tokens[6].indexOf("-", xendsAt + 1);
                                    xyz[1][seqNo] = Double.parseDouble(tokens[6].substring(xendsAt + 1, yendsAt));
                                    xyz[2][seqNo] = Double.parseDouble(tokens[6].substring(yendsAt + 1));
                                } else {
                                    xyz[0][seqNo] = Double.parseDouble(tokens[6].substring(xendsAt + 1));
                                    xyz[1][seqNo] = Double.parseDouble(tokens[7]);
                                }
                            } else {
                                xyz[0][seqNo] = Double.parseDouble(tokens[6]);
                                if (tokens[7].substring(1).contains("-")) {
                                    int yendsAt = tokens[7].indexOf("-", 1);
                                    xyz[1][seqNo] = Double.parseDouble(tokens[7].substring(0, yendsAt));
                                    xyz[2][seqNo] = Double.parseDouble(tokens[7].substring(yendsAt + 1));
                                }
                            }
                        }
                        seqNo++;
                    }
                }
            }
            int numOfCaAtom = seqNo - 1;
            return runFeatureExtraction(xyz[0], xyz[1], xyz[2], numOfCaAtom);
        } catch (FileNotFoundException e) {
        	logger.fatal(e.getMessage(),e);
            return comogphog;
        } catch (NumberFormatException e) {
        	logger.fatal(e.getMessage(),e);
            return comogphog;
        }
    }

    public String runFeatureExtraction(double x[], double y[], double z[], int numOfCAatom) {
        try {
            double[][] calphadistmat = new double[numOfCAatom][numOfCAatom];
            double maxDistance = -1;
            double minDistance=100000000;
            int n = 0;
            double totalDistData[] = new double[numOfCAatom * numOfCAatom];
            for (int j = 0; j < numOfCAatom; j++) {
                for (int k = 0; k < numOfCAatom; k++) {
                    double dist = Math.sqrt((x[j] - x[k]) * (x[j] - x[k]) + (y[j] - y[k]) * (y[j] - y[k]) + (z[j] - z[k]) * (z[j] - z[k]));
                    maxDistance = Math.max(maxDistance, dist);
                    minDistance=Math.min(minDistance, dist);
                    totalDistData[n++] = dist;
                }
            }
//        System.out.print("Maximum Distance: ");
//        System.out.println(maxDistance);
            int noQuantLevel = 255;
            int camatq1maxval = (int) (maxDistance * 2);
            for (int j = 0; j < numOfCAatom; j++) {
                for (int k = 0; k < numOfCAatom; k++) {
                    int valq2 = (int) ((totalDistData[j * numOfCAatom + k]-minDistance) * noQuantLevel / (maxDistance-minDistance));
                    calphadistmat[j][k] = valq2;
                }
            }
            
//            printCalphamatImage(calphadistmat, numOfCAatom);
            return runFeatureExtraction(calphadistmat, numOfCAatom);
        } catch (Exception e) {
        	logger.fatal(e.getMessage(),e);
            return "";
        }
    }
//
//    public void printCalphamatImage(double [][]calphadistmat,int numOfCAatom){
//    
//        try {
//    
//            File camatDir=new File(camatDirName);
//          BufferedImage im = new BufferedImage(numOfCAatom,numOfCAatom,BufferedImage.TYPE_BYTE_GRAY);
//         WritableRaster raster = im.getRaster();
//       // Put the pixels on the raster, using values between 0 and 255.
//       for(int h=0;h<numOfCAatom;h++)
//         for(int w=0;w<numOfCAatom;w++)
//           {
//           raster.setSample(w,h,0,calphadistmat[h][w]); 
//           }
//       // Store the image using the PNG format.
//String fileName=System.currentTimeMillis()+".png";
//       ImageIO.write(im,"PNG",new File(camatDir,fileName));
//        } catch (Exception e) {
//        }
//       
//    
//    }
//   
//    
    public String runFeatureExtraction(double calphaImage[][], int numOfCAatom) {
        int newdimx = numOfCAatom, newdimy = numOfCAatom;
        double[][] resizedImage = calphaImage;
        double[][] imGradX = ImFilter.runImDx(resizedImage);
        double[][] imGradY = ImFilter.runImDy(resizedImage);
        double[][] imGradMag = new double[newdimx][newdimy];
        double[][] imGradAngle = new double[newdimx][newdimy];
        double maxMagnit = 0;
        double minMagnit = 0;
        for (int rowNo = 0; rowNo < newdimx; rowNo++) {
            for (int colNo = 0; colNo < newdimy; colNo++) {
                imGradMag[rowNo][colNo] = Math.sqrt(Math.pow(imGradX[rowNo][colNo], 2) + Math.pow(imGradY[rowNo][colNo], 2));
                maxMagnit = Math.max(maxMagnit, imGradMag[rowNo][colNo]);
                minMagnit = Math.min(minMagnit, imGradMag[rowNo][colNo]);
                double theta = Math.atan2(imGradX[rowNo][colNo], imGradY[rowNo][colNo]) * 180 / Math.PI;
                imGradAngle[rowNo][colNo] = theta;
            }
        }
///////compute comog+phog here/////
        double comogThreshold = minMagnit + (maxMagnit - minMagnit) * 0.001;
        //comogThreshold=0;
        double[][] comog = computeComog(imGradMag, imGradAngle, 16, comogThreshold);
        double[] phog = computePhog(resizedImage);
        String comogphog = "";
        for (double[] comog1 : comog) {
            for (int j = 0; j < comog1.length; j++) {
                comogphog = comogphog + (int)comog1[j] + "-";
            }
        }
        
        double phogsum=0;
        for (int i = 0; i < phog.length; i++) {
            phogsum+= phog[i];
        }
        for (int i = 0; i < phog.length; i++) {
            comogphog = comogphog + (int)((phog[i]/phogsum)*1000000) + "-";
        }
        comogphog = comogphog.substring(0, comogphog.length() - 2);
        return comogphog;
    }

    private double[] computePhog(double[][] inputMat) {

        if(inputMat.length<=1)return null;
        
        int bin = 16;
        double angle = 360;
        int L = 3;
        int dimx=inputMat.length;
        int dimy=inputMat[0].length;
        BufferedImage im=new BufferedImage(dimx, dimy, BufferedImage.TYPE_BYTE_GRAY);
          WritableRaster raster = im.getRaster();
        for(int y = 0; y<dimx; y++){
         for(int x = 0; x<dimy; x++){
              raster.setSample(x,y,0,(int)inputMat[x][y]); 
         }
}
    
//        SavaeBufferedImageToDiskFile.save(im, null, null);
        
        
        
        double[] p=null;
        try{
                 PHOG phog=new PHOG(3);
phog.extract(im);
//return phg.getByteArrayRepresentation();
    
//        p = new double[85 * 9];
        
        p=phog.getDoubleHistogram();
 
        }catch(Exception e){logger.fatal(e.getMessage(),e);
        }
        
        return p;
    }

    private double[][] computeComog(double[][] imGradMag, double[][] imGradAngle, int binCount, double threshold) {
        double[][] comog = new double[binCount][binCount];
        for (double[] comog1 : comog) {
            for (int colNo = 0; colNo < comog1.length; colNo++) {
                comog1[colNo] = 0;
            }
        }
        double[][] angles = new double[imGradAngle.length][imGradAngle[0].length];
        for (int rowNo = 0; rowNo < imGradAngle.length; rowNo++) {
            System.arraycopy(imGradAngle[rowNo], 0, angles[rowNo], 0, imGradAngle[rowNo].length);
        }
        double anglePrecision = (double) 360.0 / binCount;
        for (int rowNo = 0; rowNo < imGradAngle.length; rowNo++) {
            for (int colNo = 0; colNo < imGradAngle[rowNo].length; colNo++) {
                if (angles[rowNo][colNo] < -(anglePrecision / 2)) {
                    angles[rowNo][colNo] = angles[rowNo][colNo] + 360;
                }
                angles[rowNo][colNo] = Math.round(angles[rowNo][colNo] / anglePrecision);
            }
        }
        double min = 0, max = 0;
        for (double[] angle : angles) {
            for (int j = 0; j < angle.length; j++) {
                max = Math.max(max, angle[j]);
                min = Math.min(min, angle[j]);
            }
        }
        for (int rowNo = 0; rowNo < angles.length; rowNo++) {
            for (int colNo = 0; colNo < angles[rowNo].length; colNo++) {
                if (imGradMag[rowNo][colNo] > threshold) {
                    int x = (int) Math.round(angles[rowNo][colNo]);
                    for (int i = rowNo - 1; i <= rowNo + 1; i++) {
                        for (int j = colNo - 1; j <= colNo + 1; j++) {
                            if (i >= 0 && j >= 0 && i < angles.length && j < angles[i].length && (imGradMag[i][j] > threshold)) {
                                int y = (int) Math.round(angles[i][j]);
                                comog[x][y]++;
//                                comog[y][x]++;
                            }
                        }
                    }
                }
            }
        }
        double comogsum = 0;
        for (double[] comog1 : comog) {
            for (int j = 0; j < comog1.length; j++) {
                comogsum += comog1[j];
            }
        }
        for (double[] comog1 : comog) {
            for (int j = 0; j < comog1.length; j++) {
                comog1[j] = Math.round((comog1[j] * 1000000) / comogsum);
            }
        }
        return comog;
    }


    private static class ImFilter {
        public static double[][] runImDx(double[][] input) {
            int dimx = input.length;
            int dimy = input[0].length;
            double[][] outputIm = new double[input.length][input[0].length];
            //           double[] filterDx = {-1, 0, 1};
            for (int rowNo = 0; rowNo < input.length; rowNo++) {
                outputIm[rowNo][0] = input[rowNo][1];
                outputIm[rowNo][dimy - 1] = -input[rowNo][dimy - 2];
            }
            for (int rowNo = 0; rowNo < dimx; rowNo++) {
                for (int colNo = 1; colNo < dimy - 1; colNo++) {
                    outputIm[rowNo][colNo] = input[rowNo][colNo + 1] - input[rowNo][colNo - 1];
                }
            }
            return outputIm;
        }

        static double[][] runImDy(double[][] input) {
            int dimx = input.length;
            int dimy = input[0].length;
            double[][] outputIm = new double[input.length][input[0].length];
            for (int colNo = 0; colNo < dimy; colNo++) {
                outputIm[0][colNo] = input[1][colNo];
                outputIm[dimx - 1][colNo] = -input[dimx - 2][colNo];
            }
            for (int rowNo = 1; rowNo < dimx - 1; rowNo++) {
                for (int colNo = 0; colNo < dimy; colNo++) {
                    outputIm[rowNo][colNo] = input[rowNo + 1][colNo] - input[rowNo - 1][colNo];
                }
            }
            return outputIm;
        }
    }

   
        static void print(double mat[][]) {
            for (double[] mat1 : mat) {
                for (int j = 0; j < mat1.length; j++) {
                    System.out.print(" " + mat1[j]);
                }
                System.out.println("");
            }
            System.out.println("");
        }

   }
