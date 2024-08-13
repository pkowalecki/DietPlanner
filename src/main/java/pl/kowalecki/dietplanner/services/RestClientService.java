package pl.kowalecki.dietplanner.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class RestClientService {

    private final RestTemplate restTemplate;

    public <T> ResponseEntity<T> sendGetRequest(String url, Class<T> responseType) {
        try {
            return restTemplate.getForEntity(url, responseType);
        } catch (HttpClientErrorException e) {
            HttpStatusCode status = e.getStatusCode();
            T body = null;
            try {
                body = new ObjectMapper().readValue(e.getResponseBodyAsString(), responseType);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
            return new ResponseEntity<>(body, status);
        }
    }

    public <T> ResponseEntity<T> sendPostRequest(String url, Object request, Class<T> responseType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(request, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
