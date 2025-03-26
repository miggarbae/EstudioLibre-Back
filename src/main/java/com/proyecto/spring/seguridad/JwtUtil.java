package com.proyecto.spring.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clave secreta en Base64
    private static final String SECRET_KEY = "aW50ZXJuYS1jbGF2ZS1zZWNyZXRhLXBhcmEtand0LWF1dGg=";

    // Método para obtener la clave de firma
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extraer el nombre de usuario del token
    public String obtenerUsername(String token) {  // 🔥 Cambié el nombre para que coincida con JwtAuthenticationFilter
        return extractClaim(token, Claims::getSubject);
    }

    // Extraer cualquier propiedad del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Obtener todos los claims del token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Generar un nuevo token
    public String generateToken(String username, String role) {
        return Jwts.builder()
            .setSubject(username)
            .claim("role", role) // Añadimos el rol
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }
    

    // Validar si un token es correcto
    public boolean validarToken(String token) { // 🔥 Cambié el nombre para que coincida con JwtAuthenticationFilter
        return !isTokenExpired(token);
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Validar token con datos de usuario
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = obtenerUsername(token);
        return username.equals(userDetails.getUsername()) && validarToken(token);
    }
}
