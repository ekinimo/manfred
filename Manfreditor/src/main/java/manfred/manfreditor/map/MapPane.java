package manfred.manfreditor.map;

import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import manfred.manfreditor.Controller;
import manfred.manfreditor.map.object.MapObject;
import manfred.manfreditor.map.object.MapObjectContainer;
import manfred.manfreditor.map.object.MapObjectImageView;

public class MapPane extends GridPane {
    private static final Color NOT_ACCESSIBLE_COLOR = new Color(1, 0, 0, 0.4);
    private static final Color ACCESSIBLE_COLOR = new Color(0, 0, 0, 0.0);
    public static final int PIXEL_BLOCK_SIZE = 50;

    private Rectangle[][] mapTiles;
    private Map map;

    public void resetWithSize(int horizontalSize, int verticalSize) {
        resetChildren();
        setMinWidth(horizontalSize * PIXEL_BLOCK_SIZE);
        setMinHeight(verticalSize * PIXEL_BLOCK_SIZE);
        mapTiles = new Rectangle[horizontalSize][verticalSize];
    }

    private void resetChildren() {
        Node gridLines = getChildren().get(0);
        getChildren().clear();
        getChildren().add(0, gridLines);
    }

    public void addTile(Rectangle rectangle, int x, int y) {
        add(rectangle, x, y);
        mapTiles[x][y] = rectangle;
    }

    public Rectangle getTile(int x, int y) {
        return mapTiles[x][y];
    }

    public void setMap(Map map, Controller controller) {
        this.map = map;

        int horizontalSize = map.getArray().length;
        int verticalSize = map.getArray()[0].length;
        this.resetWithSize(horizontalSize, verticalSize);

        for (int x = 0; x < horizontalSize; x++) {
            for (int y = 0; y < verticalSize; y++) {
                this.addTile(
                    createMapTile(map.getArray()[x][y], controller),
                    x,
                    y
                );
            }
        }
    }

    private Rectangle createMapTile(boolean isAccessible, Controller controller) {
        Rectangle rectangle = new Rectangle(MapPane.PIXEL_BLOCK_SIZE, MapPane.PIXEL_BLOCK_SIZE);
        rectangle.setFill(
            isAccessible
                ? ACCESSIBLE_COLOR
                : NOT_ACCESSIBLE_COLOR
        );
        rectangle.setOnMouseClicked(controller::onMapTileClicked);
        return rectangle;
    }

    public void tileClicked(int x, int y, MapObjectContainer selectedObject) {
        if (selectedObject == null) {
            switchTile(x, y);
        } else {
            addMapObject(x, y, selectedObject.getMapObject());
        }
    }

    private void switchTile(int x, int y) {
        map.getArray()[x][y] = !map.getArray()[x][y];
        this.getTile(x, y).setFill(
            map.getArray()[x][y]
                ? ACCESSIBLE_COLOR
                : NOT_ACCESSIBLE_COLOR
        );
    }

    private void addMapObject(int x, int y, MapObject mapObject) {
        Image image = mapObject.getImage();
        double heightToWidthRatio = image.getHeight() / image.getWidth();
        int columnspan = mapObject.getBlocksWidth();
        int rowspan = (int) (Math.ceil(heightToWidthRatio) * mapObject.getBlocksWidth());

        MapObjectImageView imageView = new MapObjectImageView(image, y);

        int topTileOfImageView;
        double subimageHeight = image.getHeight();
        double subimageWidth = PIXEL_BLOCK_SIZE * mapObject.getBlocksWidth();
        boolean restrictViewPort = false;

        if (imageReachesTooFarUp(y, rowspan)) {
            topTileOfImageView = 0;
            rowspan = y + 1;
            subimageHeight = PIXEL_BLOCK_SIZE * rowspan;
            restrictViewPort = true;
        } else {
            topTileOfImageView = y - rowspan + 1;
        }

        if (imageReachesTooFarRight(x, mapObject)) {
            columnspan = mapTiles.length - x;
            subimageWidth = columnspan * PIXEL_BLOCK_SIZE;
            restrictViewPort = true;
        }

        if (restrictViewPort) {
            imageView.restrictViewPort(subimageWidth, subimageHeight);
        }

        imageView.setFitWidth(PIXEL_BLOCK_SIZE * columnspan);
        this.add(imageView, x, topTileOfImageView, columnspan, rowspan);
        setValignment(imageView, VPos.BOTTOM);

        applyAccessibility(mapObject.getAccessibility(), x, y);
    }

    private boolean imageReachesTooFarRight(int x, MapObject mapObject) {
        return x + mapObject.getBlocksWidth() - 1 >= mapTiles.length;
    }

    private boolean imageReachesTooFarUp(int y, int rowspan) {
        return y - rowspan + 1 < 0;
    }

    private void applyAccessibility(boolean[][] mapStructure, int tileClickedX, int tileClickedY) {
        boolean[][] mapArray = this.map.getArray();

        int yOffset = tileClickedY - mapStructure[0].length + 1;
        for (int x = 0; x < mapStructure.length; x++) {
            if (x + tileClickedX >= mapArray.length) {
                continue;
            }
            for (int y = 0; y < mapStructure[0].length; y++) {
                if (!mapStructure[x][y]) {
                    mapArray[x + tileClickedX][y + yOffset] = false;
                    this.getTile(x + tileClickedX, y + yOffset).setFill(NOT_ACCESSIBLE_COLOR);
                }
            }
        }
    }
}
