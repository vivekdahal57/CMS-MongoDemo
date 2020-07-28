package com.cms.authentication.controller;

import com.cms.authentication.JwtTokenUtil;
import com.cms.authentication.model.JwtTokenRequest;
import com.cms.authentication.model.JwtTokenResponse;
import com.cms.userManagement.service.UsersService;
import com.cms.util.PasswordResolveUtil;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
public class JwtAuthenticationRestController {

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordResolveUtil passwordResolveUtil;

    @RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
    public ResponseEntity<JwtTokenResponse> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
            throws AuthenticationException {

        final String userName = authenticationRequest.getUsername();
        final String password = authenticationRequest.getPassword();

        String userFullName = authenticate(userName, password);
        if (userFullName == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        final String token = jwtTokenUtil.generateToken(userFullName.split("\\$")[0]);
        return ResponseEntity.ok(new JwtTokenResponse(token, userFullName.split("\\$")[1]));
    }

    @RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
    public ResponseEntity<JwtTokenResponse> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            String userName = jwtTokenUtil.getUsernameFromToken(refreshedToken);
            return ResponseEntity.ok(new JwtTokenResponse(refreshedToken, userName));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private String authenticate(String username, String password) {
        try {
            Objects.requireNonNull(username);
            Objects.requireNonNull(password);

            UserDetails user = usersService.loadUserByUsername(username);
            String userPassword = user.getPassword();
            if (userPassword.equals(passwordResolveUtil.getResolvedPassword(username, password))) {
                return user.getUsername();
            } else {
                return null;
            }
        } catch (NullPointerException e) {
            return null;
        }
    }
}
