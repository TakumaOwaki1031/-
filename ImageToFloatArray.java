import java.awt.image.BufferedImage;

public class ImageToFloatArray {

    // ONNX Runtime に渡すための float[] を作る
    // 出力形状は (1, 1, 128, 128) に対応する一次元配列
    public static float[] toFloatArray(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        float[] data = new float[1 * 1 * width * height];

        int index = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int gray = img.getRGB(x, y) & 0xFF;  // 0〜255
                float normalized = gray / 255.0f;    // 0〜1 に正規化

                data[index++] = normalized;
            }
        }

        return data;
    }
}
