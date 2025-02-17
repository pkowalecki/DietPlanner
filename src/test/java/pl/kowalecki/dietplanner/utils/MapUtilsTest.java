package pl.kowalecki.dietplanner.utils;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapUtilsTest {

    List<TestClass> testData;

    @BeforeEach
    void setUp() {
        //given
        testData = Arrays.asList(
                new TestClass(1, "Apple", "Jabłko"),
                new TestClass(2, "Banana", "Banan")
        );
    }

    @Test
    void mapToFtlShouldReturnProperMapWithNameField() {

        //when
        List<Map<String, String>> result = MapUtils.mapToFtl(testData, "id", "name");

        //then
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).get("value"));
        assertEquals("Apple", result.get(0).get("name"));
        assertEquals("2", result.get(1).get("value"));
        assertEquals("Banana", result.get(1).get("name"));
        System.out.println(result);

    }

    @Test
    void mapToFtlShouldReturnProperMapWithName2Field() {
        //when
        List<Map<String, String>> result = MapUtils.mapToFtl(testData, "id", "name2");

        //then
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).get("value"));
        assertEquals("Jabłko", result.get(0).get("name"));
        assertEquals("2", result.get(1).get("value"));
        assertEquals("Banan", result.get(1).get("name"));
        System.out.println(result);

    }

    @Test
    void mapToFtlShouldReturnEmptyValueWhenValueMethodDoesNotExist() {
        //when
        //then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MapUtils.mapToFtl(testData, "ide", "name")
        );
        assertTrue(exception.getMessage().contains("Błąd: metoda 'getIde' nie istnieje"));
    }

    @Test
    void mapToFtlShouldReturnEmptyValueWhenNameMethodDoesNotExist() {
        //when
        //then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MapUtils.mapToFtl(testData, "id", "getName3")
        );
        assertTrue(exception.getMessage().contains("Błąd: metoda 'getGetName3' nie istnieje"));
    }
}


@Getter
class TestClass {
    private final int id;
    private final String name;
    private final String name2;

    public TestClass(int id, String name, String name2) {
        this.id = id;
        this.name = name;
        this.name2 = name2;
    }
}