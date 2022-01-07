import { Component, OnInit } from '@angular/core';
import { Playlist } from '../interfaces/playlist';
import { Song } from '../interfaces/song';
import { User } from '../interfaces/user';
import { PlaylistService } from '../services/playlist.service';
import { SongService } from '../services/song.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {
  playlists: Playlist[] | undefined = [];
  songs: Song[] | undefined = []
  users: User[] | undefined = []
  userLoggedIn: User | undefined;
  logged: boolean | undefined;
  allowed: boolean | undefined
  option: string | undefined = 'playlist'

  constructor(private playlistService: PlaylistService, private songService: SongService, private userService: UserService) { }

  ngOnInit(): void {
    this.checkLogin()
    this.checkAllowed()
    this.changeOption('playlist')
  }

  checkLogin(): void {
    var user = localStorage.getItem("userLoggedIn");
    if (user == null) {
      this.logged = false;
      return;
    }
    this.userLoggedIn = JSON.parse(user);
    this.logged = true;
  }

  checkAllowed(): void {
    if (this.userLoggedIn?.isAdmin == true) {
      this.allowed = true
      return
    }
    this.allowed = false
  }

  logout(): void {
    localStorage.removeItem('userLoggedIn');
    this.logged = false
    this.allowed = false
    this.changeOption('playlist')
  }

  changeOption(option: string) {
    this.option = option
    if (option == 'playlist' && this.logged == false) {
      this.playlistService.getTopPlaylists()
        .subscribe(playlists => {
          this.playlists = playlists
        })
    }
    if(option == 'playlist' && this.logged == true){
      this.playlistService.getSuggestedPlaylists(this.userLoggedIn!.id)
        .subscribe(playlists => {
          this.playlists = playlists
        })
    }
    if(option == 'song'){
      this.songService.getTopSongs()
        .subscribe(songs => {
          this.songs = songs
        })
    }
    if(option == 'user'){
      this.userService.getTopUsers()
        .subscribe(users => {
          this.users = users
        })
    }
  }
}
