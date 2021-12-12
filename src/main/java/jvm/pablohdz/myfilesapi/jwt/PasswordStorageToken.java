package jvm.pablohdz.myfilesapi.jwt;

public interface PasswordStorageToken {
    String getPasswordToSignToken();
    int getExpirationTokenTime();
}
