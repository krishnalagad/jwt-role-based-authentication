import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  model: any = {
    username: '',
    password: '',
  };
  sessionId: any = {};
  flag: boolean = false;

  constructor(private _router: Router, private _http: HttpClient) {}

  ngOnInit(): void {}

  login() {
    // console.log(this.model);

    let data = {
      username: this.model.username,
      password: this.model.password,
    };
    this._http
      .post('http://localhost:8080/api/auth/', data)
      .subscribe((response: any) => {
        if (response && response.accessToken) {
          console.log(response);
          this.sessionId = response.accessToken;
          sessionStorage.setItem('token', this.sessionId);

          // this._router.navigate(['']);
          this.flag = true;
        } else {
          console.log('Failed');
        }
      });

    // let url = '/api/';
    // this._http
    //   .post<any>(url, {
    //     username: this.model.username,
    //     password: this.model.password,
    //   })
    //   .subscribe((res) => {
    //     if (res) {
    //       console.log(res);
    //       // this.sessionId = res.sessionId;
    //       // sessionStorage.setItem('token', this.sessionId);
    //       // this._router.navigate(['/home']);
    //     } else {
    //       alert('Authentication failed !!');
    //     }
    //   });
  }
}
