package manfred.manfreditor.map.object;

import javafx.scene.image.Image;

public class MapObject {
    private final String name;
    private final int blocksWidth;
    private final boolean[][] accessibility;
    private final Image image;

    public MapObject(String name, int blocksWidth, boolean[][] accessibility, Image image) {
        this.name = name;
        this.blocksWidth = blocksWidth;
        this.accessibility = accessibility;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getBlocksWidth() {
        return blocksWidth;
    }

    public boolean[][] getAccessibility() {
        return accessibility;
    }

    public Image getImage() {
        return image;
    }
}
