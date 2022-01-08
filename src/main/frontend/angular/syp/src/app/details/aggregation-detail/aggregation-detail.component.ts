import { Component, OnInit } from '@angular/core';
import { Aggregation } from 'src/app/interfaces/aggregation';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { AggregationsComponent } from 'src/app/aggregations/aggregations.component';
import { PlaylistService } from 'src/app/services/playlist.service';

@Component({
  selector: 'app-aggregation-detail',
  templateUrl: './aggregation-detail.component.html',
  styleUrls: ['./aggregation-detail.component.css']
})
export class AggregationDetailComponent implements OnInit {

  results: string[] = []
  aggregation: Aggregation | undefined 
  aggregations: Aggregation[] = [
    {
      name: "How many songs has a playlist in average?",
      endpoint: "/api/playlists/averagesongs",
      access: "admin"
    },
    {
      name: "How many followers has a playlist in average?",
      endpoint: "/api/playlists/averagefollows",
      access: "admin"
    },
    {
      name: "How many artists contains a playlist in average?",
      endpoint: "",
      access: "admin"
    },
    {
      name: "How many comments has a song in average?",
      endpoint: "/api/songs/averagecomments",
      access: "admin"
    },
    {
      name: "On average a song in how many playlists is contained?",
      endpoint: "/api/songs/averageplaylists",
      access: "admin"
    },
    {
      name: "How many followers has a user in average?",
      endpoint: "/api/users/averagefollows",
      access: "admin"
    },
    {
      name: "How many playlists are followed by a user in average?",
      endpoint: "/api/users/averageplaylists",
      access: "admin"
    },
    {
      name: "How many songs a user comments in average?",
      endpoint: "/api/users/averagecomments",
      access: "admin"
    },
    {
      name: "How many playlists a user creates in average?",
      endpoint: "/api/users/averageplaylists",
      access: "admin"
    },
    {
      name: "Find the top k users that has created the highest number of playlists",
      endpoint: "/api/users/topcreators",
      parameters: ["number"],
      access: "admin"
    },
    {
      name: "Find the top k users that have added to them playlists the highest number of songs of a specific artist",
      endpoint: "/api/users/mostsongsofartist",
      parameters: ["number", "artist"],
      access: "user"
    },
    {
      name: "Find the k most popular songs (based on how many playlists contains that specific song)",
      endpoint: "/api/songs/popular",
      parameters: ["number"],
      access: "user"
    },
    {
      name: "Find the k most followed users",
      endpoint: "/api/users/mostfollowed",
      parameters: ["number"],
      access: "user"
    },
    {
      name: "Find the k most followed playlists",
      endpoint: "/api/playlists/mostfollowed",
      parameters: ["number"],
      access: "user"
    },
    {
      name: "Find the users that follows at least k same playlists of the User provided in input",
      endpoint: "/api/users/similar",
      parameters: ["playlists", "username"],
      access: "user"
    },
    {
      name: "Find the k songs that has the highest number of comments",
      endpoint: "/api/songs/mostcommented",
      parameters: ["number"],
      access: "admin"
    },
    {
      name: "Find the playlists followed by users that a specific user follows",
      endpoint: "/api/playlists/dashboard",
      parameters: ["number", "id"],
      access: "user"
    }
  ]

  constructor(private location: Location, private route: ActivatedRoute, private playlistService: PlaylistService) { }

  ngOnInit(): void {
    this.getAggregation()
  }

  getAggregation(): void{
    const name = this.route.snapshot.paramMap.get('name')!;
    for(var i=0; i<this.aggregations.length; i++){
      if(this.aggregations[i].name == name) this.aggregation = this.aggregations[i]
    }
    return undefined
  }

  goBack(): void {
    this.location.back();
  }

  getResult(): void{
    var endpoint: string = ""
    for(var i=0; i<this.aggregations.length; i++){
      if(this.aggregations[i].name == this.aggregation?.name) endpoint = this.aggregations[i].endpoint
    }
    this.playlistService.getResults(endpoint)
      .subscribe(results => {
        this.results = results
      })
  }

}
