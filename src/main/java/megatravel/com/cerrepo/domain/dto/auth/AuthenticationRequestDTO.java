package megatravel.com.cerrepo.domain.dto.auth;

/**
 * Represents user's authentication request.
 * Used when user tries to log in to the system.
 */
public class AuthenticationRequestDTO {

    /**
     * User's username.
     */
    private String username;

    /**
     * User's password.
     */
    private String password;

    public AuthenticationRequestDTO() {
    }

    public AuthenticationRequestDTO(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}