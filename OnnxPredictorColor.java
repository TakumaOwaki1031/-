import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import java.nio.FloatBuffer;
import java.util.Collections;

public class OnnxPredictorColor {
    private OrtEnvironment env = OrtEnvironment.getEnvironment();
    private OrtSession session;

    public OnnxPredictorColor(String modelPath) throws Exception {
        this.session = env.createSession(modelPath, new OrtSession.SessionOptions());
    }

    public float[] predict(float[] inputData) throws Exception {

        // ★ 色モデルは 3ch
        long[] shape = new long[]{1L, 3L, 128L, 128L};

        FloatBuffer fb = FloatBuffer.wrap(inputData);

        OnnxTensor inputTensor = OnnxTensor.createTensor(env, fb, shape);

        String inputName = session.getInputNames().iterator().next();

        OrtSession.Result result = session.run(Collections.singletonMap(inputName, inputTensor));

        // ★ 出力は (1,2) のはず → float[1][2]
        float[][] output = (float[][]) result.get(0).getValue();

        return output[0];  // [クラス0, クラス1]
    }
}

