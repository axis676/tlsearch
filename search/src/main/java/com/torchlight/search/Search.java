package com.torchlight.search;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import net.sourceforge.tess4j.Tesseract;

public class Search {

	static Tesseract tesseract = new Tesseract();
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		tesseract.setDatapath("C:\\Users\\ovov9\\Downloads\\tesseract-5.2.0\\tesseract-5.2.0\\tessdata");
	}
	public static void main(String[] args) {
//	      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
//	      Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
//	      System.out.println( "mat = " + mat.dump() );
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//
//       File imgFile = new File("C:\\Users\\ovov9\\Desktop\\testJPG68.jpg");
//       String dest = "C:/Users/ovov9/Desktop";
//       Mat src = Imgcodecs.imread(imgFile.toString(), Imgcodecs.IMREAD_GRAYSCALE);
//
//       Mat dst = new Mat();
//
//       Imgproc.adaptiveThreshold(src, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 5);
//       Imgcodecs.imwrite(dest + "/AdaptiveThreshold" + imgFile.getName(), dst);
		Search mod = new Search();
		Mat origin = Imgcodecs.imread("C:\\Users\\ovov9\\Desktop\\testJPG68.jpg");
		String result = new Search().extractString(origin);
		System.out.println(result);
		
//		try {
//			mod.splitImage();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
		
		
	}
	
	
	public String extractString(Mat inputMat) {
		String result = "";
		Mat gray = new Mat();
		
		File imgFile = new File("C:\\Users\\ovov9\\Desktop\\testJPG74.jpg");
		String dest = "C:/Users/ovov9/Desktop";
		Mat src = Imgcodecs.imread(imgFile.toString(), Imgcodecs.IMREAD_REDUCED_GRAYSCALE_2);

		Mat dst = new Mat();

		Imgproc.adaptiveThreshold(src, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 5);
		String destPath = dest +"/AdaptiveThreshold" + imgFile.getName();
		Imgcodecs.imwrite(destPath, dst);
		
		File imgThresholdFile = new File(destPath);
		
		try {
			result = tesseract.doOCR(imgThresholdFile);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void splitImage() throws IOException {
        // Setting Chrome as an agent
        System.setProperty("http.agent", "Chrome");

        // reading the original image file
        // File file = new File("https://www.educative.io/api/edpresso/shot/5120209133764608/image/5075298506244096/test.jpg");
        // FileInputStream sourceFile = new FileInputStream(file);
        
        // reading the file from a URL
        URL url = new URL("https://www.educative.io/api/edpresso/shot/5120209133764608/image/5075298506244096/test.jpg");
        InputStream is = url.openStream();
        BufferedImage image = ImageIO.read(is);

        // initalizing rows and columns
        int rows = 4;
        int columns = 4;

        // initializing array to hold subimages
        BufferedImage imgs[] = new BufferedImage[16];

        // Equally dividing original image into subimages
        int subimage_Width = image.getWidth() / columns;
        int subimage_Height = image.getHeight() / rows;
        
        int current_img = 0;
        
        // iterating over rows and columns for each sub-image
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                // Creating sub image
                imgs[current_img] = new BufferedImage(subimage_Width, subimage_Height, image.getType());
                Graphics2D img_creator = imgs[current_img].createGraphics();

                // coordinates of source image
                int src_first_x = subimage_Width * j;
                int src_first_y = subimage_Height * i;

                // coordinates of sub-image
                int dst_corner_x = subimage_Width * j + subimage_Width;
                int dst_corner_y = subimage_Height * i + subimage_Height;
                
                img_creator.drawImage(image, 0, 0, subimage_Width, subimage_Height, src_first_x, src_first_y, dst_corner_x, dst_corner_y, null);
                current_img++;
            }
        }

        //writing sub-images into image files
        for (int i = 0; i < 16; i++)
        {
            File outputFile = new File("img" + i + ".jpg");
            ImageIO.write(imgs[i], "jpg", outputFile);
        }
        System.out.println("Sub-images have been created.");
    }
	
	
}
