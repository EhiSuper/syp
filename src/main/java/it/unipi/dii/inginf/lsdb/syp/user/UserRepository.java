package it.unipi.dii.inginf.lsdb.syp.user;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;


public interface UserRepository extends Neo4jRepository<User, String> {
    @Query("MATCH (n:User) WHERE n.identifier = $followerId MATCH (n2:User) WHERE n2.identifier = $followedId CREATE (n)-[:FOLLOWS]->(n2)")
    void addFollow(String followerId, String followedId);

    @Query("MATCH (n:User {identifier: $followerId})-[r:FOLLOWS]->(n2:User {identifier: $followedId}) DELETE r")
    void removeFollow(String followerId, String followedId);

    @Query("MATCH (n:User)<-[:FOLLOWS]-(followers) WHERE n.identifier = $id RETURN followers")
    List<User> getFollowers(String id);

    @Query("MATCH (n:User)-[:FOLLOWS]->(followed) WHERE n.identifier = $id RETURN followed")
    List<User> getFollowedUsers(String id);

    @Query("MATCH (n:User { identifier: $id }) SET n.username = $username")
    void updateNickname(String id, String username);

    @Query("CREATE (n:User {identifier: $id, username: $username})")
    void insert(String id, String username);

    void deleteUserByIdentifier(String id);

    @Query("MATCH (u1)-[:FOLLOWS]->(u2) RETURN u2, COUNT(u1) as numberOfFollowers ORDER BY numberOfFollowers DESC LIMIT $number")
    List<User> getMostFollowedUsers(int number);

    @Query("MATCH (n:User) WITH COUNT(n) as NumberOfUsers MATCH ()-[f:FOLLOWS]->() RETURN 1.0*COUNT(f)/NumberOfUsers")
    Double getAverageFollows();

    @Query("MATCH (n:User) WITH COUNT(n) as NumberOfUsers MATCH ()-[w:WROTE]->() RETURN 1.0*COUNT(w)/NumberOfUsers")
    Double getAverageCommentsPerUser();

    @Query( "MATCH path = ((n:User {identifier: $id})-[:LIKES]->(p)<-[:LIKES]-(users)) " +
            "WHERE NOT (n.identifier = users.identifier) " +
            "WITH DISTINCT users as possibleUsers, count(path) as numberOfSharedPlaylist " +
            "WHERE numberOfSharedPlaylist >= $numberOfPlaylist " +
            "RETURN possibleUsers ORDER BY numberOfSharedPlaylist DESC")
    List<User> getSimilarUsers(String id, int numberOfPlaylist);

    @Query("MATCH (p:Playlist)<-[:LIKES]-(followers) WHERE p.identifier = $id RETURN followers")
    List<User> getLikesById(String id);
}
