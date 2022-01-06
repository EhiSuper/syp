import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import { User } from '../../interfaces/user';
import { UserService } from '../../services/user.service';
import {newArray} from "@angular/compiler/src/util";

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  user: User | undefined;
  snapshot: User | undefined
  show: string | undefined
  userLoggedIn: User | undefined
  followed: boolean | undefined
  myAccount: boolean | undefined
  allowed: boolean | undefined
  showModifyForm: boolean | undefined

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.getUserLoggedIn()
    this.route.paramMap.subscribe(params => {
      var id = this.route.snapshot.paramMap.get("id")!;
      this.getUser(id)
    });
  }

  checkAllowed(): void{
    if(this.userLoggedIn?.isAdmin == true){
      this.allowed = true
      return
    }
    else{
      if(this.userLoggedIn?.username == this.user?.username){
        this.allowed = true
        return
      }
      this.allowed = false
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

  getUserLoggedInFollowed(): void{
    this.userService.getFollowed(this.userLoggedIn!.id)
      .subscribe(followed => {
        this.userLoggedIn!.followed = followed
        this.updateUserLoggedIn()
      })
  }

  checkFollowed(): void {
    if (!this.userLoggedIn) return
    if(this.userLoggedIn.followed == undefined) this.getUserLoggedInFollowed()
    if(!this.userLoggedIn.followed){
      this.followed = false;
      return
    }
    for (var i = 0; i < this.userLoggedIn!.followed!.length; i++) {
      if (this.userLoggedIn?.followed![i].username == this.user?.username) {
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
    if(!this.userLoggedIn?.followed){
      this.userLoggedIn!.followed = []
    }
    this.userLoggedIn?.followed!.push(this.user!)
    this.updateUserLoggedIn()
    this.userService.follow(this.userLoggedIn!.id, this.user!.id)
    this.followed = true
  }

  findIndex(): number{
    for(var i=0; i<this.userLoggedIn!.followed!.length; i++){
      if(this.userLoggedIn?.followed![i].id == this.user?.id) return i
    }
    return -1
  }

  unfollow(): void{
    var index = this.findIndex()
    this.userLoggedIn?.followed?.splice(index, 1)
    this.updateUserLoggedIn()
    this.userService.unfollow(this.userLoggedIn!.id, this.user!.id)
    this.followed = false
  }

  isMyAccount(): void{
    if(this.userLoggedIn?.username == this.user?.username) this.myAccount = true
    else this.myAccount = false
  }

  getUser(id: string): void {
    this.userService.getUser(id)
      .subscribe(user => {
        this.user = user
        this.checkAllowed()
        this.isMyAccount()
        if(!this.myAccount)
          this.checkFollowed()
        this.show = 'playlistsCreated'
      });
  }

  goBack(): void {
    this.location.back();
  }

  modifyUser(){
    this.showForm()
    this.snapshot = Object.assign({}, this.user)
  }

  saveUser(): void {
    if (this.user) {
      this.userService.updateUser(this.snapshot!, this.user)
        .subscribe();
      if(this.myAccount){
        this.user.followed = this.userLoggedIn?.followed
        this.userLoggedIn = this.user
        this.updateUserLoggedIn()
      }
    }
    this.showForm()
  }

  showForm(): void{
    this.showModifyForm = !this.showModifyForm
  }

  setShow(show: string): void {
    this.show = show;
  }

  deleteUser(): void {
    this.userService.deleteUser(this.user!.id);
    if(this.myAccount){
      this.userLoggedIn = undefined
      localStorage.removeItem("userLoggedIn")
    }
    this.router.navigateByUrl('/dashboard')
  }
}
