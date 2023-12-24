package com.safeway.userservice.sequrity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.safeway.userservice.dto.UserDetailsDao;
import com.safeway.userservice.entity.admin.Permission;
import com.safeway.userservice.entity.admin.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    private String mobile;

    private List<Role> roles;

    private List<Permission> permissions;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String mobile, String password,
                           List<Role> roles, List<Permission> permissions, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(UserDetailsDao user) {
        List<GrantedAuthority> authorities = List.of();//user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleCode())).collect(Collectors.toList());
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getMobile(), user.getPassword(), user.getRoles(), user.getPermissions(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}