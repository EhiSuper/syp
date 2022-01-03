package it.unipi.dii.inginf.lsdb.syp.comment;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface CommentRepository extends Neo4jRepository <Comment, Long>{
    @Query("MATCH (s:Song {identifier:$id})<-[:RELATED]-(c:Comment)<-[w:WROTE]-(u:User) RETURN c,u,w")
    List<Comment> findCommentsBySongId(String id);

    @Query("MATCH (s:Song)<-[r:RELATED]-(c:Comment)<-[:WROTE]-(u:User {identifier:$id}) RETURN c,s,r")
    List<Comment> findCommentsByUserId(String id);
}
