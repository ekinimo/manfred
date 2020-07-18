package manfred.manfreditor.map.object;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapObjectImageView extends ImageView {
    public MapObjectImageView(Image image, int y) {
        super(image);
        this.setPreserveRatio(true);
        this.setViewOrder(-y);
    }

    public void restrictViewPort(double subimageWidth, double subimageHeight) {
        this.setViewport(new Rectangle2D(0, this.getImage().getHeight() - subimageHeight, subimageWidth, subimageHeight));
    }
}
