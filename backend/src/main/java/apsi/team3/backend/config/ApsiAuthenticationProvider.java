package apsi.team3.backend.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.model.User;
import apsi.team3.backend.services.UserService;

@Component
public class ApsiAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;

    @Autowired
    public ApsiAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        return userService.getUserByLogin(login)
            .filter(user -> {
                try {
                    var hash = UserService.hashPassword(password, user.getSalt());
                    return Objects.equals(hash, user.getHash());
                }
                catch (ApsiException e){
                    return false;
                }
            })
            .map(user -> new UsernamePasswordAuthenticationToken(user, password, getAuthorities(user)))
            .orElse(null);
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        var type = user.getType().toString();
        var list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(type));
        return list;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}