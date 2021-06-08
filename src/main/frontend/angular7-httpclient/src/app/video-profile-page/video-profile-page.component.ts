import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders  } from '@angular/common/http';

@Component({
  selector: 'app-video-profile-page',
  templateUrl: './video-profile-page.component.html',
  styleUrls: ['./video-profile-page.component.css']
})
export class VideoProfilePageComponent implements OnInit {
  id: number;
  private sub: any;
  private video: any;

  constructor(private route: ActivatedRoute,
              private http: HttpClient) { }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
       this.id = +params['id']; // (+) converts string 'id' to a number

       this.http.get(`http://localhost:8080/centralpoint/video/profile/${this.id}`, { responseType: 'text' })
                                       .subscribe(response => {
                                                                  console.log("Success");
                                                                  var myObjStr = JSON.parse(response);
                                                                  this.video = myObjStr.videos[0];
                                                              },
                                                              err =>
                                                              {
                                                                  this.video = undefined;
                                                                  console.log("Error");
                                                                  console.log(err);
                                                              });

    });
  }

  ngOnDestroy() {
      this.sub.unsubscribe();
    }

}
