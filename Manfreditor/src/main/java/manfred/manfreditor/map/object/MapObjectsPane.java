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

    private MapObjectContainer selectedObject = null;

    public void addMapObject(MapObject mapObject, Controller controller) {
        if (nextFreeTileY + 1 > objectContainers.size()) {
            objectContainers.add(nextFreeTileY, new MapObjectContainer[NUMBER_OF_COLUMNS]);
        }

        MapObjectContainer mapObjectContainer = new MapObjectContainer(mapObject, controller);
        objectContainers.get(nextFreeTileY)[nextFreeTileX] = mapObjectContainer;

        this.addImage(mapObject.getImage());
        this.add(mapObjectContainer.getRectangle(), nextFreeTileX, nextFreeTileY);

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

    public void select(int x, int y) {
        if (selectedObject != null) {
            selectedObject.deselect();
        }

        MapObjectContainer newSelection = objectContainers.get(y)[x];
        if (newSelection.equals(selectedObject)) {
            selectedObject = null;
            return;
        }

        selectedObject = newSelection;
        selectedObject.select();
    }
}
