import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login/login.service';
import { Router } from '@angular/router';
import { MessageService } from '../messages/messages.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private loginService: LoginService, private router: Router, private messageService: MessageService) { }

  ngOnInit(): void {
      if(!this.loginService.isLoggedIn()){
          this.router.navigate(['login']);
      }
  }
 
}
