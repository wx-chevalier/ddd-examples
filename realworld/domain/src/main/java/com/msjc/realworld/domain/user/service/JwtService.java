package com.msjc.realworld.domain.user.service;

import com.msjc.realworld.domain.user.UserDO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface JwtService {
    String toToken(UserDO user);

    Optional<String> getSubFromToken(String token);
}
