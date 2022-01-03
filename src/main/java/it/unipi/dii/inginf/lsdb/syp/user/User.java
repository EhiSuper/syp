package it.unipi.dii.inginf.lsdb.syp.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unipi.dii.inginf.lsdb.syp.playlist.Playlist;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Date;
import java.util.List;


@Node("User")
@Document(collection="users")
public class User {

    @Id
    @org.springframework.data.neo4j.core.schema.Id
    private String identifier;
    private String firstName;
    private String secondName;
    private String password;
    private String username;
    private Boolean isAdmin;
    private Date dateOfCreation;

    private Date dateOfBirth;


    private List<Playlist> createdPlaylists;

    private Integer numberOfFollowers;
    private Integer numberOfPlaylists;
    private Integer numberOfSongsInPeriod;

    public User(String firstName, String password, String secondName, String username, Boolean isAdmin, Date dateOfCreation, Date dateOfBirth,
                List<Playlist> createdPlaylists, Integer numberOfFollowers, Integer numberOfPlaylists, Integer numberOfSongsInPeriod) {
        this.firstName = firstName;
        this.password = password;
        this.secondName = secondName;
        this.username = username;
        this.isAdmin = isAdmin;
        this.dateOfCreation = dateOfCreation;
        this.dateOfBirth = dateOfBirth;
        this.createdPlaylists = createdPlaylists;
        this.numberOfFollowers = numberOfFollowers;
        this.numberOfPlaylists = numberOfPlaylists;
        this.numberOfSongsInPeriod = numberOfSongsInPeriod;
    }

    @JsonProperty("id")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonProperty("playlistsCreated")
    public List<Playlist> getCreatedPlaylists() {
        return createdPlaylists;
    }

    public void setCreatedPlaylists(List<Playlist> createdPlaylists) {
        this.createdPlaylists = createdPlaylists;
    }

    public Integer getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public void setNumberOfFollowers(Integer numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    public Integer getNumberOfPlaylists() {
        return numberOfPlaylists;
    }

    public void setNumberOfPlaylists(Integer numberOfPlaylists) {
        this.numberOfPlaylists = numberOfPlaylists;
    }

    public Integer getNumberOfSongsInPeriod() {
        return numberOfSongsInPeriod;
    }

    public void setNumberOfSongsInPeriod(Integer numberOfSongsInPeriod) {
        this.numberOfSongsInPeriod = numberOfSongsInPeriod;
    }
}
