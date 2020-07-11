package manfred.manfreditor;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import manfred.manfreditor.exception.InvalidInputException;
import manfred.manfreditor.map.MapPane;
import manfred.manfreditor.map.MapReader;
import manfred.manfreditor.map.object.MapObject;
import manfred.manfreditor.map.object.MapObjectReader;
import manfred.manfreditor.map.object.MapObjectsPane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("manfreditor.fxml")
public class Controller {
    public static final int OBJECTS_GRID_SIZE = 100;

    public Button loadMapButton;
    public MapPane mapPane;
    public ScrollPane mapPaneContainer;
    public MapObjectsPane mapObjectsPane;

    private final MapReader mapReader;
    private final MapObjectReader mapObjectReader;
    public Button loadMapObjectButton;

    public Controller(MapReader mapReader, MapObjectReader mapObjectReader) {
        this.mapReader = mapReader;
        this.mapObjectReader = mapObjectReader;
    }

    public void loadMapDialogue(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lade Map");
        dialog.setHeaderText("Was willsch denn jetzt scho wieder?");
        dialog.setContentText("Name der Map:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::loadMap);
    }

    private void loadMap(String mapName) {
        try {
            mapPane.setMap(mapReader.load(mapName), this);
        } catch (InvalidInputException | IOException e) {
            showErrorMessagePopup(e.getMessage());
        }
    }

    private void showErrorMessagePopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler!!1!!1!1");
        alert.setHeaderText("Map nicht gefunden");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onMapTileClicked(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        int x = GridPane.getColumnIndex(source);
        int y = GridPane.getRowIndex(source);

        mapPane.tileClicked(x, y, mapObjectsPane.getSelectedObject());
    }

    public void onObjectTileClicked(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        int x = GridPane.getColumnIndex(source);
        int y = GridPane.getRowIndex(source);
        mapObjectsPane.select(x, y);
    }

    public void loadMapObjectDialogue(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lade Mapobjekt");
        dialog.setHeaderText("Was willsch denn jetzt scho wieder?");
        dialog.setContentText("Name des Objekts:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::loadMapObject);
    }

    private void loadMapObject(String name) {
        try {
            MapObject mapObject = mapObjectReader.load(name);

            mapObjectsPane.addMapObject(mapObject, this);
        } catch (InvalidInputException | IOException e) {
            showErrorMessagePopup(e.getMessage());
        }
    }
}
