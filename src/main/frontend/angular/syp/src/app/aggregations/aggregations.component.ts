import { identifierName } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { Aggregation } from '../interfaces/aggregation';

@Component({
  selector: 'app-aggregations',
  templateUrl: './aggregations.component.html',
  styleUrls: ['./aggregations.component.css']
})
export class AggregationsComponent implements OnInit {

  aggregations: Aggregation[] = [
    {
      name: "How many songs has a playlist in average?",
      endpoint: "/api/playlists/averagesongs"
    },
    {
      name: "How many followers has a playlist in average?",
      endpoint: "/api/playlists/averagefollows"
    },
    {
      name: "How many artists contains a playlist in average?",
      endpoint: ""
    },
    {
      name: "How many comments has a song in average?",
      endpoint: "/api/songs/averagecomments"
    },
    {
      name: "On average a song in how many playlists is contained?",
      endpoint: "/api/songs/averageplaylists"
    },
    {
      name: "How many followers has a user in average?",
      endpoint: "/api/users/averagefollows"
    },
    {
      name: "How many playlists are followed by a user in average?",
      endpoint: "/api/users/averageplaylists"
    },
    {
      name: "How many songs a user comments in average?",
      endpoint: ""
    },
    {
      name: "How many playlists a user creates in average?",
      endpoint: ""
    },
    {
      name: "Find the top k users that has created the highest number of playlists",
      endpoint: "/api/users/topcreators",
      parameters: ["number"]
    },
    {
      name: "Find the top k users that have added to them playlists the highest number of songs of a specific artist",
      endpoint: "/api/users/mostsongsofartist",
      parameters: ["number", "artist"]
    },
    {
      name: "Find the k most popular songs (based on how many playlists contains that specific song)",
      endpoint: "/api/songs/popular",
      parameters: ["number"]
    },
    {
      name: "Find the k most followed users",
      endpoint: "/api/users/mostfollowed",
      parameters: ["number"]
    },
    {
      name: "Find the k most followed playlists",
      endpoint: "/api/playlists/mostfollowed",
      parameters: ["number"]
    },
    {
      name: "Find the users that follows at least k same playlists of the User provided in input",
      endpoint: "/api/users/similar",
      parameters: ["playlists", "userId"]
    },
    {
      name: "Find the k songs that has the highest number of comments",
      endpoint: "/api/songs/mostcommented",
      parameters: ["number"]
    },
    {
      name: "Find the playlists followed by users that a specific user follows",
      endpoint: "/api/playlists/dashboard",
      parameters: ["number", "id"]
    }
  ]

  constructor() { }

  ngOnInit(): void {
  }

}
