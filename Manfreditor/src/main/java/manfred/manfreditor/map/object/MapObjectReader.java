package manfred.manfreditor.map.object;

import manfred.manfreditor.ImageLoader;
import manfred.manfreditor.exception.InvalidInputException;
import manfred.manfreditor.map.MapReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MapObjectReader {
    private static final String PATH_MAP_TILES = MapReader.PATH_MAPS + "tiles\\";
    private static final String ACCESSIBLE = "1";
    private static final String NOT_ACCESSIBLE = "0";

    private final ImageLoader imageLoader;

    public MapObjectReader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public MapObject load(String name) throws InvalidInputException, IOException {
        String jsonMap = read(PATH_MAP_TILES + name + ".json");
        return convert(jsonMap, name);
    }

    String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("", input);
    }

    MapObject convert(String jsonString, String name) throws InvalidInputException, IOException {
        JSONObject jsonMapObject = new JSONObject(jsonString);

        return new MapObject(
            name,
            jsonMapObject.getInt("blocksWidth"),
            convertMapStructure(jsonMapObject.getJSONArray("accessibility")),
            imageLoader.load(PATH_MAP_TILES + name + ".png")
        );
    }

    private boolean[][] convertMapStructure(JSONArray mapStructure) throws InvalidInputException {
        int lengthVertical = mapStructure.length();
        int lengthHorizontal = mapStructure.getJSONArray(0).length();

        boolean[][] transposedMapTiles = new boolean[lengthVertical][lengthHorizontal];
        for (int y = 0; y < lengthVertical; y++) {
            JSONArray horizontalLine = mapStructure.getJSONArray(y);
            if (horizontalLine.length() != lengthHorizontal) {
                throw new InvalidInputException("Map needs to be rectangular. First line: " + lengthHorizontal + ", line: " + y + " " + horizontalLine.length());
            }
            transposedMapTiles[y] = convertHorizontalMapLine(horizontalLine);
        }

        return transposeToGetIntuitiveXAndYRight(transposedMapTiles, lengthVertical, lengthHorizontal);
    }

    private boolean[][] transposeToGetIntuitiveXAndYRight(boolean[][] transposedMapTiles, int lengthVertical, int lengthHorizontal) {
        boolean[][] result = new boolean[lengthHorizontal][lengthVertical];
        for (int x = 0; x < lengthHorizontal; x++) {
            for (int y = 0; y < lengthVertical; y++) {
                result[x][y] = transposedMapTiles[y][x];
            }
        }
        return result;
    }

    private boolean[] convertHorizontalMapLine(JSONArray horizontalLine) throws InvalidInputException {
        boolean[] converted = new boolean[horizontalLine.length()];
        for (int x = 0; x < horizontalLine.length(); x++) {
            Object tileValue = horizontalLine.get(x);
            if (tileValue instanceof String || tileValue instanceof Integer) {
                converted[x] = convertMapTile("" + tileValue);
            } else {
                throw new InvalidInputException("Map array element was neither string nor int. Is: " + tileValue.toString());
            }
        }
        return converted;
    }

    private boolean convertMapTile(String element) throws InvalidInputException {
        if (element.equals(ACCESSIBLE)) {
            return true;
        } else if (element.equals(NOT_ACCESSIBLE)) {
            return false;
        } else {
            throw new InvalidInputException("Invalid map structure element found: " + element);
        }
    }
}
