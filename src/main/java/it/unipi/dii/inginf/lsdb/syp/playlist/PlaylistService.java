package it.unipi.dii.inginf.lsdb.syp.playlist;

import it.unipi.dii.inginf.lsdb.syp.song.Song;
import it.unipi.dii.inginf.lsdb.syp.user.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.IfNull.ifNull;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final MongoTemplate mongoTemplate;

    PlaylistService(PlaylistRepository neo4jPlaylistRepository, MongoTemplate mongoTemplate) {
        this.playlistRepository = neo4jPlaylistRepository;
        this.mongoTemplate = mongoTemplate;
    }

    List<Playlist> getPlaylistsByRegex(String regex){
        Query findPlaylistsByRegex = new Query(Criteria.where("name").regex("^" + regex, "i"));
        return mongoTemplate.find(findPlaylistsByRegex, Playlist.class);
    }

    Playlist getPlaylistById(String id){
        Query findPlaylistById = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(findPlaylistById, Playlist.class);
    }

    Playlist savePlaylist(Playlist newPlaylist){
        //prepare fields to put in mongodb
        Playlist savedPlaylist = mongoTemplate.insert(newPlaylist);

        Playlist embeddedPlaylistInfo = new Playlist(savedPlaylist.getIdentifier(), savedPlaylist.getName(),
                                            null, null, null, null);

        Query findCreator = new Query(Criteria.where("_id").is(savedPlaylist.getCreator().getIdentifier()));
        Update updateCreator = new Update().push("createdPlaylists", embeddedPlaylistInfo);
        mongoTemplate.updateFirst(findCreator, updateCreator, User.class);

        List<String> songsIdentifiers = getIdentifiersFromPlaylist(savedPlaylist);
        if(songsIdentifiers != null){
            Query findSongs = new Query(Criteria.where("_id").in(songsIdentifiers));
            Update updateSongs = new Update().push("playlists", embeddedPlaylistInfo);
            mongoTemplate.updateMulti(findSongs, updateSongs, Song.class);
        }

        //consistency between graph and document DBs
        playlistRepository.insert(savedPlaylist.getIdentifier(), savedPlaylist.getName());
        return savedPlaylist;
    }

    public Playlist updatePlaylist(Playlist oldPlaylist, Playlist newPlaylist) {
        List<String> removedSongsIdentifiers = getSongsOnlyInFirstPlaylist(oldPlaylist, newPlaylist);

        List<String> insertedSongsIdentifiers = getSongsOnlyInFirstPlaylist(newPlaylist, oldPlaylist);

        if(removedSongsIdentifiers != null){
            Playlist embeddedOldPlaylistInfo = new Playlist(oldPlaylist.getIdentifier(), oldPlaylist.getName(),
                                                                null, null, null, null);

            Query findSongs = new Query(Criteria.where("_id").in(removedSongsIdentifiers));
            Update updateSongs = new Update().pull("playlists", embeddedOldPlaylistInfo);
            mongoTemplate.updateMulti(findSongs, updateSongs, Song.class);
        }

        if(insertedSongsIdentifiers != null){
            Playlist embeddedNewPlaylistInfo = new Playlist(newPlaylist.getIdentifier(), newPlaylist.getName(),
                                                                null, null, null, null);

            Query findSongs = new Query(Criteria.where("_id").in(insertedSongsIdentifiers));
            Update updateSongs = new Update().push("playlists", embeddedNewPlaylistInfo);
            mongoTemplate.updateMulti(findSongs, updateSongs, Song.class);
        }

        Playlist savedPlaylist = mongoTemplate.save(newPlaylist);

        if (!oldPlaylist.getName().equals(newPlaylist.getName()))
            updateName(newPlaylist);

        return savedPlaylist;
    }

    void updateName(Playlist playlist){
        Query findCreator = new Query(Criteria.where("createdPlaylists._id").is(new ObjectId(playlist.getIdentifier())));
        Update updateCreator = new Update().set("createdPlaylists.$.name", playlist.getName());
        mongoTemplate.updateFirst(findCreator, updateCreator, User.class);


        Query findSongs = new Query(Criteria.where("playlists._id").is(new ObjectId(playlist.getIdentifier())));
        Update updateSongs = new Update().set("playlists.$.name", playlist.getName());
        mongoTemplate.updateMulti(findSongs, updateSongs, Song.class);

        playlistRepository.updateTitle(playlist.getIdentifier(), playlist.getName());
    }

    List<String> getSongsOnlyInFirstPlaylist(Playlist firstPlaylist, Playlist secondPlaylist){
        List<String> firstSongsIdentifiers = new ArrayList<>();
        for (Song song : firstPlaylist.getSongs()) {
            firstSongsIdentifiers.add(song.getIdentifier());
        }
        if(firstSongsIdentifiers.isEmpty()){
            return null;
        }

        List<String> secondSongsIdentifiers = new ArrayList<>();
        for (Song song : secondPlaylist.getSongs()) {
            secondSongsIdentifiers.add(song.getIdentifier());
        }
        if(secondSongsIdentifiers.isEmpty()){
            return firstSongsIdentifiers;
        }

        firstSongsIdentifiers.removeAll(secondSongsIdentifiers);
        return firstSongsIdentifiers;
    }

    public List<String> getIdentifiersFromPlaylist(Playlist playlist){
        if(playlist.getSongs() != null) {
            List<String> songsIdentifiers = new ArrayList<>();
            for (Song song : playlist.getSongs()) {
                songsIdentifiers.add(song.getIdentifier());
            }
            return songsIdentifiers;
        }
        return null;
    }

    public void deletePlaylist(String id) {
        Query findPlaylistById = new Query(Criteria.where("_id").is(id));
        Playlist deletedPlaylist = mongoTemplate.findAndRemove(findPlaylistById, Playlist.class);
        Playlist embeddedPlaylistInfo = new Playlist(deletedPlaylist.getIdentifier(), deletedPlaylist.getName(),
                                                                   null, null, null, null);

        Query findCreator = new Query(Criteria.where("_id").is(deletedPlaylist.getCreator().getIdentifier()));
        Update updateCreator = new Update().pull("createdPlaylists", embeddedPlaylistInfo);
        mongoTemplate.updateFirst(findCreator, updateCreator, User.class);

        List<String> songsIdentifiers = getIdentifiersFromPlaylist(deletedPlaylist);

        if(songsIdentifiers != null){
            Query findSongs = new Query(Criteria.where("_id").in(songsIdentifiers));
            Update updateSongs = new Update().pull("playlists", embeddedPlaylistInfo);
            mongoTemplate.updateMulti(findSongs, updateSongs, Song.class);
        }

        playlistRepository.deletePlaylistByIdentifier(deletedPlaylist.getIdentifier());
    }

    public void addPlaylistFollow(String userId, String playlistId) {
        playlistRepository.addPlaylistFollow(userId, playlistId);
    }

    public void removePlaylistFollow(String userId, String playlistId) {
        playlistRepository.removePlaylistFollow(userId, playlistId);
    }

    public List<Playlist> getFollowedPlaylists(String userId) {
        return playlistRepository.getFollowedPlaylists(userId);
    }

    public Double getAverageSongsContained() {
        ProjectionOperation fProjectStage = project("_id", "name").and("songs")
                .applyCondition(ifNull("songs").then(new ArrayList<>()));
        ProjectionOperation sProjectStage = project("_id", "name").and("songs").size().as("numberOfSongs");
        GroupOperation groupStage = group().avg("numberOfSongs").as("avgNumberOfSongs");

        Aggregation aggregation = newAggregation(fProjectStage, sProjectStage, groupStage);
        AggregationResults<Document> result = mongoTemplate.aggregate(
                aggregation, "playlists", Document.class);
        Document document = result.getUniqueMappedResult();
        return document.getDouble("avgNumberOfSongs");
    }

    public List<Playlist> getMostFollowedPlaylists(int number){
        return playlistRepository.getMostFollowedPlaylists(number);
    }

    public Double getAverageFollowsPerPlaylist(){
        return playlistRepository.getAverageFollowsPerPlaylist();
    }
}
