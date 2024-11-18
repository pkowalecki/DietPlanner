//package pl.kowalecki.dietplanner.services.WebPage;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.ui.Model;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//import pl.kowalecki.dietplanner.model.DTO.User.LoginResponseDTO;
//import pl.kowalecki.dietplanner.model.DTO.User.UserDTO;
////import pl.kowalecki.dietplanner.security.jwt.AuthJwtUtils;
//import pl.kowalecki.dietplanner.utils.ClassMapper;
//import pl.kowalecki.dietplanner.utils.UrlTools;
//
//import java.io.IOException;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Configuration
//@Slf4j
//@AllArgsConstructor
//public class WebPageService implements IWebPageService {
//
//    @Autowired
//    private AuthJwtUtils jwtUtils;
//
//    @Autowired
//    private HttpSession session;
//
////    @Autowired
////    private final RestTemplate restTemplate;
//
//
//    @Autowired
//    ClassMapper classMapper;
//
//    @Override
//    public boolean hasPermission() {
//        return false;
//    }
//
//    @Override
//    public boolean validateToken(HttpServletRequest request) {
//        String token = jwtUtils.getJwtFromCookies(request);
//        return token != null && jwtUtils.validateJwtToken(token);
//    }
//
//    @Override
//    public boolean isUserAuthorized(String requiredRole) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getAuthorities() != null) {
//            return authentication.getAuthorities().stream()
//                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredRole));
//        }
//        return false;
//    }
//
//
//    @Override
//    public void logUserAction(HttpServletRequest request) {
//        try {
//            HttpSession session = request.getSession(false);
//            String method = request.getMethod();
//            String uri = request.getRequestURI();
//            String remoteAddr = request.getRemoteAddr();
//            if (session != null) {
//                System.out.println("Remote address: " + remoteAddr);
//                UserDTO user = getLoggedUser();
//                if (user != null) {
//                    String username = user.getEmail();
//                    System.out.println("action: " + method + " on " + uri + " username: " + username);
//                    Enumeration<String> parameterNames = request.getParameterNames();
//                    while (parameterNames.hasMoreElements()) {
//                        String paramName = parameterNames.nextElement();
//                        String[] paramValues = request.getParameterValues(paramName);
//                        for (String value : paramValues) {
//                            System.out.println(paramName + ": " + value);
//                        }
//                    }
//                } else {
//                    System.out.println("No user in session. action: " + method + " on " + uri);
//                }
//            } else {
//                System.out.println("No session.action: " + method + " on " + uri);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void setSessionAttribute(String key, Object value) {
//
//    }
//
//    @Override
//    public Object getSessionAttribute(String key) {
//        return null;
//    }
//
//    @Override
//    public void invalidateSession() {
//
//    }
//
////    public <T> ResponseEntity<T> sendGetRequest(String url, Class<T> responseType, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
////        return sendRequest(url, HttpMethod.GET, null, responseType, httpRequest, httpResponse);
////    }
////
////    public <T> ResponseEntity<T> sendPostRequest(String url, Object request, Class<T> responseType, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
////        return sendRequest(url, HttpMethod.POST, request, responseType, httpRequest, httpResponse);
////    }
//
////    private <T> ResponseEntity<T> sendRequest(String url, HttpMethod method, Object request, Class<T> responseType, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
////        try {
////            HttpHeaders headers = new HttpHeaders();
////            headers.setContentType(MediaType.APPLICATION_JSON);
////
////            String token = jwtUtils.getJwtFromCookies(httpRequest);
////            if (token != null) {
////                headers.add(HttpHeaders.COOKIE, "dietapp=" + token);
////            }
////
////            HttpEntity<Object> entity = new HttpEntity<>(request, headers);
////            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
////
////
////            List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
////            if (cookies != null) {
////                for (String cookieHeader : cookies) {
////                    httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
////                }
////            }
////
////            MediaType contentType = response.getHeaders().getContentType();
////            if (contentType != null) {
////                //            log.info("Received response from {}: {}", url, response.getBody());
////                if (contentType.includes(MediaType.APPLICATION_JSON)) {
////                    T body = new ObjectMapper().readValue(response.getBody(), responseType);
////                    return new ResponseEntity<>(body, response.getStatusCode());
////                } else if (contentType.includes(MediaType.TEXT_PLAIN)) {
////                    T body = (T) new String(response.getBody());
////                    return new ResponseEntity<>(body, response.getStatusCode());
////                } else if(contentType.includes(MediaType.APPLICATION_OCTET_STREAM)){
////                    byte[] body = response.getBody().getBytes();
////                    return new ResponseEntity<>((T) body, response.getHeaders(), response.getStatusCode());
////                }else {
////                    log.error("Unexpected content type: {}", response.getHeaders().getContentType());
////                    throw new HttpClientErrorException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
////                }
////            }else {
////                log.error("Response content type is null!");
////                throw new HttpClientErrorException(HttpStatus.NO_CONTENT, "No content");
////            }
////
////        } catch (HttpClientErrorException e) {
////            String refreshToken = jwtUtils.getJwtRefreshCookie(httpRequest);
////            if (refreshToken != null) {
////                ResponseEntity<String> refreshResponse = sendRefreshJwtRequest(refreshToken, httpRequest, httpResponse);
////                if (refreshResponse.getStatusCode() == HttpStatus.OK) {
////                    String newJwt = refreshResponse.getBody();
////                    if (newJwt != null) {
////                        return sendRequest(url, method, request, responseType, httpRequest, httpResponse);
////                    }
////                }
////            }
////
////            HttpStatusCode status = e.getStatusCode();
////            T body = null;
////            try {
////                body = new ObjectMapper().readValue(e.getResponseBodyAsString(), responseType);
////            } catch (JsonProcessingException ex) {
////                log.error("Error parsing error response: {}", ex.getMessage());
////            }
////            log.error("Error during request to {}: {}", url, e.getMessage());
////            return new ResponseEntity<>(body, status);
////        } catch (IOException e) {
////            log.error("Error processing response: {}", e.getMessage());
////            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////    }
//
////    @Override
////    public ResponseEntity<String> sendRefreshJwtRequest(String refreshToken, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
////        try {
////            String url = "http://" + UrlTools.apiUrl + "/auth/refresh";
////            HttpHeaders headers = new HttpHeaders();
////            headers.setContentType(MediaType.APPLICATION_JSON);
////            headers.add(HttpHeaders.COOKIE, "dietappRef=" + refreshToken);
////
////            HttpEntity<Object> entity = new HttpEntity<>(null, headers);
////            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
////
////            List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
////            if (cookies != null) {
////                for (String cookieHeader : cookies) {
////                    httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
////                }
////            }
////            return response;
////        } catch (Exception e) {
////            log.error("Error sending refresh request: {}", e.getMessage());
////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////    }
//
//    @Override
//    public UserDTO getLoggedUser() {
//       LoginResponseDTO loginResponseDTO = classMapper.convertToDTO(session.getAttribute("user"), LoginResponseDTO.class);
//        if (loginResponseDTO != null) {
//            return UserDTO.builder()
//                    .id(loginResponseDTO.getId())
//                    .surname(loginResponseDTO.getSurname())
//                    .nickName(loginResponseDTO.getNickName())
//                    .name(loginResponseDTO.getName())
//                    .email(loginResponseDTO.getEmail())
//                    .roles(loginResponseDTO.getRoles())
//                    .build();
//        }
//        return null;
//    }
//
//    @Override
//    public void addCommonWebData(Model model) {
//        UserDTO user = getLoggedUser();
//        if (user != null) {
//            model.addAttribute("user", user);
//        }
//        getMsg(model);
//    }
//
//    public void setMsg(MessageType type, String message) {
//        Map<String, String> messageData = new HashMap<>();
//        messageData.put("type", type.name());
//        messageData.put("message", message);
//        messageData.put("icon", type.getPole());
//        session.setAttribute("webMsg", messageData);
//    }
//
//    private void getMsg(Model model) {
//        Map<String, String> messageData = (Map<String, String>) session.getAttribute("webMsg");
//        if (messageData != null) {
//            model.addAttribute("webMsg", messageData);
//            removeMsg();
//        }
//    }
//
//    private void removeMsg(){
//        session.removeAttribute("webMsg");
//    }
//
//}
