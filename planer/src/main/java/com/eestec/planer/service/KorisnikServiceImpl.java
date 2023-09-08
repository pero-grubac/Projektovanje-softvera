package com.eestec.planer.service;

import com.eestec.planer.controller.util.LoginForm;
import com.eestec.planer.dao.KorisnikDAO;
import com.eestec.planer.dao.TimDAO;
import com.eestec.planer.dto.KorisnikDTO;
import com.eestec.planer.dto.TimDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KorisnikServiceImpl implements KorisnikService {

    private KorisnikDAO korisnikDAO;
    private TimDAO timDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public KorisnikServiceImpl(KorisnikDAO korisnikDAO, TimDAO timDAO) {
        this.korisnikDAO = korisnikDAO;
        this.timDAO = timDAO;
    }

    @Override
    public List<KorisnikDTO> getAllKorisnici() {
        List<KorisnikDTO> korisnikDTOList = new ArrayList<KorisnikDTO>();
        korisnikDAO.findAll().forEach(korisnikDTOList::add);
        return korisnikDTOList;
    }

    @Override
    @Transactional
    public KorisnikDTO getKorisnik(Integer id) {
        KorisnikDTO korisnikDTO = korisnikDAO.findById(id).orElse(null);
        return korisnikDTO;
    }

    @Override
    @Transactional
    public KorisnikDTO createKorisnik(KorisnikDTO korisnikDTO) {
        String hash = passwordEncoder.encode(korisnikDTO.getLozinka());
        korisnikDTO.setLozinka(hash);
        if (korisnikDAO.save(korisnikDTO) != null)
            return korisnikDTO;
        else return null;
    }

    @Override
    @Transactional
    public KorisnikDTO updateKorisnik(KorisnikDTO korisnikDTO) {
        if (korisnikDTO != null) {
            String hash = passwordEncoder.encode(korisnikDTO.getLozinka());
            korisnikDTO.setLozinka(hash);
            KorisnikDTO korisnik = korisnikDAO.findById(korisnikDTO.getIdKorisnika()).orElse(null);
            if (korisnik != null) {
                korisnik.setLozinka((hash));
                korisnik.setIme(korisnikDTO.getIme());
                korisnik.setEmail(korisnikDTO.getEmail());
                korisnik.setPrezime(korisnikDTO.getPrezime());
                korisnik.setKorisnickoIme(korisnikDTO.getKorisnickoIme());
                korisnikDAO.save(korisnik);
                return korisnik;
            }
            return null;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteKorisnik(Integer id) {
        KorisnikDTO korisnik = korisnikDAO.findById(id).orElse(null);
        if (korisnik != null) {
            korisnikDAO.delete(korisnik);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean joinTim(int idKorisnik, int idTim) {
        TimDTO tim = timDAO.findById(idTim).orElse(null);
        KorisnikDTO korisnik = korisnikDAO.findById(idKorisnik).orElse(null);
        if (tim != null && korisnik != null) {
            korisnikDAO.joinTeam(idKorisnik, idTim);
            return true;
        }
        return false;

    }

    @Override
    public boolean leaveTim(int idKorisnik, int idTim) {
        TimDTO tim = timDAO.findById(idTim).orElse(null);
        KorisnikDTO korisnik = korisnikDAO.findById(idKorisnik).orElse(null);
        if (tim != null && korisnik != null) {
            korisnikDAO.leaveTeam(idKorisnik, idTim);
            return true;
        }
        return false;
    }

    @Override
    public boolean login(LoginForm loginForm) {
        Optional<KorisnikDTO> optionalKorisnikDTO=korisnikDAO.findBykorisnickoIme(loginForm.getUsername());
        if(optionalKorisnikDTO.isPresent()){
            KorisnikDTO korisnik = optionalKorisnikDTO.get();
            return  passwordEncoder.matches(loginForm.getLozinka(),korisnik.getLozinka());
        }
        return false;
    }
}
