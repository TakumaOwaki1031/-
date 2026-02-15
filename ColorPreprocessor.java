import java.awt.image.BufferedImage;

public class ColorPreprocessor {

    // 画像を HSV → float[] に変換（C,H,W）
    public static float[] preprocess(BufferedImage img) {

        int targetW = 128;
        int targetH = 128;

        // 1. リサイズ
        BufferedImage resized = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
        resized.getGraphics().drawImage(img, 0, 0, targetW, targetH, null);

        float[] output = new float[3 * targetW * targetH];

        int idx = 0;

        for (int y = 0; y < targetH; y++) {
            for (int x = 0; x < targetW; x++) {

                int rgb = resized.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // 2. RGB → HSV（Java 標準ライブラリ）
                float[] hsv = java.awt.Color.RGBtoHSB(r, g, b, null);

                float H = hsv[0]; // 0〜1
                float S = hsv[1]; // 0〜1
                float V = hsv[2]; // 0〜1

                // 3. (C,H,W) の順番で格納
                output[idx++] = H;
                output[idx++] = S;
                output[idx++] = V;
            }
        }

        return output;
    }
}


