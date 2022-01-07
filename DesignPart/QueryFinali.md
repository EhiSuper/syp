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
        2) how many followers has a playlist in avg? --- GRAPH 
        3) how many artists contains a playlist in avg? --- MONGO 

    Songs 
        1) How many comments has a song in avg? --- GRAPH 
        2) On average a song in how many playlists is contained? --- MONGO 

    User
        1) How many followers has a user in average? --- GRAPH 
        2) How many playlists are followed by a user in average? --- GRAPH
        3) How many songs a user comments in avg? --- GRAPH 
        4) How many playlists a user creates in average? --- MONGO


Aggregations for MongoDB
    1) Find the top k users that has created the highest number of playlists. --Aggregation / Admin 
    2) Find the top k users that have added to them playlists the highest number of songs of a specific artist. --Aggregation/User
    3) Find the k most popular songs (based on how many playlist contains that specific song) --Dashboard songs


On-graph queries for GraphDB
    1) Find the k most followed users  -- Dashboard users
    2) Find the k most followed playlists -- Dashboard playlist not logged
    3) Find the users that follows at least k same playlists of the User provided in input Aggregation/ Admin
    4) Find the k songs that has the highest number of comments -- Aggregation/admin
    5) Find k playlists followed by users that a specific user follows --Dashboard playlist logged

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



