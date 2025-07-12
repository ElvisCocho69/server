package com.api.server.persistence.entity.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.api.server.persistence.entity.order.Order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    private String lastname;

    private String email;

    private String contacto;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Order> orders;

    @Column(unique = true)
    private String username;

    private String password;  

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public static enum UserStatus {
        ENABLED, DISABLED;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role == null) return null;

        if(role.getPermissions() == null) return null;

        List<SimpleGrantedAuthority> authorities = role.getPermissions().stream()
            .map(each -> each.getOperation().getName())
            .map(each -> new SimpleGrantedAuthority(each))
            .collect(Collectors.toList());
        
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.getName()));
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
        return status == UserStatus.ENABLED;
    }    

}
