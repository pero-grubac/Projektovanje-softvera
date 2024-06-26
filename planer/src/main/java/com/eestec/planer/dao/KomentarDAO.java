package com.eestec.planer.dao;

import com.eestec.planer.dto.KomentarDTO;
import com.eestec.planer.dto.KorisnikDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KomentarDAO extends JpaRepository<KomentarDTO, Integer> {

    public List<KomentarDTO> findAllByIdZadatak(Integer idZadatak);
    public List<KomentarDTO> findAllByIdZadatakAndKorisnik(Integer idZadatak, KorisnikDTO korisnik);

}
