package manfred.manfreditor.map;

import manfred.game.Game;
import manfred.manfreditor.ImageLoader;
import manfred.manfreditor.exception.InvalidInputException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MapReader {
    public static final String ACCESSIBLE = "1";
    public static final String NOT_ACCESSIBLE = "0";
    public static final String PATH_MAPS = Game.PATH_DATA + "maps\\";
    public static final String PATH_MAPS_TILE_IMAGES = PATH_MAPS + "tiles\\";

    private final ImageLoader imageLoader;

    public MapReader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public Map load(String name) throws InvalidInputException, IOException {
        String jsonMap = read(PATH_MAPS + name + ".json");
        return convert(jsonMap);
    }

    String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("", input);
    }

    Map convert(String jsonString) throws InvalidInputException {
        try {
            JSONObject jsonInput = new JSONObject(jsonString);

            String name = jsonInput.getString("name");
            boolean[][] mapTiles = convertMap(jsonInput.getJSONArray("map"));

            return new Map(name, mapTiles);
        } catch (JSONException $e) {
            throw new InvalidInputException($e.getMessage() + " in map " + jsonString);
        }
    }

    private boolean[][] convertMap(JSONArray jsonMap) throws InvalidInputException {
        int lengthVertical = jsonMap.length();
        int lengthHorizontal = jsonMap.getJSONArray(0).length();

        boolean[][] transposedMapTiles = new boolean[lengthVertical][lengthHorizontal];
        for (int y = 0; y < lengthVertical; y++) {
            JSONArray horizontalLine = jsonMap.getJSONArray(y);
            if (horizontalLine.length() != lengthHorizontal) {
                throw new InvalidInputException("Map needs to be rectangular. First line: " + lengthHorizontal + ", line: " + y + " " + horizontalLine.length());
            }
            transposedMapTiles[y] = convertHorizontalMapLine(horizontalLine);
        }

        return transposeToGetIntuitiveXAndYRight(transposedMapTiles, lengthVertical, lengthHorizontal);
    }

    private boolean[][] transposeToGetIntuitiveXAndYRight(boolean[][] original, int lengthVertical, int lengthHorizontal) {
        boolean[][] transposed = new boolean[lengthHorizontal][lengthVertical];
        for (int x = 0; x < lengthHorizontal; x++) {
            for (int y = 0; y < lengthVertical; y++) {
                transposed[x][y] = original[y][x];
            }
        }
        return transposed;
    }

    private boolean[] convertHorizontalMapLine(JSONArray horizontalJsonLine) throws InvalidInputException {
        boolean[] mapTileLine = new boolean[horizontalJsonLine.length()];
        for (int x = 0; x < horizontalJsonLine.length(); x++) {
            Object tileValue = horizontalJsonLine.get(x);
            if (tileValue instanceof String || tileValue instanceof Integer) {
                mapTileLine[x] = convertMapTile("" + tileValue);
            } else {
                throw new InvalidInputException("Map array element was neither string nor int. Is: " + tileValue.toString());
            }
        }
        return mapTileLine;
    }

    private boolean convertMapTile(String tileValue) {
        return tileValue.equals(ACCESSIBLE);
    }
}
