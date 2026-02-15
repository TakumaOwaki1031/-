import java.util.*;

public class ModeLogCalculator {

    // スコアの最頻値を求める（小数2桁に丸めてカテゴリ化）
    public static float calculateMode(List<Float> scores) {
        Map<Float, Integer> countMap = new HashMap<>();

        for (float value : scores) {
            float rounded = Math.round(value * 100f) / 100f; // 小数2桁
            countMap.put(rounded, countMap.getOrDefault(rounded, 0) + 1);
        }

        float mode = 0f;
        int maxCount = 0;

        for (Map.Entry<Float, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mode = entry.getKey();
            }
        }

        return mode;
    }

    // 底＝最頻値で log(x) を計算
    public static double logWithModeBase(float x, float mode) {
        return Math.log(x) / Math.log(mode);
    }

    // スコアリストから最頻値を求め、X の log を返す
    public static double calculateLogFromScores(List<Float> scores, float x) {
        float mode = calculateMode(scores);
        return logWithModeBase(x, mode);
    }
}
