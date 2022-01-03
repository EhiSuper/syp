package it.unipi.dii.inginf.lsdb.syp.song;

import it.unipi.dii.inginf.lsdb.syp.playlist.Playlist;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.IfNull.ifNull;

@Service
public class SongService {
    private final MongoTemplate mongoTemplate;
    private final SongRepository songRepository;

    SongService(MongoTemplate mongoTemplate, SongRepository songRepository) {
        this.mongoTemplate = mongoTemplate;
        this.songRepository = songRepository;
    }

    List<Song> getSongsByRegex(String regex){
        Query findSongsByRegex = new Query(Criteria.where("track").regex("^" + regex, "i"));
        return mongoTemplate.find(findSongsByRegex, Song.class);
    }

    Song getSongById(String id){
        Query findSongById = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(findSongById, Song.class);
    }

    Song saveSong(Song newSong){
        Song savedSong = mongoTemplate.insert(newSong);
        //consistency between graph and document DBs
        songRepository.insert(savedSong.getIdentifier(), savedSong.getTrack());
        return savedSong;
    }

    Song updateSong(Song oldSong, Song newSong){
        Song updatedSong = mongoTemplate.save(newSong);

        if(!oldSong.getTrack().equals(newSong.getTrack()) || !oldSong.getArtist().equals(newSong.getArtist())){
            Query findPlaylists = new Query(Criteria.where("songs._id").is(new ObjectId(updatedSong.getIdentifier())));
            Update updatePlaylists = new Update().set("songs.$.track", updatedSong.getTrack())
                                                 .set("songs.$.artist", updatedSong.getArtist());
            mongoTemplate.updateMulti(findPlaylists, updatePlaylists, Playlist.class);
        }

        songRepository.updateTitle(updatedSong.getIdentifier(), updatedSong.getTrack());
        return updatedSong;
    }

    void deleteSong(String id){
        Query findSongById = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(findSongById, Song.class);

        Query findPlaylists = new Query(Criteria.where("songs._id").is(new ObjectId(id)));
        Update updatePlaylists = new Update().set("songs.$.track", "this song has been removed by an admin");
        mongoTemplate.updateMulti(findPlaylists, updatePlaylists, Playlist.class);

        songRepository.deleteSongByIdentifier(id);
    }

    public List<Song> getPopularSongs(int numberToReturn) {
        MatchOperation firstFilterStage = match(new Criteria("playlists").exists(true));
        ProjectionOperation projectStage = project("_id", "track").and("playlists").size().as("numberOfPlaylists");
        MatchOperation secondFilterStage = match(new Criteria("numberOfPlaylists").gt(0));
        SortOperation sortStage = sort(Sort.by(Sort.Direction.DESC, "numberOfPlaylists"));

        Aggregation aggregation = newAggregation(firstFilterStage, projectStage, secondFilterStage, sortStage, limit(numberToReturn));
        AggregationResults<Song> result = mongoTemplate.aggregate(
                aggregation, "songs", Song.class);
        return result.getMappedResults();
    }

    public Double getAveragePlaylistsPerSong() {
        ProjectionOperation fProjectStage = project("_id", "track").and("playlists")
                .applyCondition(ifNull("playlists").then(new ArrayList<>()));
        ProjectionOperation sProjectStage = project("_id", "track").and("playlists").size().as("numberOfPlaylists");
        GroupOperation groupStage = group().avg("numberOfPlaylists").as("avgNumberOfPlaylists");

        Aggregation aggregation = newAggregation(fProjectStage, sProjectStage, groupStage);
        AggregationResults<Document> result = mongoTemplate.aggregate(
                aggregation, "songs", Document.class);
        Document document = result.getUniqueMappedResult();
        return document.getDouble("avgNumberOfPlaylists");
    }

    public Double getAverageCommentsPerSong(){
        return songRepository.getAverageCommentsPerSong();
    }

    public List<Song> getMostCommentedSongs(int number) {
        return songRepository.getMostCommentedSongs(number);
    }
}
