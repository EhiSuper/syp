import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddPlaylistComponent } from '../add-playlist/add-playlist.component';
import { AddSongComponent } from '../add-song/add-song.component';
import { AggregationsComponent } from '../aggregations/aggregations.component';

import { DashboardComponent } from '../dashboard/dashboard.component';
import { AggregationDetailComponent } from '../details/aggregation-detail/aggregation-detail.component';
import { PlaylistDetailComponent } from '../details/playlist-detail/playlist-detail.component';
import { SongDetailComponent } from '../details/song-detail/song-detail.component';
import { UserDetailComponent } from '../details/user-detail/user-detail.component';
import { SubscribeComponent } from '../subscribe/subscribe.component';
import { LoginComponent } from '../utils/login/login.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'songs/:id', component: SongDetailComponent },
  { path: 'playlists/:id', component: PlaylistDetailComponent },
  { path: 'users/:id', component: UserDetailComponent},
  { path: 'login', component: LoginComponent},
  { path: 'addSong', component: AddSongComponent},
  { path: 'subscribe', component: SubscribeComponent},
  { path: 'addPlaylist', component: AddPlaylistComponent},
  { path: 'aggregations', component: AggregationsComponent},
  { path: 'aggregations/:name', component: AggregationDetailComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
