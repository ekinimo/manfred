package manfred.manfreditor.map;

import manfred.manfreditor.ImageLoader;
import manfred.manfreditor.exception.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class MapReaderTest {
    private MapReader underTest;

    @BeforeEach
    void init() {
        underTest = new MapReader(mock(ImageLoader.class));
    }

    @Test
    void testConvert() throws InvalidInputException {
        boolean[][] expected = {
            {false},
            {true}
        };
        String input = "{name: test, map: [[0, 1]]}";

        Map result = underTest.convert(input);

        Assertions.assertArrayEquals(expected, result.getArray());
    }
}
