package br.gov.ba.sesab.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    private PasswordUtil() {}

    public static String hash(String senhaPlana) {
        return BCrypt.hashpw(senhaPlana, BCrypt.gensalt());
    }

    public static boolean verificar(String senhaDigitada, String senhaHash) {
        if (senhaDigitada == null || senhaHash == null) {
            return false;
        }
        return BCrypt.checkpw(senhaDigitada, senhaHash);
    }
}
