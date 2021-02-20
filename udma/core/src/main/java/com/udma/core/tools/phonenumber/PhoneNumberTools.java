package com.udma.core.tools.phonenumber;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import io.vavr.API;
import io.vavr.control.Try;

public class PhoneNumberTools {

  private static final PhoneNumberUtil UTIL = PhoneNumberUtil.getInstance();

  public static Try<PhoneNumber> parse(String phoneNumber) {
    return API.Try(() -> UTIL.parse(phoneNumber, "CN"));
  }

  public static Try<PhoneNumber> parse(String phoneNumber, String defaultRegion) {
    return API.Try(() -> UTIL.parse(phoneNumber, defaultRegion));
  }

  public static String format(PhoneNumber phoneNumber, PhoneNumberFormat phoneNumberFormat) {
    return UTIL.format(phoneNumber, phoneNumberFormat);
  }

  public static String formatE164(PhoneNumber phoneNumber) {
    return format(phoneNumber, PhoneNumberFormat.E164);
  }
}
