package com.example.identityservice.service;

import com.example.identityservice.dto.request.AuthenticationRequest;
import com.example.identityservice.dto.request.IntrospectRequest;
import com.example.identityservice.dto.response.AuthenticationResponse;
import com.example.identityservice.dto.response.IntrospectResponse;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.exception.ErrorCode;
import com.example.identityservice.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
@Data
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true )
public class AuthenticationService {
    UserRepository userRepository;
    @NonFinal
    protected static final String SIGNER_KEY = " oqIC6eZm4jP/jX+JjfFKlPXthjv3zwWgC0kIu606yEYnR/z5zqQnZHAra5FRcEH3";
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        return  IntrospectResponse.builder().
                valid(verified && expityTime.after(new Date()))
                . build();


    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(()-> new AppException(ErrorCode.USER_NOTEXISTED));
        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);

            var token = generateToken(request.getUsername());
            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        }
            private String generateToken(String username){
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet jwtClaimSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("Khanh").issueTime( new Date())
                    .expirationTime(new Date(
                            Instant.now().plus(2, ChronoUnit.HOURS).toEpochMilli()))
                    .claim("customClaim", " Custom")
                    .build();
            Payload payload = new Payload(jwtClaimSet.toJSONObject());

            JWSObject jwsObject = new JWSObject(header, payload);
            try {
                jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
                return  jwsObject.serialize();
            } catch (JOSEException e) {
                log.error("Cannot create Token", e);
                throw new RuntimeException(e);
            }

        }
    }

}
