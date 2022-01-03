package it.unipi.dii.inginf.lsdb.syp.user;

import it.unipi.dii.inginf.lsdb.syp.playlist.Playlist;
import it.unipi.dii.inginf.lsdb.syp.song.Song;
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
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.IfNull.ifNull;

@Service
public class UserService {
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;

    UserService(MongoTemplate mongoTemplate, UserRepository neo4jUserRepository) {
        this.mongoTemplate = mongoTemplate;
        this.userRepository = neo4jUserRepository;
    }

    List<User> getUsersByRegex(String regex){
        Query findUsersByRegex = new Query(Criteria.where("username").regex("^" + regex, "i"));
        return mongoTemplate.find(findUsersByRegex, User.class);
    }

    User getUserById(String id){
        Query findUserById = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(findUserById, User.class);
    }

    User saveUser(User newUser){
        User savedUser = mongoTemplate.insert(newUser);
        //consistency between graph and document DBs
        userRepository.insert(savedUser.getIdentifier(), savedUser.getUsername());
        return savedUser;
    }

    User updateUser(User oldUser, User newUser){
        User savedUser = mongoTemplate.save(newUser); //overwrites the entire document

        if(!oldUser.getUsername().equals(newUser.getUsername())){
            Query findPlaylists = new Query(Criteria.where("creator._id").is(new ObjectId(savedUser.getIdentifier())));
            Update updatePlaylists = new Update().set("creator.username", savedUser.getUsername());
            mongoTemplate.updateMulti(findPlaylists, updatePlaylists, Playlist.class);
            userRepository.updateNickname(newUser.getIdentifier(), newUser.getUsername());
        }

        return savedUser;
    }

    void addFollow(String followerId, String followedId){
        userRepository.addFollow(followerId, followedId);
    }

    void removeFollow(String followerId, String followedId){
        userRepository.removeFollow(followerId, followedId);
    }

    void deleteUser(String id){
        //update redundant info
        Query findUserById = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(findUserById, User.class);

        Query findPlaylistsByCreatorId = new Query(Criteria.where("creator._id").is(new ObjectId(id)));
        List<Playlist> deletedPlaylists = mongoTemplate.findAllAndRemove(findPlaylistsByCreatorId, Playlist.class);
        for(Playlist playlist: deletedPlaylists ){
            deleteRedundantInfo(playlist);
        }
        userRepository.deleteUserByIdentifier(id);
    }

    private void deleteRedundantInfo(Playlist playlist) {
        Playlist embeddedPlaylistInfo = new Playlist(playlist.getIdentifier(), playlist.getName(),
                null, null, null, null);

        List<String> songsIdentifiers = getIdentifiersFromPlaylist(playlist);

        if(songsIdentifiers != null){
            Query findSongs = new Query(Criteria.where("_id").in(songsIdentifiers));
            Update updateSongs = new Update().pull("playlists", embeddedPlaylistInfo);
            mongoTemplate.updateMulti(findSongs, updateSongs, Song.class);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////DA SPOSTARE IN UTILS//////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

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


    public List<User> getFollowersById(String id) {
        return userRepository.getFollowers(id);
    }

    public List<User> getFollowedUsersById(String id) {
        return userRepository.getFollowedUsers(id);
    }

    public List<User> getTopCreators(int numberToReturn) {
        MatchOperation firstFilterStage = match(new Criteria("createdPlaylists").exists(true));
        ProjectionOperation projectStage = project("_id", "username").and("createdPlaylists").size().as("numberOfPlaylists");
        MatchOperation secondFilterStage = match(new Criteria("numberOfPlaylists").gt(0));
        SortOperation sortStage = sort(Sort.by(Sort.Direction.DESC, "numberOfPlaylists"));

        Aggregation aggregation = newAggregation(firstFilterStage, projectStage, secondFilterStage, sortStage, limit(numberToReturn));
        AggregationResults<User> result = mongoTemplate.aggregate(
                aggregation, "users", User.class);
        return result.getMappedResults();
    }

    public Double getAverageCreatedPlaylistsPerUser() {
        ProjectionOperation fProjectStage = project("_id", "username").and("createdPlaylists")
                .applyCondition(ifNull("createdPlaylists").then(new ArrayList<>()));
        ProjectionOperation sProjectStage = project("_id", "username").and("createdPlaylists").size().as("numberOfPlaylists");
        GroupOperation groupStage = group().avg("numberOfPlaylists").as("avgNumberOfPlaylistsCreated");

        Aggregation aggregation = newAggregation(fProjectStage, sProjectStage, groupStage);
        AggregationResults<Document> result = mongoTemplate.aggregate(
                aggregation, "users", Document.class);
        Document document = result.getUniqueMappedResult();
        return document.getDouble("avgNumberOfPlaylistsCreated");
    }

    public List<User> getMostFollowedUsers(int number){
        return userRepository.getMostFollowedUsers(number);
    }

    public Double getAverageFollows(){
        return userRepository.getAverageFollows();
    }

    public Double getAverageCommentsPerUser(){
        return userRepository.getAverageCommentsPerUser();
    }

    public List<User> getSimilarUsers(String id, int numberOfPlaylist) {
        return userRepository.getSimilarUsers(id, numberOfPlaylist);
    }

    public List<User> getUsersWithMostSongsWithinAPeriod(int numberToReturn, String artist) {
        MatchOperation firstFilterStage = match(new Criteria("songs").exists(true));
        ProjectionOperation fProjectStage = project("_id", "creator").and(filter("songs")
                .as("songs").by(
                        ComparisonOperators.valueOf("songs.artist").equalToValue(artist))).as("songs");
        ProjectionOperation tProjectStage = project("_id", "creator").and("songs").size().as("numberOfSongsInPeriod");
        GroupOperation groupStage = group("creator._id").first("creator.username").as("username").sum("numberOfSongsInPeriod").as("numberOfSongsInPeriod");
        SortOperation sortStage = sort(Sort.by(Sort.Direction.DESC, "numberOfSongsInPeriod"));
        Aggregation aggregation = newAggregation(firstFilterStage, fProjectStage, tProjectStage, groupStage, sortStage, limit(numberToReturn));
        AggregationResults<User> result = mongoTemplate.aggregate(
                aggregation, "playlists", User.class);
        return result.getMappedResults();
    }

    public List<User> getLikesById(String id) {
        return userRepository.getLikesById(id);
    }
}
