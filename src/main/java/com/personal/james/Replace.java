package com.personal.james;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Replace implements Runnable {
    private int xStart;
    private int yStart;
    private int blockWidth;
    private int blockHeight;
    private int iterations;
    private FillType fillType;
    private static List<Texture> textureImages = Application.loadTextures();
    private BufferedImage img;

    public Replace (int xStart,int yStart, int blockWidth, int blockHeight, int iterations, BufferedImage img, FillType fillType) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.img = img;
        this.iterations = iterations;
        this.fillType = fillType;
    }


    @Override
    public void run() {
        if (textureImages == null) {
            System.err.println("No list of textures provided to transform the image");
            return;
        }
        long startTime = System.nanoTime();
        switch (fillType) {

            case ALL_X -> {
                for (int i = 0; i < iterations;i++) {
                    Color avg = Application.findAvgBlockColor(img,xStart,yStart + (i*16),blockWidth,blockHeight);
                    Texture closestFound = Application.findClosestTextureTo(avg,textureImages);
                    img.getGraphics().drawImage(closestFound.getImg(), xStart, yStart + (i*16), blockWidth,blockHeight, null);

                }
            }
            case ALL_Y -> {
                for (int i = 0;i < iterations;i++) {
                    Color avg = Application.findAvgBlockColor(img, xStart + (i*16), yStart,blockWidth,blockHeight);
                    Texture closestFound = Application.findClosestTextureTo(avg,textureImages);
                    img.getGraphics().drawImage(closestFound.getImg(), xStart + (i*16), yStart, blockWidth, blockHeight, null);
                }
            }
            case CORNER -> {
                Color avg = Application.findAvgBlockColor(img, xStart, yStart,blockWidth,blockHeight);
                Texture closestFound = Application.findClosestTextureTo(avg,textureImages);
                img.getGraphics().drawImage(closestFound.getImg(), xStart, yStart, blockWidth, blockHeight, null);
            }
        }

    }
}
