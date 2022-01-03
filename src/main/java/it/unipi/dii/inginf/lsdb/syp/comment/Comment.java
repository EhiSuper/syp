package it.unipi.dii.inginf.lsdb.syp.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.unipi.dii.inginf.lsdb.syp.song.Song;
import it.unipi.dii.inginf.lsdb.syp.user.User;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;

@Node("Comment")
public class Comment {
    @Id @GeneratedValue
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm", timezone = "default")
    private Date date;
    private Integer vote;
    private String body;
    @Relationship(type = "WROTE", direction = Relationship.Direction.INCOMING)
    private User user;
    @Relationship(type = "RELATED", direction = Relationship.Direction.OUTGOING)
    private Song song;

    public Comment(Long id, Date date, Integer vote, String body, User user, Song song) {
        this.id = id;
        this.date = date;
        this.vote = vote;
        this.body = body;
        this.user = user;
        this.song = song;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
