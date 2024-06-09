package com.personal.james;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.sqrt;

/**
 * the main class of the program that houses the console interface and
 * methods needed to transform image.
 */
public class Application {

    static JFrame gui;

    /**
     * creates a JFrame GUI and users I/O directories if first time running the jar
     * @param args a String array object
     */
    public static void main(String[] args){
        new File("images/input").mkdirs();
        new File("images/output").mkdirs();

        gui = AppGUI.generate();

    }
    public static void transformAllImages() {

        long startTime = System.nanoTime();
        int filesProcessed = 0;

        File inputDirectory = new File("images/input");
        File[] files = inputDirectory.listFiles();

        for (File inputImage : files) {

            List<String> supportedTypes = List.of("png","jpg","wbmp","gif","jpeg","bmp");
            String fileType = inputImage.getName().substring(inputImage.getName().indexOf(".") + 1);

            if (supportedTypes.contains(fileType.toLowerCase())) {
                String cleanedFileName = inputImage.getName().replace("."+fileType,"");
                BufferedImage img = loadImage(inputImage);
                if (img == null) {
                    AppGUI.reportInfo("Error: " + inputImage.getName() + "is not the file type it claims to be, it cannot be processed");
                    continue;
                }
                File outputFile = new File("images/output" + "/" + cleanedFileName + "-mosaic." + fileType);

                try {
                    ImageIO.write(transformImage(img),fileType,outputFile);
                } catch (IOException e) {
                    System.out.println("Error writing transformed image to output folder");
                    e.printStackTrace();
                }

                filesProcessed +=1;
            }
            else {
                AppGUI.reportInfo("Error: " + inputImage.getName() + " is not supported.");
            }

        }
        if (files.length == 0) {
            AppGUI.reportInfo("No files found in the input folder.");
        }
        else {
            AppGUI.reportInfo(filesProcessed + " file(s) transferred in " + ((System.nanoTime() - startTime) / 1000000000.0) + " seconds.");
        }

    }


    /**
     * Takes a BufferedImage and uses multi-threading to transform the image, saving the output to a local folder.
     * @param img a BufferedImage object
     */
    public static BufferedImage transformImage(BufferedImage img) {

        int height = img.getHeight();
        int width = img.getWidth();
        int remainderH = height % 16;
        int remainderW = width % 16;
        int leftPadding = remainderW / 2;
        int rightPadding = remainderW - leftPadding;
        int topPadding = remainderH / 2;
        int bottomPadding = remainderH - topPadding;


        try (ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (int i = 0; i < width / 16; i++) {
                Replace newThread = new Replace(leftPadding + 16 * i, topPadding,16,16, height / 16, img, FillType.ALL_X);
                threadPool.submit(newThread);
            }

            if (remainderW != 0) {
                threadPool.submit(new Replace(0, topPadding,leftPadding,16,height / 16, img, FillType.ALL_X));
                threadPool.submit(new Replace(width - rightPadding, topPadding,rightPadding,16,height / 16, img, FillType.ALL_X));
            }

            if (remainderH != 0) {
                threadPool.submit(new Replace(leftPadding, 0,16,topPadding,width / 16, img, FillType.ALL_Y));
                threadPool.submit(new Replace(leftPadding, height - bottomPadding,16,bottomPadding,width / 16, img, FillType.ALL_Y));
            }

            if (remainderH != 0 && remainderW != 0) {
                threadPool.submit(new Replace(0,0,leftPadding,topPadding,1,img,FillType.CORNER));
                threadPool.submit(new Replace(width-rightPadding,0,rightPadding,topPadding,1,img,FillType.CORNER));
                threadPool.submit(new Replace(0,height-bottomPadding,leftPadding,bottomPadding,1,img,FillType.CORNER));
                threadPool.submit(new Replace(width-rightPadding,height-bottomPadding,rightPadding,topPadding,1,img,FillType.CORNER));
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println("When trying to create a FixedThreadPool, Runtime.getRuntime().availableProcessors() returned an integer <= 0");
            e.printStackTrace();
        }

        return img;
    }
    /**
     * Loads local image files
     * @return a BufferedImage object
     */
    public static BufferedImage loadImage(File file) {
        try {
            return ImageIO.read(file);
        }
        catch (IOException e) {
            System.out.println("Error loading image file " + file.getName() +" at " + file.getAbsolutePath());
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Given a source image and co-ordinate, will return the average Color of a 16x16 area of the image with top left hand
     * corner at the given co-ordinate
     * @param img a BufferedImage object
     * @param startX an int
     * @param startY an int
     * @return a Color object representing the average color of the desired 16x16 area
     */
    public static Color findAvgBlockColor(BufferedImage img, int startX, int startY, int width, int height) {
        int rAvg = 0;
        int gAvg = 0;
        int bAvg = 0;
        for (int row = 0;row<width;row++) {
            for (int column = 0; column < height;column++ ) {
                int pixelColor = img.getRGB(startX+row,startY+column);
                rAvg += (pixelColor & 0xff0000) >> 16;
                gAvg += (pixelColor & 0xff00) >> 8;
                bAvg += pixelColor & 0xff;
            }
        }
        int pixelCount = width * height;

        return new Color(rAvg/pixelCount, gAvg/pixelCount, bAvg/pixelCount);
    }


    /**
     * Given a Color, List of Texture objects, this method will find the Texture who's average color is closest to the given color
     * @param avgColorOfBlock a Color object
     * @param textureImages a List of Texture objects
     * @return Texture object who's average image color is closest to avgColorOfBlock
     */
    public static Texture findClosestTextureTo(Color avgColorOfBlock, List<Texture> textureImages){

        double distance = 1000; //max distance possible between colors is ~441 in RGB color space
        Texture closest = null;

        int avgR = avgColorOfBlock.getRed();
        int avgG = avgColorOfBlock.getGreen();
        int avgB = avgColorOfBlock.getBlue();

        //use euclidean distance formula to find closest matching texture
        for (Texture texture : textureImages) {
            int avgR2 = texture.getR();
            int avgG2 = texture.getG();
            int avgB2 = texture.getB();
            double nextDistance = sqrt(Math.pow(avgR - avgR2, 2) + Math.pow(avgG - avgG2, 2) + Math.pow(avgB - avgB2, 2));
            if (nextDistance < distance) {
                distance = nextDistance;
                closest = texture;
            }
        }
        return closest;
    }


    /**
     * generate a List of Texture objects from a local text file, using a local directory of images
     * @return a List of Texture Objects
     */
    public static List<Texture> loadTextures() {
        List<Texture> textureImages = new ArrayList<>();
        Scanner scanner = null;
        File textureInfo = new File("external-resources/textureInfo.txt");
        try {
            scanner = new Scanner(textureInfo);
        } catch (FileNotFoundException e) {
            System.err.println("Error trying to open textureInfo.txt: FileNotFoundException");
            return null;
        }

        scanner.nextLine();
        scanner.useDelimiter(",");
        while (scanner.hasNextLine()) {
            String name = scanner.next();
            textureImages.add(new Texture(loadImage(new File("external-resources/textures/" + name)),name));
            scanner.nextLine();

        }
        return textureImages;
    }


}
