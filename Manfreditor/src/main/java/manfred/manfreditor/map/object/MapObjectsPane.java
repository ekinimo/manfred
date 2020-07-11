package manfred.manfreditor.map.object;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import manfred.manfreditor.Controller;

import java.util.LinkedList;

public class MapObjectsPane extends GridPane {
    private static final int NUMBER_OF_COLUMNS = 3;

    private final LinkedList<MapObjectContainer[]> objectContainers = new LinkedList<>();

    private int nextFreeTileX = 0;
    private int nextFreeTileY = 0;

    public void addMapObject(MapObject mapObject) {
        if (nextFreeTileY + 1 > objectContainers.size()) {
            objectContainers.add(nextFreeTileY, new MapObjectContainer[NUMBER_OF_COLUMNS]);
        }

        MapObjectContainer mapObjectContainer = new MapObjectContainer(mapObject);
        objectContainers.get(nextFreeTileY)[nextFreeTileX] = mapObjectContainer;

        this.add(mapObjectContainer.getRectangle(), nextFreeTileX, nextFreeTileY);
        this.addImage(mapObject.getImage());

        nextFreeTileX++;
        if (nextFreeTileX >= NUMBER_OF_COLUMNS) {
            nextFreeTileX = 0;
            nextFreeTileY++;
        }
    }

    private void addImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        if (image.getHeight() > image.getWidth()) {
            imageView.setFitHeight(Controller.OBJECTS_GRID_SIZE);
        } else {
            imageView.setFitWidth(Controller.OBJECTS_GRID_SIZE);
        }

        this.add(imageView, nextFreeTileX, nextFreeTileY);
        setValignment(imageView, VPos.CENTER);
        setHalignment(imageView, HPos.CENTER);
    }
}
