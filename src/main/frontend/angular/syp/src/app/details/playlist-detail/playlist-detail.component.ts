import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import { Playlist } from '../../interfaces/playlist';
import { PlaylistService } from '../../services/playlist.service';
import { User } from 'src/app/interfaces/user';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-playlist-detail',
  templateUrl: './playlist-detail.component.html',
  styleUrls: ['./playlist-detail.component.css']
})
export class PlaylistDetailComponent implements OnInit {
  playlist: Playlist | undefined
  snapshot: Playlist | undefined
  show: string | undefined
  userLoggedIn: User | undefined
  followed: boolean | undefined
  allowed: boolean | undefined
  showModifyForm: boolean | undefined

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private playlistService: PlaylistService,
    private userService: UserService,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.getUserLoggedIn()
    this.getPlaylist();
  }

  checkAllowed(): void{
    if(!this.userLoggedIn){
      this.allowed = false
      return
    }
    if(this.userLoggedIn?.isAdmin == true){
      this.allowed = true
      return
    }
    else{
      if(this.playlist?.creator.username == this.userLoggedIn.username){
        this.allowed = true
        return
      }
      else this.allowed = false
    }
  }

  getUserLoggedIn(): void {
    var user = localStorage.getItem("userLoggedIn")
    if (user == null) {
      this.userLoggedIn = undefined
      return
    }
    this.userLoggedIn = JSON.parse(user)
  }

  checkFollowed(): void {
    if (!this.userLoggedIn) return
    if(!this.userLoggedIn.playlistsFollowed){
      this.followed = false
      return
    }
    for (var i = 0; i < this.userLoggedIn!.playlistsFollowed!.length; i++) {
      if (this.userLoggedIn?.playlistsFollowed![i].name == this.playlist?.name) {
        this.followed = true
        return
      }
    }
    this.followed = false
  }

  updateUserLoggedIn(): void {
    localStorage.removeItem("userLoggedIn")
    const jsonData = JSON.stringify(this.userLoggedIn);
    localStorage.setItem('userLoggedIn', jsonData);
    this.getUserLoggedIn()
  }

  follow(): void {
    if(!this.userLoggedIn?.playlistsFollowed){
      this.userLoggedIn!.playlistsFollowed = []
    }
    this.userLoggedIn?.playlistsFollowed!.push(this.playlist!)
    this.updateUserLoggedIn()
    this.playlistService.likePlaylist(this.userLoggedIn!.id, this.playlist!.id)
    this.followed = true
  }

  findIndexPlaylist(array: Array<Playlist>, playlist: Playlist){
    var index = 0
    for(var i=0; i<array.length; i++){
      if(array[i].name == playlist.name) return i
    }
    return -1
  }

  unfollow(): void {
    var index = this.findIndexPlaylist(this.userLoggedIn?.playlistsFollowed!, this.playlist!)
    this.userLoggedIn?.playlistsFollowed!.splice(index!, 1)
    this.updateUserLoggedIn()
    this.playlistService.dislikePlaylist(this.userLoggedIn!.id, this.playlist!.id)
    this.followed = false
  }

  getPlaylist(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.playlistService.getPlaylist(id)
      .subscribe(playlist => {
        this.playlist = playlist
        this.checkFollowed()
        this.checkAllowed()
        this.show  = 'songs'
      });
  }

  goBack(): void {
    this.location.back();
  }

  modifyPlaylist(): void{
    this.showForm()
    this.snapshot = Object.assign({}, this.playlist)
    this.snapshot!.songs = []
    this.playlist?.songs.forEach(val => this.snapshot!.songs.push(Object.assign({}, val)));
  }

  savePlaylist(): void {
    if (this.playlist) {
      this.playlistService.updatePlaylist(this.snapshot!, this.playlist)
        .subscribe();
    }
    this.showForm()
  }

  setShow(show: string): void {
    this.show = show;
  }

  deletePlaylist(): void {
    this.playlistService.deletePlaylist(this.playlist!.id)
    this.router.navigateByUrl('/dashboard')
  }

  showForm(): void{
    this.showModifyForm = !this.showModifyForm
  }
}
