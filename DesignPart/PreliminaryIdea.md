Main query 
Data una canzone -> Resituisci le playlist che la contengono e quante sono --    Which and how many are the playlists that contains a specific song? 
Data una canzone -> Restiuisci i commenti relativi ad essa 
Data una canzone -> Restituisci voto medio alla canzone 
Data una canzone -> Resituisci il testo di essa

Dato un user -> Restiuisci le playlist create e seguite (in due tab diversi)
Dato un user -> Resituisci tutti i commenti fatti da quell'utente 

Data una playlist -> Restituisci canzoni all'interno
Data una playlist -> Restituisci il voto medio della playlist 
Data una playlist -> Restituisci la liste degli user che la seguono
Data una playlist -> Restituisci il creatore di essa


Attori 
User normali -> user che hanno un'interazione base con la piattaforma
User amministratori -> User che hanno completo accesso alla piattaforma

Principali funzionalità
User normali = Creano, modificano ed eliminano le proprie playlist, seguono altri utenti, seguono altre playlist, commentare le canzoni, votare le canzoni e playlist
User admin = Eliminare i commenti, Eliminare/Modificare gli utenti, aggiungere/eliminare/modificare canzoni, eliminare/modificare playlist

Dataset Description

Source: Kaggle (Spotify playlists) / Data scraping from Genius (utenti e commenti) / Random generated vote (playlist and songs)
Description: 
Volume: Jo
Variety: Kaggle (Spotify playlists) / Data scraping from Genius (utenti e commenti) / Random generated vote (playlist and songs)
Velocity: A lot of playlist are often created 


Classi 

Utenti: 
    - id_utente
    - 

Canzoni:

Playlist: 

Commenti


Requirements and entities handled by document db = Tutte le informazioni relative all'entità 

Requirements and entities handled by graph db = Gestisce le relazioni tra le entità 

Software Architecture Preliminary Idea
- Linguaggio programmazione = Java 
- Frontend = Console or JavaFX/JavaWebApplication
- Backend = 
    - MongoDb
    - neo4J





ESEMPIO MONGODB

{
    _userID: ...,
    username: ...,
    name: ...,
    surname: ...,
    date_of_birth: ...,
    email: ...,
    password: ...,
    date_of_creation: ...,
    playlist_created: [ 
        {
            _playlistID: ...,
            name: ...
            songs_number: ...,
            date_of_creation: ...,
        },
        ...
    ]
}

{
    _songID: ...,
    name: ...,
    author: ...,
    album: ...,
    date: ...,
    lyrics: ...,
    duration_time: ...,
    avg_vote: ...,
    playlist_that_contains_it: [ 
        {
           _playlistID: ...,
            name: ...
            user_id: ..., 
        },
        ...
    ]
}

{
    _playlistID: ...,
    name: ...
    songs_number: ...,
    date_of_creation: ...,
    songs: [ 
        {
            _songID: ...,
            name: ...,
            author: ...,
            album: ...,
            duration_time: ...
        },
        ...
    ]
}


ESEMPIO GRAPH_DB

User →: FOLLOW → User, which represents a user following another in the application, and is created/removed when a User adds/removes another User from his followed users list. This relationship has no attributes.

User →: FOLLOW → Playlist, which represents a user following a playlist of the application and is created when a User follows a playlist. This relationship has no attributes.

User →: WRITES → Comment, which represents a user comment posted on a song within the application and is created when a User post a comment. This relationship has no attributes.

Comment → :RELATED → Song, which represents a comment related on a song, and is created when a User post a comment on that song. This relationship has no attributes.




