package pl.kowalecki.dietplanner.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.kowalecki.dietplanner.utils.JwtUtil;

@ControllerAdvice
@RequiredArgsConstructor
public class BaseDataController {

    private final JwtUtil jwtUtil;
    @Value("${dietplanner.app.jwtCookieName}")
    private String jwtName;

    @ModelAttribute
    public void addUserInfoToModel(HttpServletRequest request, Model model) {
        String token = jwtUtil.extractTokenFromRequest(request, jwtName);

        if (token != null && jwtUtil.isTokenValid(token)) {
            Claims claims = jwtUtil.getClaims(token);
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("userId", claims.get("id"));
            model.addAttribute("userRoles", claims.get("roles"));
        } else {
            model.addAttribute("isLoggedIn", false);
        }
    }

}
