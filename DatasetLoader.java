import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatasetLoader {

    public static List<File> loadImages(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        List<File> images = new ArrayList<>();

        if (files != null) {
            for (File f : files) {
                if (f.isFile() && isImage(f)) {
                    images.add(f);
                }
            }
        }
        return images;
    }

    private static boolean isImage(File f) {
        String name = f.getName().toLowerCase();
        return name.endsWith(".jpg") ||
               name.endsWith(".jpeg") ||
               name.endsWith(".png");
    }
}

