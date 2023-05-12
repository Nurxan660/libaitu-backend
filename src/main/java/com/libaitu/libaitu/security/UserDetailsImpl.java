package com.libaitu.libaitu.security;


import com.libaitu.libaitu.entity.Roles;
import com.libaitu.libaitu.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Integer userId;


    public static UserDetailsImpl build(User user){
        List<Roles> roles=List.of(user.getRole());
        List<GrantedAuthority> rolesForSpring=roles.stream()
                .map(r->new SimpleGrantedAuthority(r.getRole().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(user.getUsername(),user.getPassword(),rolesForSpring,user.getUserId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
