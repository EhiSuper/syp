import { Component, OnInit } from '@angular/core';
import { Playlist } from '../interfaces/playlist';
import { User } from '../interfaces/user';
import { PlaylistService } from '../services/playlist.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {
  playlists: Playlist[] = [];
  userLoggedIn: User | undefined;
  logged: boolean | undefined;
  allowed: boolean | undefined

  constructor(private playlistService: PlaylistService) { }

  ngOnInit(): void {
    this.checkLogin()
    this.checkAllowed()
    this.getPlaylists()
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
  }

  getPlaylists(): void {
    this.playlistService.getDashboardPlaylists()
      .subscribe(playlists => {
        this.playlists = playlists
      });
  }
}
