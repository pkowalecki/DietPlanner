package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MealHistoryDTO{

    private UUID public_id;
    private Long userId;
    private String mealsIds;
    private LocalDateTime created;
    private Double multiplier;
}
