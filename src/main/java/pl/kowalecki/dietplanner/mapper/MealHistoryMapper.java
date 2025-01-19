package pl.kowalecki.dietplanner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.kowalecki.dietplanner.model.DTO.meal.MealHistoryDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MealHistoryMapper {

    @Mapping(target = "created", source = "created", qualifiedByName = "formatDate")
    MealHistoryDTO mapToDto(MealHistoryDTO mealHistory);

    @Named("formatDate")
    default String formatDate(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}