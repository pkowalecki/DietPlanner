package pl.kowalecki.dietplanner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T convertToDTO(Object o, Class<T> klaseu){
        return objectMapper.convertValue(o, klaseu);
    }
    public <T> List<T> convertToDTOList(List<?> list, Class<T> klaseu){
        return list.stream()
                .map(element -> objectMapper.convertValue(element, klaseu))
                .collect(Collectors.toList());
    }
}
