package manfred.manfreditor.map.object;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import manfred.manfreditor.Controller;

public class MapObjectContainer {
    private static final Color SELECTED_COLOR = new Color(1, 0, 0, 0.4);
    private static final Color NOT_SELECTED_COLOR = new Color(0, 0, 0, 0.0);

    private final MapObject mapObject;
    private final Rectangle rectangle;

    public MapObjectContainer(MapObject mapObject) {
        this.mapObject = mapObject;
        this.rectangle = new Rectangle(Controller.OBJECTS_GRID_SIZE, Controller.OBJECTS_GRID_SIZE);
        rectangle.setFill(NOT_SELECTED_COLOR);
    }

    public MapObject getMapObject() {
        return mapObject;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void select() {
        rectangle.setFill(SELECTED_COLOR);
    }

    public void deselect() {
        rectangle.setFill(NOT_SELECTED_COLOR);
    }
}
