package com.eestec.planer.config;

import com.eestec.planer.dto.KorisnikDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class KorisnikInfoDetails implements UserDetails {


    private String korisnickoIme;
    private String lozinka;
    private List<GrantedAuthority> authorities;

    public KorisnikInfoDetails(KorisnikDTO korisnikDTO) {
        korisnickoIme = korisnikDTO.getKorisnickoIme();
        lozinka = korisnikDTO.getLozinka();
        authorities = Arrays.stream(korisnikDTO.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return lozinka;
    }

    @Override
    public String getUsername() {
        return korisnickoIme;
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
