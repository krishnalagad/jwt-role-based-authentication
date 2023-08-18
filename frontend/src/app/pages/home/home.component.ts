import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  tokenValue: any;
  ngOnInit(): void {
    this.tokenValue = sessionStorage.getItem('token');
    console.log(this.tokenValue);
  }
}
