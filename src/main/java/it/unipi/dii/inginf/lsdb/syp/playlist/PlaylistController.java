package it.unipi.dii.inginf.lsdb.syp.playlist;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class PlaylistController {
    private final PlaylistService playlistService;

    PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/api/playlists")
    List<Playlist> getPlaylistsByRegex(@RequestParam(value="name", defaultValue = "") String regex) {
        return playlistService.getPlaylistsByRegex(regex);
    }

    @GetMapping("/api/playlists/{id}")
    Playlist getPlaylistById(@PathVariable(value="id") String id) {
        return playlistService.getPlaylistById(id);
    }

    @GetMapping("/api/users/playlistsfollowed/{id}")
    List<Playlist> getFollowedPlaylists(@PathVariable(value="id") String userId) {
        return playlistService.getFollowedPlaylists(userId);
    }

    @GetMapping("/api/playlists/averagesongs")
    Double getAverageSongsContained() {
        return playlistService.getAverageSongsContained();
    }

    @GetMapping("/api/playlists/mostfollowed")
    List<Playlist> getMostFollowedPlaylists(@RequestParam(value="number", defaultValue = "5") String number) {
        return playlistService.getMostFollowedPlaylists(Integer.parseInt(number));
    }

    @GetMapping("/api/playlists/averagefollows")
    Double getAverageFollowsPerPlaylist() {
        return playlistService.getAverageFollowsPerPlaylist();
    }

    @PostMapping("/api/playlists")
    Playlist savePlaylist(@RequestBody Playlist newPlaylist){
        Playlist savedPlaylist = playlistService.savePlaylist(newPlaylist);
        return savedPlaylist;
    }

    @PutMapping("/api/playlists")
    Playlist updatePlaylist(@RequestBody List<Playlist> playlists){
        Playlist updatedPlaylist = playlistService.updatePlaylist(playlists.get(0), playlists.get(1));
        return updatedPlaylist;
    }

    @GetMapping("/api/like")
    void addPlaylistFollow(@RequestParam(value="userId") String userId, @RequestParam(value="playlistId") String playlistId){
        playlistService.addPlaylistFollow(userId, playlistId);
    }

    @GetMapping("/api/dislike")
    void removePlaylistFollow(@RequestParam(value="userId") String userId, @RequestParam(value="playlistId") String playlistId){
        playlistService.removePlaylistFollow(userId, playlistId);
    }

    @DeleteMapping("/api/playlists/{id}")
    void deletePlaylist(@PathVariable(value="id") String id){
        playlistService.deletePlaylist(id);
    }
}
