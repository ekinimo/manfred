package manfred.manfreditor;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageLoader {
    public Image load(String path) throws FileNotFoundException {
        return new Image(new FileInputStream(path));
    }
}
