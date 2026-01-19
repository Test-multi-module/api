package net.testproj.auth.services;

import lombok.RequiredArgsConstructor;
import net.testproj.auth.properties.JwtProps;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LoginCodeService {
    private static final SecureRandom RANDOM = new SecureRandom();
    
    private final Map<String, String> codeStorage = new ConcurrentHashMap<>();
    
    private final JwtProps props;

}
