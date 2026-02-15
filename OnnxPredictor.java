import ai.onnxruntime.*;
import java.util.*;
import java.nio.FloatBuffer;

public class OnnxPredictor {

    private OrtEnvironment env;
    private OrtSession session;

    public OnnxPredictor(String modelPath) throws Exception {
        env = OrtEnvironment.getEnvironment();
        session = env.createSession(modelPath, new OrtSession.SessionOptions());
    }

    // ★ float[][] → float[] に変換して返すバージョン
    public float[] predict(float[] inputData) throws Exception {

        long[] shape = {1, 1, 128, 128};

        FloatBuffer fb = FloatBuffer.wrap(inputData);

        OnnxTensor inputTensor = OnnxTensor.createTensor(env, fb, shape);

        String inputName = session.getInputNames().iterator().next();

        OrtSession.Result result = session.run(
                Collections.singletonMap(inputName, inputTensor)
        );

        // ★ 出力は float[][] だった
        float[][] output2d = (float[][]) result.get(0).getValue();

        // ★ 1次元にして返す（多くのモデルは [1][N]）
        return output2d[0];
    }
}



