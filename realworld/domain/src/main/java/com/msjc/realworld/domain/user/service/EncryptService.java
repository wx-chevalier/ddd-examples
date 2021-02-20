package com.msjc.realworld.domain.user.service;

public interface EncryptService {

  String encrypt(String password);

  boolean check(String checkPassword, String realPassword);
}
