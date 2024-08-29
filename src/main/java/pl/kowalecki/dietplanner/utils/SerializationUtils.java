package pl.kowalecki.dietplanner.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class SerializationUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Konwertuje obiekt na listę określonego typu.
     *
     * @param data       Obiekt do konwersji
     * @param typeRef    Typ referencyjny określający docelowy typ listy
     * @param <T>        Typ elementów w liście
     * @return           Lista obiektów określonego typu
     * @throws IOException Jeśli wystąpi błąd podczas konwersji
     */
    public static <T> List<T> convertToList(Object data, TypeReference<List<T>> typeRef) throws IOException {
        return objectMapper.convertValue(data, typeRef);
    }
}
