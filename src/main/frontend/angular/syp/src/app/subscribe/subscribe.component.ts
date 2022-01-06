import { Component, OnInit } from '@angular/core';
import { User } from '../interfaces/user';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-subscribe',
  templateUrl: './subscribe.component.html',
  styleUrls: ['./subscribe.component.css']
})
export class SubscribeComponent implements OnInit {

  name: string | undefined
  username: string | undefined
  password: string | undefined
  confirmPassword: string | undefined
  users$!: Observable<User[]>;

  constructor(
    private userService: UserService,
    private router: Router
    ) { }

  ngOnInit(): void {
  }

  subscribe(): void {
    this.userService.getUserByUsername(this.username!).subscribe(users => {
      if(users.length != 0) {
        window.alert("username already taken")
        return
      }
      else{
        this.addUser()
      }
    })
  }

  addUser(): void{
    var username = this.username
    var password = this.password
    var dateOfCreation = new Date()
    if(password == this.confirmPassword){
      this.userService.addUser({ username, password, dateOfCreation } as User)
      .subscribe(_ => {
        this.router.navigateByUrl('/login');
      });
    }
    else {
      window.alert("passwords aren't the same")
    }
  }

}
