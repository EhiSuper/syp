import { Injectable } from '@angular/core';
import { InMemoryDbService } from 'angular-in-memory-web-api';
import { Playlist } from './interfaces/playlist';
import { Song } from './interfaces/song';
import { User } from './interfaces/user';

@Injectable({
  providedIn: 'root',
})
export class InMemoryDataService implements InMemoryDbService {
  createDb() {
    const playlists = [
      { 
        id: 11, 
        name: 'Hard Rock 2021',
        songs: [ { id:31, name:"Hell Bells"},{id:37,name:"Highway to hell"}],
        followers: [ { id:31, name:"Antonio", username:"antonio"}, { id:32, name:"Fabio", username:"fabio"}]
      },
      { 
        id: 12, 
        name: 'Natale 2021',
        songs: [{id: 32, name:"Gingle Bell"},{ id:34, name:"Merry Christmas"}],
        followers: [ {id:33, name:"Lorenzo", username:"lorenzo"}, { id:31, name:"Antonio", username:"antonio"}, { id:32, name:"Fabio", username:"fabio"} ]
      },
      { id: 13, name: 'Chill music' },
      { id: 14, name: 'Top 2021' },
      { 
        id: 15, 
        name: 'Best Playlist Ever',
        songs: [ {id:33, name:"Happy Birthday"}, {id:35, name:"Sono fuori dal tunnel"}],
        followers: []
      },
      { id: 16, name: 'Happy Playlist' },
      { id: 17, name: 'Indie songs' },
      { id: 18, name: 'My favourite Playlist' },
      { id: 19, name: 'Top songs 2021' },
      { id: 20, name: '105 Playlist' }
    ];

    const songs = [
      { id: 31,
        name: 'Hell Bells',
        avgVote: 4.5,
        lyric: "I'm rolling thunder, a pouring rain I'm comin' on like a hurricane There's lightning's flashing across the sky You're only young, but you're gonna die...",
        playlists: [{ id:11, name:"Hard Rock 2021"}],
        comments: [{id:1, songId: 31, vote: 5, user:31, body:"Great song"}, {id:2, songId:31, vote:4, user:32, body:"Pure Rock"}]
      },
      { id: 32,
        name: 'Gingle Bell',
        avgVote: 5,
        lyric: "Jingle bells, jingle bells, jingle all the way Oh, what fun it is to ride in a one horse open sleigh",
        playlists: [{id:12, name:"Natale 2021"}],  
        comments: [{id:3, songId:32, vote: 5, user:33, body:"Happy Christmas"}, {id:4, songId:32, vote:5, user:34, body:"The bells are gingling"}]
      },
      { id: 33, name: 'Happy Birthday' },
      { id: 34, name: 'Merry Christmas' },
      { id: 35, name: 'Sono fuori dal tunnel' },
      { id: 36, name: 'Roar' },
      { id: 37, name: 'Highway to hell' },
      { id: 38, name: 'Universe' },
      { id: 39, name: 'Father and son' },
      { id: 40, name: 'My song' }
    ];

    const users = [
      { id: 31,
        name: 'Antonio',
        username: 'antonio',
        password: 'antonio',
        isAdmin: true,
        playlistsCreated: [{id: 11, name:"Hard Rock 2021"}],
        playlistsFollowed: [{id: 12, name:"Natale 2021"}, {id: 11, name:"Hard Rock 2021"}],
        comments: [{id:1, songId:31, vote: 5, user:31, body:"Great song"}],
        followers: [ {id:33, name:"Lorenzo", username:"lorenzo"}, { id:34, name:"Marco", username:"marco"}],
        followed: [ {id:32, name:"Fabio", username:"fabio"}, { id:35, name:"Pietro", username:"pietro"}]
      },
      { id: 32,
        name: 'Fabio',
        username: 'fabio',
        password: 'fabio',
        isAdmin: true,
        playlistsCreated: [{id: 12, name:"Natale 2021"}],
        playlistsFollowed: [{id: 11, name:"Hard Rock 2021"}, {id: 12, name:"Natale 2021"}],
        comments: [{id:2, songId:31, vote:4, user:32, body:"Pure Rock"}],
        followers: [ {id:31, name:"Antonio", username:"antonio"}, { id:34, name:"Marco", username:"marco"}],
        followed: [ {id:31, name:"Antonio", username:"antonio"}, { id:35, name:"Pietro", username:"pietro"}]   
      },
      { id: 33,
        name: 'Lorenzo',
        username: 'lorenzo',
        password: 'lorenzo',
        isAdmin: false,
        playlistsCreated: [{id: 15, name: 'Best Playlist Ever'}],
        playlistsFollowed: [{id: 12, name:"Natale 2021"}],
        comments: [{id:3, songId:32, vote: 5, user:33, body:"Happy Christmas"}],
        followers: [],
        followed: []   
      },
      { id: 34, name: 'Marco', username:"marco", comments: [{id:4, songId:32, vote:5, user:34, body:"The bells are gingling"}]},
      { id: 35, name: 'Pietro', username:"pietro" },
      { id: 36, name: 'Giulia', username:"giulia" },
      { id: 37, name: 'Rose', username:"rose" },
      { id: 38, name: 'Sky', username:"sky" },
      { id: 39, name: 'Filippo', username:"filippo" },
      { id: 40, name: 'Sean', usename:"filippo" }
    ];
    
    return {playlists, songs, users};
  }

  // Overrides the genId method to ensure that a hero always has an id.
  // If the heroes array is empty,
  // the method below returns the initial number (11).
  // if the heroes array is not empty, the method below returns the highest
  // hero id + 1.
  genId(playlists: Playlist[]): number {
    return playlists.length > 0 ? Math.max(...playlists.map(playlist => playlist.id)) + 1 : 11;
  }

  // Overrides the genId method to ensure that a hero always has an id.
  // If the heroes array is empty,
  // the method below returns the initial number (11).
  // if the heroes array is not empty, the method below returns the highest
  // hero id + 1.
  genIdsongs(songs: Song[]): number {
    return songs.length > 0 ? Math.max(...songs.map(song => song.id)) + 1 : 11;
  }

  genIusers(users: User[]): number {
    return users.length > 0 ? Math.max(...users.map(user => user.id)) + 1 : 11;
  }
  
}
