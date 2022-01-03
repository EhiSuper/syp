import { Component, OnInit, Input } from '@angular/core';
import { Playlist } from '../interfaces/playlist';

import { User } from '../interfaces/user';
import { PlaylistService } from '../services/playlist.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-followers',
  templateUrl: './followers.component.html',
  styleUrls: ['./followers.component.css']
})

export class FollowersComponent implements OnInit {
  @Input() option: string | undefined // se option == playlist vuol dire che devo caricare i followers della playlist
  //se option == user devo caricare i followers di un user

  @Input() playlist: Playlist | undefined
  @Input() user: User | undefined
  followers: User[] = []

  constructor(private userService: UserService, private playlistService: PlaylistService) { }

  ngOnInit(): void {
    this.getFollowers()
  }

  getFollowers(): void {
    if (this.option == 'playlist') {
      this.playlistService.getFollowers(this.playlist!.id)
        .subscribe(followers => this.followers = followers)
    }
    if(this.option == 'followers') {
      this.userService.getFollowers(this.user!.id)
        .subscribe(followers => this.followers = followers)
    }
    if(this.option == 'followed') {
      this.userService.getFollowed(this.user!.id)
        .subscribe(followed => this.followers = followed)
    }
  }
}
