package manfred.manfreditor;

import manfred.manfreditor.map.MapObjectReader;
import manfred.manfreditor.map.MapReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManfreditorContext {
    @Bean
    public MapReader mapReader(ImageLoader imageLoader) {
        return new MapReader(imageLoader);
    }

    @Bean
    public MapObjectReader mapObjectReader(ImageLoader imageLoader) {
        return new MapObjectReader(imageLoader);
    }

    @Bean
    public ImageLoader imageLoader() {
        return new ImageLoader();
    }
}
