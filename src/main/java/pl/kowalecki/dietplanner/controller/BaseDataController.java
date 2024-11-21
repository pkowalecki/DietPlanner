//package pl.kowalecki.dietplanner.controller;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import pl.kowalecki.dietplanner.utils.JwtUtil;
//
//import java.io.IOException;
//
//@ControllerAdvice
//@RequiredArgsConstructor
//public class BaseDataController {
//
//    private final JwtUtil jwtUtil;
//    @Value("${dietplanner.app.jwtCookieName}")
//    private String jwtName;
//
//    @ModelAttribute
//    public void addUserInfoToModel(HttpServletResponse response, HttpServletRequest request, Model model) throws IOException {
//        String token = jwtUtil.extractTokenFromRequest(request, jwtName);
//        if (token != null && jwtUtil.isTokenValid(token)) {
//            Claims claims = jwtUtil.getClaims(token);
//            model.addAttribute("isLoggedIn", true);
//            model.addAttribute("userId", claims.get("id"));
//            model.addAttribute("userRoles", claims.get("roles"));
//        }
//    }
//
//}
