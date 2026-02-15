import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Imageloader {
    public static BufferedImage loadImage(String path) throws Exception {
        return ImageIO.read(new File(path));
    }

    public static void main(String[] args) throws Exception {
        BufferedImage img = loadImage("test.jpg");
        System.out.println("画像サイズ: " + img.getWidth() + " x " + img.getHeight());
    }
}
