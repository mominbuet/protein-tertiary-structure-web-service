package featureExtraction;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import org.apache.log4j.Logger;

import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.utils.ImageUtils;
import net.semanticmetadata.lire.utils.MetricsUtils;
import net.semanticmetadata.lire.utils.SerializationUtils;

public class PHOG
  implements LireFeature
{
	static Logger logger = Logger.getLogger(PHOG.class);

  static ConvolveOp sobelX = new ConvolveOp(new Kernel(3, 3, new float[] { 1.0F, 0.0F, -1.0F, 2.0F, 0.0F, -2.0F, 1.0F, 0.0F, -1.0F }));
  static ConvolveOp sobelY = new ConvolveOp(new Kernel(3, 3, new float[] { 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.0F, -2.0F, -1.0F }));
  static ConvolveOp gaussian = new ConvolveOp(new Kernel(5, 5, ImageUtils.makeGaussianKernel(5, 1.4F)));
  static ColorConvertOp grayscale = new ColorConvertOp(ColorSpace.getInstance(1003), null);
  int[] tmp255 = { 255 };
  int[] tmp128 = { 128 };
  int[] tmp000 = { 0 };
  int[] tmpPixel = { 0 };
  double thresholdLow = 60.0D;
  double thresholdHigh = 100.0D;
  public static int bins = 9;
  double[] histogram;
  int L=3;
  
  public PHOG(int Levels) {
	  L=Levels;}

public  PHOG(){
	  
  }
  
  
  public void extract(BufferedImage paramBufferedImage)
  {
    BufferedImage localBufferedImage = grayscale.filter(paramBufferedImage, null);
    double[][] arrayOfDouble1 = sobelFilterX(localBufferedImage);
    double[][] arrayOfDouble2 = sobelFilterY(localBufferedImage);
    int i = localBufferedImage.getWidth();
    int j = localBufferedImage.getHeight();
    double[][] arrayOfDouble3 = new double[i][j];
    double[][] arrayOfDouble4 = new double[i][j];
      
    for (int k = 0; k < i; k++) {
      for (int m = 0; m < j; m++)
      {
        if (arrayOfDouble1[k][m] != 0.0D) {
          arrayOfDouble3[k][m] = Math.atan(arrayOfDouble2[k][m] / arrayOfDouble1[k][m]);
        } else {
          arrayOfDouble3[k][m] = 1.570796326794897D;
        }
        arrayOfDouble4[k][m] = Math.hypot(arrayOfDouble2[k][m], arrayOfDouble1[k][m]);
      }
    }
      
    for (int k = 0; k < i; k++)
    {
      localBufferedImage.getRaster().setPixel(k, 0, new int[] { 255 });
      localBufferedImage.getRaster().setPixel(k, j - 1, new int[] { 255 });
    }
    for (int k = 0; k < j; k++)
    {
      localBufferedImage.getRaster().setPixel(0, k, new int[] { 255 });
      localBufferedImage.getRaster().setPixel(i - 1, k, new int[] { 255 });
    }
    for (int k = 1; k < i - 1; k++) {
      for (int m = 1; m < j - 1; m++) {
        if ((arrayOfDouble3[k][m] < 0.3926990816987241D) && (arrayOfDouble3[k][m] >= -0.3926990816987241D))
        {
          if ((arrayOfDouble4[k][m] > arrayOfDouble4[(k + 1)][m]) && (arrayOfDouble4[k][m] > arrayOfDouble4[(k - 1)][m])) {
            setPixel(k, m, localBufferedImage, arrayOfDouble4[k][m]);
          } else {
            localBufferedImage.getRaster().setPixel(k, m, this.tmp255);
          }
        }
        else if ((arrayOfDouble3[k][m] < 1.178097245096172D) && (arrayOfDouble3[k][m] >= 0.3926990816987241D))
        {
          if ((arrayOfDouble4[k][m] > arrayOfDouble4[(k - 1)][(m - 1)]) && (arrayOfDouble4[k][m] > arrayOfDouble4[(k - 1)][(m - 1)])) {
            setPixel(k, m, localBufferedImage, arrayOfDouble4[k][m]);
          } else {
            localBufferedImage.getRaster().setPixel(k, m, this.tmp255);
          }
        }
        else if ((arrayOfDouble3[k][m] < -1.178097245096172D) || (arrayOfDouble3[k][m] >= 1.178097245096172D))
        {
          if ((arrayOfDouble4[k][m] > arrayOfDouble4[k][(m + 1)]) && (arrayOfDouble4[k][m] > arrayOfDouble4[k][(m + 1)])) {
            setPixel(k, m, localBufferedImage, arrayOfDouble4[k][m]);
          } else {
            localBufferedImage.getRaster().setPixel(k, m, this.tmp255);
          }
        }
        else if ((arrayOfDouble3[k][m] < -0.3926990816987241D) && (arrayOfDouble3[k][m] >= -1.178097245096172D))
        {
          if ((arrayOfDouble4[k][m] > arrayOfDouble4[(k + 1)][(m - 1)]) && (arrayOfDouble4[k][m] > arrayOfDouble4[(k - 1)][(m + 1)])) {
            setPixel(k, m, localBufferedImage, arrayOfDouble4[k][m]);
          } else {
            localBufferedImage.getRaster().setPixel(k, m, this.tmp255);
          }
        }
        else {
          localBufferedImage.getRaster().setPixel(k, m, this.tmp255);
        }
      }
    }
    int[] arrayOfInt = { 0 };
    int n;
    
//    for (int m = 1; m < i - 1; m++) {
//      for (n = 1; n < j - 1; n++) {
//        if (localBufferedImage.getRaster().getPixel(m, n, arrayOfInt)[0] < 50) {
//          trackWeakOnes(m, n, localBufferedImage);
//        }
//      }
//    }
    
    for (int m = 2; m < i - 2; m++) {
      for (n = 2; n < j - 2; n++) {
        if (localBufferedImage.getRaster().getPixel(m, n, arrayOfInt)[0] > 50) {
          localBufferedImage.getRaster().setPixel(m, n, this.tmp255);
        }
      }
    }
    
    /**
     * modified below
     */
    int numOfNodes=0;
    for(int level=0;level<=L;level++){
    numOfNodes+=(int)Math.round(Math.pow(4, level));
    }
    	this.histogram = new double[numOfNodes * bins];
    int index=0;
    for(int level=0;level<=L;level++){
    	int parts=(int)Math.round(Math.pow(2, level));
    	int partLen=i/parts;
    	for(int partNox=1;partNox<=parts;partNox++){
    		int leftx=(partNox-1)*partLen;
        	int rightx=partNox*partLen;
        	for(int partNoy=1;partNoy<=parts;partNoy++){
    	    		
    	int lefty=(partNoy-1)*partLen;
    	int righty=partNoy*partLen;
    	int dx=partLen;
    	int dy=partLen;
    	if(leftx+dx>=i)dx=i-leftx;
    	if(lefty+dy>=j)dx=j-lefty;
    	
    	   System.arraycopy(getHistogram(leftx, lefty, dx, dy, localBufferedImage, arrayOfDouble3), 0, this.histogram, index * bins, bins);
    index++;
    		}
    	}
    }
    
//    //L=0
//    System.arraycopy(getHistogram(0, 0, i, j, localBufferedImage, arrayOfDouble3), 0, this.histogram, 0, bins);
//  //L=1
//    System.arraycopy(getHistogram(0, 0, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, bins, bins);
//    System.arraycopy(getHistogram(i / 2, 0, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 2 * bins, bins);
//    System.arraycopy(getHistogram(0, j / 2, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 3 * bins, bins);
//    System.arraycopy(getHistogram(i / 2, j / 2, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 4 * bins, bins);
//  //L=2
//    System.arraycopy(getHistogram(0, 0, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, bins, bins);
//    System.arraycopy(getHistogram(i / 2, 0, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 2 * bins, bins);
//    System.arraycopy(getHistogram(0, j / 2, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 3 * bins, bins);
//    System.arraycopy(getHistogram(i / 2, j / 2, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 4 * bins, bins);
//  
//    System.arraycopy(getHistogram(0, 0, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, bins, bins);
//    System.arraycopy(getHistogram(i / 2, 0, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 2 * bins, bins);
//    System.arraycopy(getHistogram(0, j / 2, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 3 * bins, bins);
//    System.arraycopy(getHistogram(i / 2, j / 2, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 4 * bins, bins);
//  
//    System.arraycopy(getHistogram(0, 0, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, bins, bins);
//    System.arraycopy(getHistogram(i / 2, 0, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 2 * bins, bins);
//    System.arraycopy(getHistogram(0, j / 2, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 3 * bins, bins);
//    System.arraycopy(getHistogram(i / 2, j / 2, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 4 * bins, bins);
//  
//    System.arraycopy(getHistogram(0, 0, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, bins, bins);
//    System.arraycopy(getHistogram(i / 2, 0, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 2 * bins, bins);
//    System.arraycopy(getHistogram(0, j / 2, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 3 * bins, bins);
//    System.arraycopy(getHistogram(i / 2, j / 2, i / 2, j / 2, localBufferedImage, arrayOfDouble3), 0, this.histogram, 4 * bins, bins);
//  
//    
    
  
  
  }
  
  private double[] getHistogram(int paramInt1, int paramInt2, int paramInt3, int paramInt4, BufferedImage paramBufferedImage, double[][] paramArrayOfDouble)
  {
    int[] arrayOfInt = { 0 };
    double[] arrayOfDouble = new double[bins];
    double d1 ;
    for (int j = 0; j < arrayOfDouble.length; j++) {
      arrayOfDouble[j] = 0.0D;
    }
    for (int j = paramInt1; j < paramInt1 + paramInt3; j++) {
      for (int k = paramInt2; k < paramInt2 + paramInt4; k++) {
        if (paramBufferedImage.getRaster().getPixel(j, k, arrayOfInt)[0] < 50)
        {
          d1 = (paramArrayOfDouble[j][k] / 3.141592653589793D + 0.5D) * bins;
          int i;
          if (d1 == Math.floor(d1))
          {
            i = (int)Math.floor(d1);
            if (i == bins) {
              i = 0;
            }
            arrayOfDouble[i] += 1.0D;
          }
          else
          {
            i = (int)Math.floor(d1);
            if (i == bins) {
              i = 0;
            }
            arrayOfDouble[i] += d1 - Math.floor(d1);
            i = (int)Math.ceil(d1);
            if (i == bins) {
              i = 0;
            }
            arrayOfDouble[i] += Math.ceil(d1) - d1;
          }
        }
      }
    }
    double d2 = 0.0D;
    for (int m = 0; m < arrayOfDouble.length; m++) {
      d2 = Math.max(arrayOfDouble[m], d2);
    }
    if (d2 > 0.0D) {
      for (int m = 0; m < arrayOfDouble.length; m++) {
        arrayOfDouble[m] = Math.round(64.0D * arrayOfDouble[m] / d2);
      }
    }
    return arrayOfDouble;
  }
  
  private int quantAngle(double paramDouble)
  {
    int i = (int)Math.round((paramDouble / 3.141592653589793D + 0.5D) * bins);
    if (i == bins) {
      i = 0;
    }
    return i;
  }
  
  private void trackWeakOnes(int paramInt1, int paramInt2, BufferedImage paramBufferedImage)
  {
	  try {
		
    for (int i = paramInt1 - 1; i <= paramInt1 + 1; i++) {
      for (int j = paramInt2 - 1; j <= paramInt2 + 1; j++) {
        if (isWeak(i, j, paramBufferedImage))
        {
          paramBufferedImage.getRaster().setPixel(i, j, this.tmp000);
          trackWeakOnes(i, j, paramBufferedImage);
        }
      }
    }
		} catch (Exception e) {
logger.fatal(e.getMessage(),e);
		}

	  }
  
  private boolean isWeak(int paramInt1, int paramInt2, BufferedImage paramBufferedImage)
  {
	  try {
		  return (paramBufferedImage.getRaster().getPixel(paramInt1, paramInt2, this.tmpPixel)[0] > 0) && (paramBufferedImage.getRaster().getPixel(paramInt1, paramInt2, this.tmpPixel)[0] < 255);

	} catch (Exception ex) {
		logger.fatal(ex.getMessage(),ex);
	}
	  return false;
    }
  
  private void setPixel(int paramInt1, int paramInt2, BufferedImage paramBufferedImage, double paramDouble)
  {
    if (paramDouble > this.thresholdHigh) {
      paramBufferedImage.getRaster().setPixel(paramInt1, paramInt2, this.tmp000);
    } else if (paramDouble > this.thresholdLow) {
      paramBufferedImage.getRaster().setPixel(paramInt1, paramInt2, this.tmp128);
    } else {
      paramBufferedImage.getRaster().setPixel(paramInt1, paramInt2, this.tmp255);
    }
  }
  
  private double[][] sobelFilterX(BufferedImage paramBufferedImage)
  {
    double[][] arrayOfDouble = new double[paramBufferedImage.getWidth()][paramBufferedImage.getHeight()];
    int[] arrayOfInt = new int[1];
    int i ;
    for (int j = 1; j < paramBufferedImage.getWidth() - 1; j++) {
      for (int k = 1; k < paramBufferedImage.getHeight() - 1; k++)
      {
        i = 0;
        i += paramBufferedImage.getRaster().getPixel(j - 1, k - 1, arrayOfInt)[0];
        i += 2 * paramBufferedImage.getRaster().getPixel(j - 1, k, arrayOfInt)[0];
        i += paramBufferedImage.getRaster().getPixel(j - 1, k + 1, arrayOfInt)[0];
        i -= paramBufferedImage.getRaster().getPixel(j + 1, k - 1, arrayOfInt)[0];
        i -= 2 * paramBufferedImage.getRaster().getPixel(j + 1, k, arrayOfInt)[0];
        i -= paramBufferedImage.getRaster().getPixel(j + 1, k + 1, arrayOfInt)[0];
        arrayOfDouble[j][k] = i;
      }
    }
    for (int j = 0; j < paramBufferedImage.getWidth(); j++)
    {
      arrayOfDouble[j][0] = 0.0D;
      arrayOfDouble[j][(paramBufferedImage.getHeight() - 1)] = 0.0D;
    }
    for (int j = 0; j < paramBufferedImage.getHeight(); j++)
    {
      arrayOfDouble[0][j] = 0.0D;
      arrayOfDouble[(paramBufferedImage.getWidth() - 1)][j] = 0.0D;
    }
    return arrayOfDouble;
  }
  
  private double[][] sobelFilterY(BufferedImage paramBufferedImage)
  {
    double[][] arrayOfDouble = new double[paramBufferedImage.getWidth()][paramBufferedImage.getHeight()];
    int[] arrayOfInt = new int[1];
    int i ;
    for (int j = 1; j < paramBufferedImage.getWidth() - 1; j++) {
      for (int k = 1; k < paramBufferedImage.getHeight() - 1; k++)
      {
        i = 0;
        i += paramBufferedImage.getRaster().getPixel(j - 1, k - 1, arrayOfInt)[0];
        i += 2 * paramBufferedImage.getRaster().getPixel(j, k - 1, arrayOfInt)[0];
        i += paramBufferedImage.getRaster().getPixel(j + 1, k - 1, arrayOfInt)[0];
        i -= paramBufferedImage.getRaster().getPixel(j - 1, k + 1, arrayOfInt)[0];
        i -= 2 * paramBufferedImage.getRaster().getPixel(j, k + 1, arrayOfInt)[0];
        i -= paramBufferedImage.getRaster().getPixel(j + 1, k + 1, arrayOfInt)[0];
        arrayOfDouble[j][k] = i;
      }
    }
    for (int j = 0; j < paramBufferedImage.getWidth(); j++)
    {
      arrayOfDouble[j][0] = 0.0D;
      arrayOfDouble[j][(paramBufferedImage.getHeight() - 1)] = 0.0D;
    }
    for (int j = 0; j < paramBufferedImage.getHeight(); j++)
    {
      arrayOfDouble[0][j] = 0.0D;
      arrayOfDouble[(paramBufferedImage.getWidth() - 1)][j] = 0.0D;
    }
    return arrayOfDouble;
  }
  
  public byte[] getByteArrayRepresentation()
  {
    return SerializationUtils.toByteArray(this.histogram);
  }
  
    /**
     *
     * @param paramArrayOfByte
     */
    public void setByteArrayRepresentation(byte[] paramArrayOfByte)
  {
    this.histogram = SerializationUtils.toDoubleArray(paramArrayOfByte);
  }
  
  public void setByteArrayRepresentation(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.histogram = SerializationUtils.toDoubleArray(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public double[] getDoubleHistogram()
  {
    return this.histogram;
  }
  
  public float getDistance(LireFeature paramLireFeature)
  {
    return (float)MetricsUtils.distL1(this.histogram, ((PHOG)paramLireFeature).histogram);
  }
  
  public String getStringRepresentation()
  {
    return null;
  }
  
  public void setStringRepresentation(String paramString) {}
}

/* Location:           F:\Research\bioinformatics\scopdataExperiment\codes in java\ProteinResearch\lib\lire\lire.jar

 * Qualified Name:     net.semanticmetadata.lire.imageanalysis.PHOG

 * JD-Core Version:    0.7.0.1

 */