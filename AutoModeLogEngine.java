import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class AutoModeLogEngine {

    private List<Float> shapeScores = new ArrayList<>();
    private List<Float> colorScores = new ArrayList<>();

    private OnnxPredictor shapePredictor;
    private OnnxPredictorColor colorPredictor;

    public AutoModeLogEngine(String shapeModel, String colorModel) throws Exception {
        shapePredictor = new OnnxPredictor(shapeModel);
        colorPredictor = new OnnxPredictorColor(colorModel);
    }

    // ★ データセットを自動で読み込み、推論してスコアを蓄積
    public void processDataset(String folderPath) throws Exception {
        List<File> images = DatasetLoader.loadImages(folderPath);

        for (File imgFile : images) {
            BufferedImage img = ImageIO.read(imgFile);

            // 形モデル前処理
            float[] shapeInput = ShapePreprocessor.preprocess(img);
            float[] shapeLogits = shapePredictor.predict(shapeInput);

            // ★ softmax に変換してから蓄積
            float[] shapeProbs = softmax(shapeLogits);
            shapeScores.add(shapeProbs[0]);

            // 色モデル前処理
            float[] colorInput = ColorPreprocessor.preprocess(img);
            float[] colorLogits = colorPredictor.predict(colorInput);

            // ★ softmax に変換してから蓄積
            float[] colorProbs = softmax(colorLogits);
            colorScores.add(colorProbs[0]);
        }
    }

    // ★ softmax 関数
    private float[] softmax(float[] logits) {
        float max = Float.NEGATIVE_INFINITY;
        for (float v : logits) {
            if (v > max) max = v;
        }

        float sum = 0f;
        float[] exps = new float[logits.length];
        for (int i = 0; i < logits.length; i++) {
            exps[i] = (float)Math.exp(logits[i] - max);
            sum += exps[i];
        }

        for (int i = 0; i < exps.length; i++) {
            exps[i] /= sum;
        }

        return exps;
    }

    // ★ 1枚だけ推論するメソッド（正しい位置）
    public double predictSingleImage(BufferedImage img, double shapeExp, double colorExp) throws Exception {

        // 形モデル
        float[] shapeInput = ShapePreprocessor.preprocess(img);
        float[] shapeLogits = shapePredictor.predict(shapeInput);
        float[] shapeProbs = softmax(shapeLogits);
        double shapeWeighted = Math.pow(shapeProbs[0], shapeExp);

        // 色モデル
        float[] colorInput = ColorPreprocessor.preprocess(img);
        float[] colorLogits = colorPredictor.predict(colorInput);
        float[] colorProbs = softmax(colorLogits);
        double colorWeighted = Math.pow(colorProbs[0], colorExp);

        // 融合
        return shapeWeighted * colorWeighted;
    }

    public float getShapeMode() {
        return ModeLogCalculator.calculateMode(shapeScores);
    }

    public float getColorMode() {
        return ModeLogCalculator.calculateMode(colorScores);
    }

    public double getShapeLog(float x) {
        return ModeLogCalculator.calculateLogFromScores(shapeScores, x);
    }

    public double getColorLog(float x) {
        return ModeLogCalculator.calculateLogFromScores(colorScores, x);
    }

    public double getFusion(float x) {
        return getShapeLog(x) * getColorLog(x);
    }

    public double getShapeWeighted(double x, double shapeExp) {
        double shapeLog = getShapeLog((float)x);
        return Math.pow(shapeLog, shapeExp);
    }

    public double getColorWeighted(double x, double colorExp) {
        double colorLog = getColorLog((float)x);
        return Math.pow(colorLog, colorExp);
    }

    public double getFusionWeighted(double x, double shapeExp, double colorExp) {
        double s = getShapeWeighted(x, shapeExp);
        double c = getColorWeighted(x, colorExp);
        return s * c;
    }

    public double getShapeExponent() {
        return getShapeMode();   // 形の指数 = 形の最頻値
    }

    public double getColorExponent() {
        return getColorMode();   // 色の指数 = 色の最頻値
    }
}



