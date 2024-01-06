package com.peterczigany.vote.model;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class ShortUrlIdGenerator implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    int length = 6;

    StringBuilder builder = new StringBuilder(length);
    Random random = new SecureRandom();

    for (int i = 0; i < length; i++) {
      int index = random.nextInt(chars.length());
      builder.append(chars.charAt(index));
    }

    return builder.toString();
  }
}
