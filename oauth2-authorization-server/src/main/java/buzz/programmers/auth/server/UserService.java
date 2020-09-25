package buzz.programmers.auth.server;


import buzz.programmers.auth.server.model.Account;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@ConfigurationProperties(prefix = "accounts")
public class UserService implements UserDetailsService {

    private Set<Account> users;
    private final PasswordEncoder passwordEncoder;

    public UserService(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Set<Account> getUsers() {
        return Set.copyOf(users);
    }

    public Optional<Account> loadAccountByUsername(final String username) {
        return users.stream()
                .filter(user -> user.getEmail().equals(username))
                .findFirst();
    }

    public void setUsers(final Set<Account> users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Account account = loadAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.withUsername(account.getEmail())
                .password(passwordEncoder.encode(account.getPassword()))
                .roles(account.getRoles()).build();
    }
}
