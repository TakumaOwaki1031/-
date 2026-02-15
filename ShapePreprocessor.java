import java.awt.image.BufferedImage;

public class ShapePreprocessor {

    public static float[] preprocess(BufferedImage img) {

        // 1. グレースケール
        BufferedImage gray = GrayScaler.toGray(img);

        // 2. ヒストグラム平均化
        BufferedImage eq = HistogramEqualizer.equalize(gray);

        // 3. リサイズ
        BufferedImage resized = ResizeImage.resize(eq, 128, 128);

        // 4. float[] へ変換（C,H,W）
        float[] input = ImageToFloatArray.toFloatArray(resized);

        return input;
    }
}

