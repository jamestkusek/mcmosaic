package com.personal.james;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Texture {

    private final BufferedImage img;
    private final String name;
    int R;
    int G;
    int B;

    public Texture(BufferedImage img, String name) {
        this.img = img;
        this.name = name;
        Color avg = Application.findAvgBlockColor(img,0,0,16,16);
        this.R = avg.getRed();
        this.G = avg.getGreen();
        this.B = avg.getBlue();
    }

    public BufferedImage getImg() {
        return img;
    }


    public int getR() {
        return R;
    }

    public int getG() {
        return G;
    }

    public int getB() {
        return B;
    }
}
