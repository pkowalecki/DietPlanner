package pl.kowalecki.dietplanner.controller.document;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kowalecki.dietplanner.service.document.IDocumentService;
import reactor.core.publisher.Mono;


@RequestMapping("/auth/document")
@Controller
@AllArgsConstructor
@Slf4j
public class DocumentController {

    IDocumentService documentService;

    @PostMapping(value = "/downloadMealDocument", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<byte[]>> downloadMealDocument(@RequestParam String pageId) {
        return documentService.downloadMealsPlan(pageId);
    }

}
