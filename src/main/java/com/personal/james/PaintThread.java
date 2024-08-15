package com.personal.james;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PaintThread implements Runnable {
    private final int xStart;
    private final int yStart;
    private final int blockWidth;
    private final int blockHeight;
    private final int iterations;
    private final FillType fillType;
    private static final List<Texture> textureImages = Application.loadTextures();
    private final BufferedImage img;

    public PaintThread(int xStart, int yStart, int blockWidth, int blockHeight, int iterations, BufferedImage img, FillType fillType) {
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
