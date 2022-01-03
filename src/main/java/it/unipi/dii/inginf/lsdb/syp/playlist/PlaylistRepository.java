package it.unipi.dii.inginf.lsdb.syp.playlist;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface PlaylistRepository extends Neo4jRepository<Playlist, String> {
    @Query("MATCH (n:User) WHERE n.identifier = $userId MATCH (p:Playlist) WHERE p.identifier = $playlistId CREATE (n)-[:LIKES]->(p)")
    void addPlaylistFollow(String userId, String playlistId);

    @Query("MATCH (n:User {identifier: $userId})-[r:LIKES]->(p:Playlist {identifier: $playlistId}) DELETE r")
    void removePlaylistFollow(String userId, String playlistId);

    @Query("MATCH (n:User)-[:LIKES]->(followedplaylists) WHERE n.identifier = $id RETURN followedplaylists")
    List<Playlist> getFollowedPlaylists(String id);

    @Query("CREATE (n:Playlist {identifier: $id, name: $name})")
    void insert(String id, String name);

    void deletePlaylistByIdentifier(String id);

    @Query("MATCH (n:Playlist { identifier: $id }) SET n.name = $name")
    void updateTitle(String id, String name);

    @Query("MATCH (u1)-[:LIKES]->(p) RETURN p, COUNT(u1) as numberOfFollowers ORDER BY numberOfFollowers DESC LIMIT $number")
    List<Playlist> getMostFollowedPlaylists(int number);

    @Query("MATCH (n:Playlist) WITH COUNT(n) as NumberOfPlaylists MATCH ()-[f:LIKES]->() RETURN 1.0*COUNT(f)/NumberOfPlaylists")
    Double getAverageFollowsPerPlaylist();
}
