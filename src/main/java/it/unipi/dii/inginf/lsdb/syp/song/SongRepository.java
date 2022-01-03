package it.unipi.dii.inginf.lsdb.syp.song;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface SongRepository extends Neo4jRepository<Song, String> {
    @Query("MATCH (n:Song { identifier: $id }) SET n.title = $title")
    void updateTitle(String id, String title);

    void deleteSongByIdentifier(String id);

    @Query("CREATE (n:Song {identifier: $id, title: $title})")
    void insert(String id, String title);

    @Query("MATCH (n:Song) WITH COUNT(n) as NumberOfSongs MATCH ()-[r:RELATED]->() RETURN 1.0*COUNT(r)/NumberOfSongs")
    Double getAverageCommentsPerSong();

    @Query("MATCH (song)<-[:RELATED]-(comment) RETURN song, COUNT(comment) as numberOfComments ORDER BY numberOfComments DESC LIMIT $number")
    List<Song> getMostCommentedSongs(int number);
}
