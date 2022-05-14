package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerServiceImpl;
import com.game.service.PlayerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class PlayerController {
    final PlayerSpecification playerSpecification;
    final PlayerServiceImpl service;

    @Autowired
    public PlayerController(PlayerSpecification playerSpecification, PlayerServiceImpl playerService) {
        this.playerSpecification = playerSpecification;
        this.service = playerService;
    }

    @PostMapping(value = "/players")
    public Player createPlayer(@RequestBody Player player) {
        return service.createPlayer(player);
    }

    @GetMapping(value = "/players/{id}")
    public Player getPlayer(@PathVariable Long id) {
        return service.findPlayerByID(id);
    }

    @PostMapping(value = "/players/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        return service.updatePlayer(id, player);
    }

    @DeleteMapping(value = "/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        service.deletePlayer(id);
    }

    @GetMapping(value = "/players")
    public List<Player> getPlayersList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race,
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer pageSize) {

        Specification<Player> spec = playerSpecification.playersFilter(
                name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return service.findAllPlayers(spec, pageable).getContent();
    }

    @GetMapping(value = "/players/count")
    public Integer getPlayersCount(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race,
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel) {

        Specification<Player> spec = playerSpecification.playersFilter(
                name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel);

        return service.findAllPlayers(spec).size();

    }
}
