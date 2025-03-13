package pl.kowalecki.dietplanner.service.document;


import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IDocumentService {
    Mono<ResponseEntity<byte[]>> downloadMealsPlan(String pageId);
}
