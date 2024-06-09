
import com.personal.james.AppGUI;
import com.personal.james.Application;
import com.personal.james.Texture;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {



    @Test
    public void findClosestTextureToTest() throws IOException {
        Texture red = new Texture(Application.loadImage(new File("external-resources/test-textures/red.png")),"redTest");
        Texture blue = new Texture(Application.loadImage(new File("external-resources/test-textures/blue.png")),"blueTest");
        Texture redBlue = new Texture(Application.loadImage(new File("external-resources/test-textures/redblue.png")),"redBlueTest");

        List<Texture> testList  = new ArrayList<>();
        testList.add(red);testList.add(blue);testList.add(redBlue);
        assertEquals(Application.findClosestTextureTo(Color.RED,testList),red);
        assertEquals(Application.findClosestTextureTo(Color.BLUE,testList),blue);
        assertEquals(Application.findClosestTextureTo(Color.MAGENTA,testList),redBlue);

    }

    @Test
    public void findAvgBlockColorTest() {
        BufferedImage test1 = Application.loadImage(new File("external-resources/test-textures/redblue.png"));
        assertEquals(Application.findAvgBlockColor(test1,0,0,16,16),new Color(127,0,127));


    }

    @Test
    public void transformTest() {
        BufferedImage testImage = Application.loadImage(new File("external-resources/test-textures/test-image.png/"));
        BufferedImage resultImage = Application.transformImage(testImage);

        BufferedImage expectedImage = Application.loadImage(new File("external-resources/test-textures/test-image-mosaic.png"));

        assertArrayEquals(returnPixelData(expectedImage),returnPixelData(resultImage));



    }

    public byte[] returnPixelData(BufferedImage inputImg) {
        return ((DataBufferByte) inputImg.getRaster().getDataBuffer()).getData();
    }
}



