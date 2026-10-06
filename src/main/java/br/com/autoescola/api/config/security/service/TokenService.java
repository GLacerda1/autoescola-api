package br.com.autoescola.api.config.security.service;

import br.com.autoescola.api.application.core.domain.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class TokenService {
    private static final String ISSUER = "autoescola-api";
    private final Algorithm algorithm;

    public TokenService(@Value("${api.security.token.secret}") String secret) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET precisa ter no mínimo 32 bytes UTF-8.");
        }
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(Usuario usuario) {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException ex){
            throw new RuntimeException("Erro ao gerar o token JWT!", ex);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException ex){
            throw new RuntimeException("TokenJWT inválido ou expirado!", ex);
        }
    }

    private Instant dataExpiracao() {
        return Instant.now().plusSeconds(30 * 60L);
    }
}
