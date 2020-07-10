package manfred.manfreditor.map;

import javafx.scene.image.Image;

public class MapObject {
    private final String name;
    private final int blocksWidth;
    private final boolean[][] mapStructure;
    private final Image image;

    public MapObject(String name, int blocksWidth, boolean[][] mapStructure, Image image) {
        this.name = name;
        this.blocksWidth = blocksWidth;
        this.mapStructure = mapStructure;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
