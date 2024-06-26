package com.eestec.planer.dao;


import com.eestec.planer.dto.KorisnikDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface KorisnikDAO extends JpaRepository<KorisnikDTO, Integer> {

    @Modifying
    @Query("UPDATE KorisnikDTO k SET k.ime = :ime, k.prezime = :prezime, k.korisnickoime = :korisnickoime, k.lozinka = :lozinka, k.email = :email WHERE k.IdKorisnika = :id")
    void updateKorisnik(@Param("ime") String ime, @Param("prezime") String prezime, @Param("korisnickoime") String korisnickoime, @Param("lozinka") String lozinka, @Param("email") String email, @Param("id") Integer id);

    @Modifying
    @Query(value = "INSERT INTO korisnik_pripada_timu (Korisnik_IdKorisnika, Tim_IdTim) " +
            "SELECT :idKorisnika AS Korisnik_IdKorisnika, :idTim AS Tim_IdTim " +
            "FROM dual " +
            "WHERE NOT EXISTS (" +
            "    SELECT 1 FROM korisnik_pripada_timu " +
            "    WHERE Korisnik_IdKorisnika = :idKorisnika AND Tim_IdTim = :idTim" +
            ")", nativeQuery = true)
    @Transactional
    void joinTeam(@Param("idKorisnika") Integer idKorisnika, @Param("idTim") Integer idTim);

    @Modifying
    @Query(value = "INSERT INTO korisnik_radi_zadatak (`Korisnik_IdKorisnika`, `Zadatak_IdZadatak`) VALUES (:idKorisnika,:idZadatka)", nativeQuery = true)
    @Transactional
    void assignTask(@Param("idKorisnika") Integer idKorisnika, @Param("idZadatka") Integer idZadatka);


    @Modifying
    @Query(value = "DELETE FROM korisnik_radi_zadatak " +
            "WHERE Korisnik_IdKorisnika = :idKorisnika AND Zadatak_IdZadatak = :idZadatak", nativeQuery = true)
    @Transactional
    void dropTask(@Param("idKorisnika") Integer idKorisnika, @Param("idZadatak") Integer idZadatak);

    @Query("SELECT k FROM KorisnikDTO k WHERE k.korisnickoime = :korisnickoime")
    Optional<KorisnikDTO> findBykorisnickoIme(@Param("korisnickoime") String korisnickoIme);

    @Query(value = "SELECT k.* FROM korisnik k " +
            "INNER JOIN korisnik_pripada_timu kt on k.IdKorisnika=kt.Korisnik_IdKorisnika " +
            "WHERE kt.Tim_IdTim =:idTim", nativeQuery = true)
    List<KorisnikDTO> getAllByIdTim(@Param("idTim") Integer idTim);

    @Query(value = "SELECT k.Obrisan FROM korisnik k WHERE k.KorisnickoIme =:username", nativeQuery = true)
    Byte isDeletedByUsername(@Param("username") String username);

    @Query(value = "SELECT k.* FROM korisnik k \n" +
            "INNER JOIN korisnik_pripada_timu kpt ON kpt.Korisnik_IdKorisnika = k.IdKorisnika \n" +
            "INNER JOIN kategorija kat ON kat.IdTim = kpt.Tim_IdTim \n" +
            "INNER JOIN zadatak z ON z.IdKategorija = kat.IdKategorija \n" +
            "WHERE z.IdZadatak = :id", nativeQuery = true)
    List<KorisnikDTO> userEmails(@Param("id") Integer id);
    @Query(value = "SELECT k.* FROM korisnik k \n" +
            "INNER JOIN korisnik_pripada_timu kpt ON kpt.Korisnik_IdKorisnika = k.IdKorisnika \n" +
            "WHERE kpt.Tim_IdTim = :id", nativeQuery = true)
    List<KorisnikDTO> getKorisniciByIdTeam(@Param("id") Integer id);
}