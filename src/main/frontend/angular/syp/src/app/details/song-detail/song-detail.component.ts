import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import { Song } from '../../interfaces/song';
import { SongService } from '../../services/song.service';
import { User } from 'src/app/interfaces/user';
import { userComment } from 'src/app/interfaces/userComment';
import { UserService } from 'src/app/services/user.service';
import { CommentService } from 'src/app/services/comment.service';

@Component({
  selector: 'app-song-detail',
  templateUrl: './song-detail.component.html',
  styleUrls: ['./song-detail.component.css']
})
export class SongDetailComponent implements OnInit {
  song: Song | undefined;
  snapshot: Song | undefined
  show: string | undefined
  userLoggedIn: User | undefined
  allowed: boolean | undefined
  showModifyForm: boolean | undefined

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private songService: SongService,
    private userService: UserService,
    private commentService: CommentService,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.getUserLoggedIn()
    this.getSong();
  }

  checkAllowed(): void{
    if(this.userLoggedIn?.isAdmin == true){
      this.allowed = true
      return
    }
    this.allowed = false
  }

  getUserLoggedIn(): void {
    var user = localStorage.getItem("userLoggedIn")
    if (user == null) {
      this.userLoggedIn = undefined
      return
    }
    this.userLoggedIn = JSON.parse(user)
  }

  getSong(): void {
    const id = this.route.snapshot.paramMap.get('id')
    this.songService.getSong(id!)
      .subscribe(song => {
        this.song = song
        this.show = 'playlists';
        this.checkAllowed()
      })
  }

  goBack(): void {
    this.location.back();
  }

  modifySong(): void{
    this.showForm()
    this.snapshot = Object.assign({}, this.song)
  }

  saveSong(): void {
    if (this.song) {
      this.songService.updateSong(this.snapshot!, this.song)
        .subscribe();
    }
    this.showForm()
  }

  setShow(show: string): void {
    this.show = show;
  }

  deleteSong(): void {
    this.songService.deleteSong(this.song!.id)
    this.router.navigateByUrl('/dashboard')
  }

  showForm(): void{
    this.showModifyForm = !this.showModifyForm
  }
}
