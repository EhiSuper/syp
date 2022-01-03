import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
//import { HttpClientInMemoryWebApiModule } from 'angular-in-memory-web-api';
//import { InMemoryDataService } from './in-memory-data.service';
import { FormsModule } from '@angular/forms';


import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing/app-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SearchBarComponent } from './search-bar/search-bar.component';
import { PlaylistDetailComponent } from './details/playlist-detail/playlist-detail.component';
import { SongDetailComponent } from './details/song-detail/song-detail.component';
import { UserDetailComponent } from './details/user-detail/user-detail.component';
import { SongsComponent } from './songs/songs.component';
import { FollowersComponent } from './followers/followers.component';
import { PlaylistsComponent } from './playlists/playlists.component';
import { CommentsComponent } from './comments/comments.component';
import { LoginComponent } from './utils/login/login.component';
import { AddSongComponent } from './add-song/add-song.component';
import { SubscribeComponent } from './subscribe/subscribe.component';
import { AddPlaylistComponent } from './add-playlist/add-playlist.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    SearchBarComponent,
    PlaylistDetailComponent,
    SongDetailComponent,
    UserDetailComponent,
    SongsComponent,
    FollowersComponent,
    PlaylistsComponent,
    CommentsComponent,
    LoginComponent,
    AddSongComponent,
    SubscribeComponent,
    AddPlaylistComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    /*
    // The HttpClientInMemoryWebApiModule module intercepts HTTP requests
    // and returns simulated server responses.
    // Remove it when a real server is ready to receive requests.
    HttpClientInMemoryWebApiModule.forRoot(
      InMemoryDataService, { dataEncapsulation: false }
    ),
    */
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
