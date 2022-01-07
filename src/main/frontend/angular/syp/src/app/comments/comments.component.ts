import { CommaExpr, identifierName } from '@angular/compiler';
import { Component, OnInit, Input } from '@angular/core';
import { Song } from '../interfaces/song';
import { User } from '../interfaces/user';
import { userComment } from '../interfaces/userComment';
import { CommentService } from '../services/comment.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})

export class CommentsComponent implements OnInit {
  @Input() song: Song | undefined
  @Input() user: User | undefined
  @Input() option: string | undefined
  @Input() userLoggedIn: User | undefined

  comments: userComment[] = []
  modifyForm: number | undefined
  newCommentVote: string | undefined
  newCommentBody: string | undefined
  commented: boolean | undefined
  showCommentForm: boolean | undefined
  avgVote: string | undefined

  constructor(private commentService: CommentService, private userService: UserService) { }

  ngOnInit(): void {
    this.getComments()
  }

  getComments(): void {
    if (this.option == 'user') {
      this.commentService.getUserComments(this.user!.id)
        .subscribe(comments => {
          this.comments = comments
          if (!this.userLoggedIn) return
          if (this.userLoggedIn.comments == undefined) this.getUserLoggedInComments()
          else this.checkCommented()
          this.getAvgVote()
        })
    }
    if (this.option == 'song') {
      this.commentService.getSongComments(this.song!.id)
        .subscribe(comments => {
          this.comments = comments
          if (!this.userLoggedIn) return
          if (this.userLoggedIn.comments == undefined) this.getUserLoggedInComments()
          else this.checkCommented()
          this.getAvgVote()
        })
    }
  }

  getAvgVote(): void {
    var sum: number = 0
    if (this.comments.length == 0) return
    for (var i = 0; i < this.comments.length; i++) {
      sum = sum + parseFloat(this.comments[i].vote)
    }
    this.avgVote = (sum / this.comments.length).toFixed(2)
  }

  getUserLoggedInComments(): void {
    this.commentService.getUserComments(this.userLoggedIn!.id)
      .subscribe(comments => {
        this.userLoggedIn!.comments = comments
        this.updateUserLoggedIn()
        this.checkCommented()
      })
  }

  checkCommented() {
    for (var i = 0; i < this.userLoggedIn!.comments!.length; i++) {
      if (this.userLoggedIn?.comments![i].song?.id == this.song?.id) {
        this.commented = true
        return
      }
    }
    this.commented = false
  }

  addComment(): void {
    var user = <User>{}
    user.id = this.userLoggedIn!.id
    user.username = this.userLoggedIn!.username
    var song = <Song>{}
    song.id = this.song!.id
    song.track = this.song!.track
    var vote = this.newCommentVote
    var voteNum = parseFloat(vote!)
    if (voteNum > 5 || voteNum < 0) {
      window.alert("The vote has to be between 0 and 5")
      return
    }
    var body = this.newCommentBody
    var date = new Date()
    this.commentService.addComment({ user, song, vote, body, date } as userComment)
      .subscribe(comment => {
        if (!this.userLoggedIn?.comments)
          this.userLoggedIn!.comments = []
        this.userLoggedIn?.comments?.push(comment)
        this.updateUserLoggedIn()
        this.getComments()
      })
    this.commented = true
    this.showCommentForm = false
  }

  updateUserLoggedIn(): void {
    localStorage.removeItem("userLoggedIn")
    const jsonData = JSON.stringify(this.userLoggedIn);
    localStorage.setItem('userLoggedIn', jsonData);
    this.getUserLoggedIn()
  }

  getUserLoggedIn(): void {
    var user = localStorage.getItem("userLoggedIn")
    if (user == null) {
      this.userLoggedIn = undefined
      return
    }
    this.userLoggedIn = JSON.parse(user)
  }

  saveComment(comment: userComment): void {
    if (comment) {
      if (this.option == 'user') {
        var user = <User>{}
        user.id = this.userLoggedIn!.id
        user.username = this.userLoggedIn!.username
        comment.user = user
      }
      if (this.option == 'song') {
        var song = <Song>{}
        song.id = this.song!.id
        song.track = this.song!.track
        comment.song = song
      }
      var voteNum = parseFloat(comment.vote)
      if (voteNum > 5 || voteNum < 0) {
        window.alert("The vote has to be between 0 and 5")
        return
      }
      this.commentService.updateComment(comment)
        .subscribe(_ => this.getComments());
    }
    this.modifyForm = undefined
  }

  deleteComment(comment: userComment): void {
    this.comments = this.comments.filter(h => h !== comment);
    var index = this.findCommentIndex(comment.id)
    this.userLoggedIn?.comments?.splice(index, 1)
    this.updateUserLoggedIn()
    this.commentService.deleteComment(comment.id)
    this.getComments()
  }

  findCommentIndex(commentId: number): number {
    for (var i = 0; i < this.userLoggedIn!.comments!.length; i++) {
      if (this.userLoggedIn?.comments![0].id == commentId) return i
    }
    return -1
  }

  showModifyForm(id: number): void {
    if (this.modifyForm == undefined) this.modifyForm = id
    else this.modifyForm = undefined
  }

  checkAllowed(comment: userComment): boolean{
    if(this.userLoggedIn?.isAdmin) return true
    for(var i=0 ; i<this.userLoggedIn!.comments!.length; i++){
      if(this.userLoggedIn!.comments![i].id == comment.id) return true
    }
    return false
  }
}


