    package com.aksa.chatapp.security;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.server.ServerHttpRequest;
    import org.springframework.http.server.ServletServerHttpRequest;
    import org.springframework.stereotype.Component;
    import org.springframework.web.socket.server.HandshakeInterceptor;

    import jakarta.servlet.http.HttpServletRequest;
    import java.util.Map;

    @Component
    public class JwtHandshakeInterceptor implements HandshakeInterceptor {

        @Autowired
        private JwtTokenUtil jwtTokenUtil;

        @Override
        public boolean beforeHandshake(ServerHttpRequest request,
                                       org.springframework.http.server.ServerHttpResponse response,
                                       org.springframework.web.socket.WebSocketHandler wsHandler,
                                       Map<String, Object> attributes) throws Exception {
            if (!(request instanceof ServletServerHttpRequest)) {
                return false;
            }
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String authHeader = servletRequest.getHeader("Authorization");
            String token = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            } else {
                token = servletRequest.getParameter("token");
            }
            if (token != null && jwtTokenUtil.validateToken(token)) {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                attributes.put("username", username);
                return true;
            }
            return false;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request,
                                   org.springframework.http.server.ServerHttpResponse response,
                                   org.springframework.web.socket.WebSocketHandler wsHandler, Exception exception) {}
    }
