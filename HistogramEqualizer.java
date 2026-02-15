import java.awt.image.BufferedImage;

public class HistogramEqualizer {

    public static BufferedImage equalize(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();

        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        int[] histogram = new int[256];
        int[] lut = new int[256];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = src.getRGB(x, y) & 0xFF;
                histogram[rgb]++;
            }
        }

        int total = width * height;
        int cumulative = 0;

        for (int i = 0; i < 256; i++) {
            cumulative += histogram[i];
            lut[i] = (int)((cumulative * 255.0) / total);
        }

     
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = src.getRGB(x, y) & 0xFF;
                int newVal = lut[gray];
                int newPixel = (newVal << 16) | (newVal << 8) | newVal;
                dst.setRGB(x, y, newPixel);
            }
        }

        return dst;
    }
}
