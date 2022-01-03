Basic Query

    Find a user, playlist, song starting from a string ( ex: song name, playlist name, user username )

    Given a song:
        1) Retrive the playlist that contains it 
        2) Retrive the comments 
        3) Retrive the avg vote 
        4) Retrive the lyrics 

    Give a user:
        1) Retrive playlist created
        2) Retrive the playlist followed
        3) Retrive the comments of the user 
        4) Retrive the followers of the user
        5) Retrieve the users that this user follow

    Give a playlist:
        1) Retrive the songs contained in it
        2) Retrive the list of user that follows it 

Analytics and Statistics

    Playlist 
        1) how many songs has a playlist in avg? --- MONGO
        2) how many followers follows a playlist in avg? --- GRAPH
        3) how many artists contains a playlist in avg? --- MONGO 

    Songs 
        1) Avg vote of a songs --- MONGO 
        2) How many comments has a song in avg? --- GRAPH 
        3) On average a song in how many playlists is contained? --- MONGO 

    User
        1) How many users follows a user in avg? --- GRAPH 
        2) How many playlists are followed by a user in avg? --- GRAPH
        3) How many song a user comments in avg? --- GRAPH 
        4) How many playlist are created by a user in avg? --- MONGO


Aggregations for MongoDB
    1) Find the top k users that has created the highest number of playlists.
    2) Find the top k users that have added to them playlist the highest number of song of a specific period.
    3) Find the k most popular songs (based on how many playlist contains that specific song)
    4) Find the k playlist that has the highest rating based on the songs that it contains.


On-graph queries for GraphDB
    1) Find the most k followed users 
    2) Find the most k followed playlists
    3) Find the users that follows at least k same playlists of the User provided in input
    4) Find the k songs that has the highest number of comments 

Playlist:
  {
    id: string
    name: string -
    creationDate: date -
    creator: User 
    songs: Song[] -
  }

User:
{
    id: string
    username: string
    password: string
    isAdmin: boolean
    dateOfCreation: date
    dateOfBirth: date
    createdPlaylists: Playlist[]
}

Song:
{
    id: string
    track: string -
    artist: string -
    year: string -
    lyric: string -
    album: string -
    playlists: Playlist[] 
}
<<<<<<< HEAD
=======




>>>>>>> origin/modification/css



