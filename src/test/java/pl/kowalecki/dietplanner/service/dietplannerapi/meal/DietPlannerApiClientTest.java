package pl.kowalecki.dietplanner.service.dietplannerapi.meal;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.client.dpa.meal.DietPlannerApiClient;
import pl.kowalecki.dietplanner.mapper.MealHistoryMapper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


class DietPlannerApiClientTest {

    public static MockWebServer mockWebServer;
    private DietPlannerApiClient client;
    private MealHistoryMapper mapper;
    @BeforeEach
    void setUpMockWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        client = new DietPlannerApiClient(WebClient.create(mockWebServer.url("/").url().toString()), mapper);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldReturnPageOfMeals_whenResponseIsValid() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\n" +
                        "  \"content\": [\n" +
                        "    {\n" +
                        "      \"id\": 1,\n" +
                        "      \"name\": \"Grilled Chicken\",\n" +
                        "      \"description\": \"Healthy grilled chicken breast\",\n" +
                        "      \"calories\": 250\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 2,\n" +
                        "      \"name\": \"Vegan Salad\",\n" +
                        "      \"description\": \"Fresh mixed greens with avocado\",\n" +
                        "      \"calories\": 180\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"page\": 0,\n" +
                        "  \"size\": 10,\n" +
                        "  \"totalPages\": 1,\n" +
                        "  \"totalElements\": 2\n" +
                        "}")
                .setBodyDelay(1, TimeUnit.SECONDS));
        client.getPageMeals(1, 1, "all").block();
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/api/v1/meal/getMealsData?page=1&size=1&mealType=all");
    }
}