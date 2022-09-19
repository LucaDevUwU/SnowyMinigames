package com.snowy.snowyminigames.instance;

import com.google.common.collect.TreeMultimap;
import com.snowy.snowyminigames.GameState;
import com.snowy.snowyminigames.SnowyMinigame;
import com.snowy.snowyminigames.kit.Kit;
import com.snowy.snowyminigames.kit.KitType;
import com.snowy.snowyminigames.kit.type.FighterKit;
import com.snowy.snowyminigames.kit.type.MinerKit;
import com.snowy.snowyminigames.manager.ConfigManager;
import com.snowy.snowyminigames.team.Team;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Arena {

    private SnowyMinigame minigame;

    private int id;
    private Location spawn;

    private GameState state;
    private List<UUID> players;
    private HashMap<UUID, Team> teams;
    private HashMap<UUID, Kit> kits;
    private Countdown countdown;
    private Game game;
    private boolean canJoin;

    public Arena(SnowyMinigame minigame, int id, Location spawn) {
        this.minigame = minigame;

        this.id = id;
        this.spawn = spawn;

        setState(GameState.RECRUITING);
        this.players = new ArrayList<>();
        this.teams = new HashMap<>();
        this.kits = new HashMap<>();
        this.countdown = new Countdown(minigame, this);
        this.game = new Game(this);
        this.canJoin = true;
    }

    /* GAME */
    public void start() { game.start(); }

    public void reset() {
        if (state == GameState.LIVE) {
            this.canJoin = false;
            Location loc = ConfigManager.getLobbySpawn();
            for (UUID uuid: players) {
                Player player = Bukkit.getPlayer(uuid);
                player.teleport(loc);
                removeKit(player.getUniqueId());
            }
            players.clear();
            teams.clear();

            String worldName = spawn.getWorld().getName();
            Bukkit.unloadWorld(spawn.getWorld(), false);
            World world = Bukkit.createWorld(new WorldCreator(worldName));
            world.setAutoSave(false);
        }

        kits.clear();
        sendTitle("", "");
        state = GameState.RECRUITING;
        countdown.cancel();
        countdown = new Countdown(minigame, this);
        game = new Game(this);
    }

    /* TOOLS */
    public void sendMessage(String message) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    public void sendTitle(String title, String subtitle) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendTitle(title, subtitle);
        }
    }

    /* PLAYERS */
    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        player.teleport(spawn);

        player.sendMessage(ChatColor.GOLD + "Choose your kit with /arena kit!");

        TreeMultimap<Integer, Team> count = TreeMultimap.create();
        for (Team team : Team.values()) {
            count.put(getTeamCount(team), team);
        }

        Team lowest = (Team) count.values().toArray()[0];
        setTeam(player, lowest);

        player.sendMessage(ChatColor.AQUA + "You have been automatically placed on " + lowest.getDisplay() + ChatColor.AQUA + " team.");

        if (state.equals(GameState.RECRUITING) && players.size() >= ConfigManager.getRequiredPlayer()) {
            countdown.start();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        player.teleport(ConfigManager.getLobbySpawn());
        player.sendTitle("", "");

        removeTeam(player);

        removeKit(player.getUniqueId());

        if (state == GameState.COUNTDOWN && players.size() < ConfigManager.getRequiredPlayer()) {
            sendMessage(ChatColor.RED + "There is not enough players. Countdown Stopped");
            reset();
            return;
        }

        if (state == GameState.LIVE && players.size() < ConfigManager.getRequiredPlayer()) {
            sendMessage(ChatColor.RED + "The game has ended as too many players have left");
            reset();
        }
    }

    /* INFO */
    public int getId() { return id; }
    public World getWorld() { return spawn.getWorld(); }

    public GameState getState() { return state; }
    public List<UUID> getPlayers() { return players; }
    public Game getGame() { return game; }
    public boolean canJoin() { return canJoin; }
    public void toggleCanJoin() { this.canJoin = !canJoin; }

    public void setState(GameState state) { this.state = state; }
    public HashMap<UUID, Kit> getKits() { return kits; }
    public void setTeam(Player player, Team team) {
        removeTeam(player);
        teams.put(player.getUniqueId(), team);
    }

    public void removeTeam(Player player) {
        if (teams.containsKey(player.getUniqueId())) {
            teams.remove(player.getUniqueId());
        }
    }

    public int getTeamCount(Team team) {
        int amount = 0;
        for (Team t : teams.values()) {
            if (t == team) {
                amount++;
            }
        }
        return amount;
    }

    public Team getTeam(Player player) { return teams.get(player.getUniqueId()); }

    public void removeKit(UUID uuid) {
        if (kits.containsKey(uuid)) {
            kits.get(uuid).remove();
            kits.remove(uuid);
        }
    }

    public void setKit(UUID uuid, KitType type) {
        removeKit(uuid);
        if (type == KitType.MINER) {
            kits.put(uuid, new MinerKit(minigame, uuid));
        } else if (type == KitType.FIGHTER) {
            kits.put(uuid, new FighterKit(minigame, uuid));
        }
    }

    public KitType getKitType(Player player) {
        return kits.containsKey(player.getUniqueId()) ? kits.get(player.getUniqueId()).getType() : null;
    }
}