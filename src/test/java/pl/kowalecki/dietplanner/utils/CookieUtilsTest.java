package pl.kowalecki.dietplanner.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.test.util.ReflectionTestUtils;
import pl.kowalecki.dietplanner.exception.InvalidCookieException;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CookieUtilsTest {


    private static final String SAMPLE_TOKEN = "sampleToken";
    private static final int maxAge = 3600;

    private CookieUtils cookieUtils;
    private HttpServletResponse response;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        cookieUtils = new CookieUtils();
        response = mock(HttpServletResponse.class);
        request = mock(HttpServletRequest.class);

        ReflectionTestUtils.setField(cookieUtils, "accessTokenCookieName", "dietapp");
        ReflectionTestUtils.setField(cookieUtils, "refreshTokenCookieName", "dietappRef");
    }


    @Test
    void setAccessTokenCookieShouldSetCookie(){
        //given
        String expectedCookieName = "dietapp";
        //when
        cookieUtils.setAccessTokenCookie(response, SAMPLE_TOKEN, maxAge);
        //then
        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

        verify(response).addHeader(headerCaptor.capture(), valueCaptor.capture());

        assertThat(headerCaptor.getValue()).isEqualTo(HttpHeaders.SET_COOKIE);

        ResponseCookie expectedCookie = ResponseCookie.from(expectedCookieName, SAMPLE_TOKEN)
                .path("/")
                .maxAge(maxAge)
                .httpOnly(true)
                .sameSite("Lax")
                .build();

        assertThat(valueCaptor.getValue()).isEqualTo(expectedCookie.toString());
    }

    @Test
    void setRefreshTokenCookieShouldSetCookie(){
        //given
        String expectedCookieName = "dietappRef";
        //when
        cookieUtils.setRefreshTokenCookie(response, SAMPLE_TOKEN, maxAge);
        //then
        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

        verify(response).addHeader(headerCaptor.capture(), valueCaptor.capture());

        assertThat(headerCaptor.getValue()).isEqualTo(HttpHeaders.SET_COOKIE);

        ResponseCookie expectedCookie = ResponseCookie.from(expectedCookieName, SAMPLE_TOKEN)
                .path("/")
                .maxAge(maxAge)
                .httpOnly(true)
                .sameSite("Lax")
                .build();

        assertThat(valueCaptor.getValue()).isEqualTo(expectedCookie.toString());
    }


    @Test
    void setAccessTokenCookieShouldThrowExceptionWhenResponseIsNull(){
        //given
        HttpServletResponse httpServletResponse = null;
        //when
        //then
        assertThatThrownBy(() -> cookieUtils.setAccessTokenCookie(httpServletResponse, SAMPLE_TOKEN, maxAge))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void setAccessTokenCookieShouldThrowExceptionWhenMaxAgeIsNegative(){
        //given
        int maxAge = -1;
        //when
        //then
        assertThatThrownBy(() -> cookieUtils.setAccessTokenCookie(response, SAMPLE_TOKEN, maxAge))
                .isInstanceOf(InvalidCookieException.class);
    }

    @Test
    void setAccessTokenCookieShouldThrowExceptionWhenTokenIsNull(){
        //given
        String token = null;
        //when
        //then
        assertThatThrownBy(() -> cookieUtils.setAccessTokenCookie(response, token, maxAge))
                .isInstanceOf(InvalidCookieException.class);
    }

    @Test
    void setAccessTokenCookieShouldThrowExceptionWhenCookieNameIsNull(){
        //given
        ReflectionTestUtils.setField(cookieUtils, "accessTokenCookieName", null);
        //when
        //then
        assertThatThrownBy(() -> cookieUtils.setAccessTokenCookie(response, SAMPLE_TOKEN, maxAge))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldExtractJwtokenFromAccessCookie(){
        //given
        String cookieName = "dietapp";
        Cookie accessTokenCookie = new Cookie(cookieName, SAMPLE_TOKEN);
        Cookie[] cookies = {accessTokenCookie};
        when(request.getCookies()).thenReturn(cookies);
        //when
        String token = cookieUtils.extractJwtokenFromAccessCookie(request);
        //then
        assertThat(token).isEqualTo(SAMPLE_TOKEN);

    }

    @Test
    void shouldExtractRefreshTokenFromRefreshCookie(){
        //given
        String cookieName = "dietappRef";
        Cookie accessTokenCookie = new Cookie(cookieName, SAMPLE_TOKEN);
        Cookie[] cookies = {accessTokenCookie};
        when(request.getCookies()).thenReturn(cookies);
        //when
        String token = cookieUtils.extractRefreshTokenFromRefreshCookie(request);
        //then
        assertThat(token).isEqualTo(SAMPLE_TOKEN);

    }

    @Test
    void extractJwtokenFromAccessCookieShouldReturnExceptionWhenRequestIsNull(){
        //given
        request = null;
        //when
        //then
        assertThatThrownBy(() -> cookieUtils.extractJwtokenFromAccessCookie(request)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void extractJwtokenFromAccessCookieShouldReturnExceptionWhenCookieNameIsNull(){
        //given
        ReflectionTestUtils.setField(cookieUtils, "accessTokenCookieName", null);
        //when
        //then
        assertThatThrownBy(() -> cookieUtils.extractJwtokenFromAccessCookie(request)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void extractJwtokenFromAccessCookieShouldReturnNullWhenThereIsNoCookies(){
        //given
        Cookie[] cookies = {};
        when(request.getCookies()).thenReturn(cookies);
        //when
        String token = cookieUtils.extractJwtokenFromAccessCookie(request);
        //then
        assertThat(token).isNull();
    }
}