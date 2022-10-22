package com.torchlight.search;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
	
	static double starX = 0; 
	static double starY = 0; 
	static double amountX = 0; 
	static double amountY = 0; 
	static double priceX = 0; 
	static double priceY = 0; 
	
	static double xOffset = 0;
	static double yOffset = 0;
	public static void main(String[] args) {
		Search mod = new Search();
		//設定價格數字所在位置 須設定五個點位 星位、數量位、價格位、右邊一格星位，下面一格星位
		mod.setStartPoint();
		
		// 建立 Robot、座標 物件
		try {
			Robot ro = new Robot();
//			int page = 0;
//			while(page<=20) {
				// 以位置為中心截圖
				double targetRatio = 33;
				int count = 0;
				for (int i = 1; i <= 5; i++) {
					for (int j = 1; j <= 2; j++) {
						count++;
						File amountImgFile = mod.screenshot("amount" + count, amountX + xOffset * (i - 1),
								amountY + yOffset * (j - 1));
						File priceImgFile = mod.screenshot("price" + count, priceX + xOffset * (i - 1),
								priceY + yOffset * (j - 1));
						try {
							String amountString = mod.extractString(amountImgFile);
							String priceString = mod.extractString(priceImgFile);



							Double amount = Double.valueOf(amountString);
							Double price = Double.valueOf(priceString);
							Double thisRatio = amount / price;
							
							if (thisRatio >= targetRatio) {
								System.out.println("amount" + count + ":" + amountString);
								System.out.println("price" + count + ":" + priceString);
								System.out.println("thisRatio : " + thisRatio);
								int thisStarX = (int)(starX + xOffset * (i - 1));
								int thisStarY = (int)(starY + yOffset * (j - 1));
//								mod.sendString(thisStarX, thisStarY);
								ro.mouseMove(thisStarX,thisStarY);
								ro.mousePress(InputEvent.BUTTON1_MASK);
								ro.delay(100);
								ro.mouseRelease(InputEvent.BUTTON1_MASK);
							}
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				ro.mouseMove(1891,591);
				ro.delay(100);
				ro.mousePress(InputEvent.BUTTON1_MASK);
				ro.delay(100);
				ro.mouseRelease(InputEvent.BUTTON1_MASK);

//			}
//
//			page++;
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//OCR判斷數字
		
		//比值計算與判斷
	}
	
	
	public String extractString(File imgFile) {
		String result = "";
		Mat gray = new Mat();
		
//		File imgFile = new File("C:\\Users\\ovov9\\Desktop\\3.png");
		String dest = imgFile.getParent();
		
		
//		Mat src = Imgcodecs.imread(imgFile.toString(), Imgcodecs.IMREAD_GRAYSCALE);
		Mat src = Imgcodecs.imread(imgFile.toString());
		Mat dst = new Mat();
		Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
		
		String destPath = dest +"/metadata" + imgFile.getName();
		Imgcodecs.imwrite(destPath, dst);
		
//		Mat src2 = Imgcodecs.imread(destPath.toString(), Imgcodecs.IMREAD_GRAYSCALE);
//		Imgproc.adaptiveThreshold(src2, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 5);
//
//		String destPath2 = dest +"/metadata2" + imgFile.getName();
//		Imgcodecs.imwrite(destPath2, dst);
		
		File imgThresholdFile = new File(destPath);
		
		try {
			tesseract.setLanguage("digits");
			result = tesseract.doOCR(imgThresholdFile);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public File screenshot(String name, double x, double y) {
		File f = new File("image/"+name+".jpg");
		try {
			double xoffset = 30;
			double yoffset = 17;
			int x0 = (int)(x-xoffset);
			int y0 = (int)(y-yoffset);
			
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension d = tk.getScreenSize();
			Rectangle rec = new Rectangle(x0,y0,(int)xoffset*2,(int)yoffset*2);
			Robot ro = new Robot();
			BufferedImage img = ro.createScreenCapture(rec);
//			File f = new File("image/"+name+".jpg");// set appropriate path
			ImageIO.write(img, "jpg", f);
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return f;
	}
	
	public void setStartPoint() {
		starX = 218.0;
		starY = 321.0;
		amountX = 418; 
		amountY = 326; 
		priceX = 359; 
		priceY = 545; 
			
		xOffset = 534-218; 
		yOffset = 637-321;
	}
	
//	public void sendString(int x, int y) throws IOException, InterruptedException {
//		Runtime runtime = Runtime.getRuntime();
//	    String ahkPath = "C:\\Program Files\\AutoHotkey\\AutoHotkey.exe";
//	    String scriptPath = "C:\\Users\\ovov9\\Desktop\\ahk\\吃參數按左鍵.ahk";
//	    runtime.exec(new String[] {ahkPath, scriptPath, String.valueOf(x), String.valueOf(y)} );
//	    Thread.currentThread();
//	    Thread.sleep(1000);
//	}
}
