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
        try{
            Query findSongsByRegex = new Query(Criteria.where("track").regex("^" + regex, "i"));
            return mongoTemplate.find(findSongsByRegex, Song.class);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    Song getSongById(String id){
        try{
            Query findSongById = new Query(Criteria.where("_id").is(id));
            return mongoTemplate.findOne(findSongById, Song.class);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    Song saveSong(Song newSong){
        Song savedSong = null;
        try{
            mongoTemplate.insert(newSong);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        try{
            songRepository.insert(savedSong.getIdentifier(), savedSong.getTrack());
        } catch (Exception e){
            e.printStackTrace();
            Query findSongById = new Query(Criteria.where("_id").is(savedSong.getIdentifier()));
            mongoTemplate.remove(findSongById, Song.class);
            return null;
        }

        return savedSong;
    }

    Song updateSong(Song oldSong, Song newSong){
        Song updatedSong = null;

        try{
            updatedSong = mongoTemplate.save(newSong);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        if(!oldSong.getTrack().equals(newSong.getTrack()) || !oldSong.getArtist().equals(newSong.getArtist())){

            try{
                songRepository.updateTrack(updatedSong.getIdentifier(), updatedSong.getTrack());
            } catch (Exception e){
                e.printStackTrace();
                mongoTemplate.save(oldSong);
                return null;
            }

            Query findPlaylists = new Query(Criteria.where("songs._id").is(new ObjectId(updatedSong.getIdentifier())));
            Update updatePlaylists = new Update().set("songs.$.track", updatedSong.getTrack())
                                                 .set("songs.$.artist", updatedSong.getArtist());
            mongoTemplate.updateMulti(findPlaylists, updatePlaylists, Playlist.class);
        }

        return updatedSong;
    }

    void deleteSong(String id){
        Song deletedSong = null;
        try{
            Query findSongById = new Query(Criteria.where("_id").is(id));
            deletedSong = mongoTemplate.findAndRemove(findSongById, Song.class);
        } catch (Exception e){
            e.printStackTrace();
            //return null;
            return;
        }

        try{
            songRepository.deleteSongByIdentifier(id);
        } catch (Exception e){
            e.printStackTrace();
            mongoTemplate.insert(deletedSong);
            //return null
            return;
        }

        Query findPlaylists = new Query(Criteria.where("songs._id").is(new ObjectId(id)));
        Update updatePlaylists = new Update().set("songs.$.track", "this song has been removed by an admin");
        mongoTemplate.updateMulti(findPlaylists, updatePlaylists, Playlist.class);
    }

    public List<Song> getPopularSongs(int numberToReturn) {
        MatchOperation filterSongsWithoutAttributePlaylists = match(new Criteria("playlists").exists(true));
        ProjectionOperation getNumberOfPlaylists = project("_id", "track").and("playlists").size().as("numberOfPlaylists");
        MatchOperation filterSongsContainedInNoPlaylists = match(new Criteria("numberOfPlaylists").gt(0));
        SortOperation sortByNumberOfPlaylists = sort(Sort.by(Sort.Direction.DESC, "numberOfPlaylists"));

        Aggregation aggregation = newAggregation(filterSongsWithoutAttributePlaylists,
                                                 getNumberOfPlaylists,
                                                 filterSongsContainedInNoPlaylists,
                                                 sortByNumberOfPlaylists,
                                                 limit(numberToReturn));

        try{
            AggregationResults<Song> result = mongoTemplate.aggregate(
                    aggregation, "songs", Song.class);
            return result.getMappedResults();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Song> getMostCommentedSongs(int number) {
        try{
            return songRepository.getMostCommentedSongs(number);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Double getAveragePlaylistsPerSong() {
        ProjectionOperation projectEmptyArrayInSongsWithoutPlaylists = project("_id", "track").and("playlists")
                .applyCondition(ifNull("playlists").then(new ArrayList<>()));
        ProjectionOperation getNumberOfPlaylists = project("_id", "track").and("playlists").size().as("numberOfPlaylists");
        GroupOperation groupAllAndGetAveragePlaylists = group().avg("numberOfPlaylists").as("avgNumberOfPlaylists");

        Aggregation aggregation = newAggregation(projectEmptyArrayInSongsWithoutPlaylists,
                                                 getNumberOfPlaylists,
                                                 groupAllAndGetAveragePlaylists);

        try{
            AggregationResults<Document> result = mongoTemplate.aggregate(
                    aggregation, "songs", Document.class);

            Document document = result.getUniqueMappedResult();
            return document.getDouble("avgNumberOfPlaylists");
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Double getAverageCommentsPerSong(){
        try{
            return songRepository.getAverageCommentsPerSong();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
