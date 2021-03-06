package manfred.game.map;

import manfred.game.GameConfig;
import manfred.game.controls.KeyControls;
import manfred.game.graphics.PaintableContainerElement;
import manfred.game.interact.Interactable;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.function.Consumer;

public class Map {
    private final String name;
    private final MapTile[][] mapTiles;
    private final GameConfig gameConfig;

    public Map(String name, MapTile[][] mapTiles, GameConfig gameConfig) {
        this.name = name;
        this.mapTiles = mapTiles;
        this.gameConfig = gameConfig;
    }

    // TODO necessary?
    public String getName() {
        return name;
    }

    public MapTile[][] getArray() {
        return mapTiles;
    }

    public boolean isAccessible(int x, int y) {
        return isInBounds(x, y) && mapTiles[x][y].isAccessible();
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < mapTiles.length
                && y >= 0 && y < mapTiles[0].length;
    }

    public void paint(Graphics g, Point offset) {
        g.setColor(Color.RED);

        for (int x = 0; x < mapTiles.length; x++) {
            for (int y = 0; y < mapTiles[0].length; y++) {
                paintTile(g, offset, x, y);
            }
        }
    }

    private void paintTile(Graphics g, Point offset, int x, int y) {
        BufferedImage tileImage = mapTiles[x][y].getImage();
        if (tileImage != null) {
            int imageWidth = gameConfig.getPixelBlockSize() * 2;
            int imageHeight = tileImage.getHeight() * imageWidth / tileImage.getWidth();
            g.drawImage(
                    tileImage,
                    gameConfig.getPixelBlockSize() * x - offset.x,
                    gameConfig.getPixelBlockSize() * (y + 1) - offset.y - imageHeight,
                    imageWidth,
                    imageHeight,
                    null
            );
            return;
        }

        if (mapTiles[x][y] instanceof Interactable) {
            g.setColor(Color.YELLOW);
        }
        if (!isAccessible(x, y)) {
            g.fillRect(
                    gameConfig.getPixelBlockSize() * x - offset.x,
                    gameConfig.getPixelBlockSize() * y - offset.y,
                    gameConfig.getPixelBlockSize(),
                    gameConfig.getPixelBlockSize()
            );
        }
        if (mapTiles[x][y] instanceof Interactable) {
            g.setColor(Color.RED);
        }
    }

    @Nullable
    public Interactable getInteractable(int x, int y) {
        return mapTiles[x][y] instanceof Interactable
                ? (Interactable) mapTiles[x][y]
                : null;
    }

    @Nullable
    public Consumer<KeyControls> stepOn(int x, int y) {
        return mapTiles[x][y].onStep();
    }

    public Stack<PaintableContainerElement> getPaintableContainerElements() {
        Stack<PaintableContainerElement> elements = new Stack<>();

        int numberOfHorizontalTiles = mapTiles.length;
        int numberOfVerticaltiles = mapTiles[0].length;
        for (int x = 0; x < numberOfHorizontalTiles; x++) {
            for (int y = 0; y < numberOfVerticaltiles; y++) {
                elements.push(new PaintableContainerElement(
                        mapTiles[x][y],
                        gameConfig.getPixelBlockSize() * x,
                        gameConfig.getPixelBlockSize() * y
                ));
            }
        }

        return elements;
    }
}
