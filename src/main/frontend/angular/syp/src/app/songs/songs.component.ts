import { Component, OnInit, Input } from '@angular/core';
import { Playlist } from '../interfaces/playlist';
import { Song } from '../interfaces/song';
import { findIndex } from 'rxjs';

@Component({
  selector: 'app-songs',
  templateUrl: './songs.component.html',
  styleUrls: ['./songs.component.css']
})

export class SongsComponent implements OnInit {
  @Input() playlist: Playlist | undefined
  @Input() songs: Song[] = [];
  @Input() allowed: boolean | undefined
  @Input() showModifyForm: boolean | undefined

  constructor() { }

  ngOnInit(): void {

  }

  deleteSongFromPlaylist(song: Song): void{
    var index = this.findIndex(song.id)
    this.playlist?.songs.splice(index, 1)
  }

  findIndex(songId: string): number{
    for(var i=0; i<this.playlist!.songs.length; i++){
      if(this.playlist!.songs[i].id == songId) return i
    }
    return -1
  }
}
