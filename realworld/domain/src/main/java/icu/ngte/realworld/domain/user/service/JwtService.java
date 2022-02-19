package icu.ngte.realworld.domain.user.service;

import icu.ngte.realworld.domain.user.UserDO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface JwtService {
    String toToken(UserDO user);

    Optional<String> getSubFromToken(String token);
}
