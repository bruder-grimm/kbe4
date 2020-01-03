package htwb.ai.mundt.filter;

public interface IAuthenticator {
    /** authenticates a token
     *  @param token the token
     *  @return if its valid */
    boolean authenticate(String token);
}
