import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) throws Exception {

        AutoModeLogEngine engine = new AutoModeLogEngine(
                "shape_model.onnx",
                "color_model.onnx"
        );

        // データセットから指数を決める
        engine.processDataset("dataset/target");

        double shapeExp = engine.getShapeExponent();
        double colorExp = engine.getColorExponent();

        System.out.println("形の指数: " + shapeExp);
        System.out.println("色の指数: " + colorExp);

        // 1枚の画像を推論
        BufferedImage img = ImageIO.read(new File("dataset/target/test.jpg"));

        double result = engine.predictSingleImage(img, shapeExp, colorExp);

        System.out.println("最終融合スコア（1枚）: " + result);
    }
}

