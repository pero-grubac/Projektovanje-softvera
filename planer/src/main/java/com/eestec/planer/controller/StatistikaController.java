package com.eestec.planer.controller;

import com.eestec.planer.service.StatistikaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = "http://localhost:3000")
public class StatistikaController {
    @Autowired
    private StatistikaService statistikaService;


    @GetMapping("/taskbymonth/{year}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getMonthyTasksByUserByYear(@PathVariable Integer year){
        return ResponseEntity.ok(statistikaService.mjesecniBrojZadatakaPoKorisniku(year));
    }
    @GetMapping("/numberofusers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getNumberOfUsers(){
        return   ResponseEntity.ok(statistikaService.brojKorisnika());
    }
    @GetMapping("/taksperuser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> tasksPerUser(){
        return  ResponseEntity.ok(statistikaService.brojZadatakaPoKorisniku());
    }
    @GetMapping("/taskbymonthbyteam/{year}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getMonthyTasksByTeamByYear(@PathVariable Integer year){
        return ResponseEntity.ok(statistikaService.mjesecniBrojZadatakaPoTimu(year));
    }
    @GetMapping("/taskbymonth/{userid}/{year}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getMonthyTasks(@PathVariable Integer userid,@PathVariable Integer year){
        return ResponseEntity.ok(statistikaService.mjesecniBrojZadatakaKorisnika(userid,year));
    }
    @GetMapping("/taskbymonthinteam/{teamid}/{year}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getMonthyTasksInTeam(@PathVariable Integer teamid,@PathVariable Integer year){
        return ResponseEntity.ok(statistikaService.mjesecniBrojZadatakaPoKorisnikuUnutarTima(teamid,year));
    }
}
