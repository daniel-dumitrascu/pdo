import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';

@Component({
  selector: 'app-register-section',
  templateUrl: './register-section.component.html',
  styleUrls: ['./register-section.component.css']
})
export class RegisterSectionComponent implements OnInit {

  constructor(private http: HttpClient) { }

  ngOnInit() {
  }

  public submitChecker(event) {
      if(this.videoPersonalScore.nativeElement.value == 'undefined' ||
         this.videoPersonalScore.nativeElement.value == false)
        this.videoPersonalScore.nativeElement.value = 0;
  }

  public sendData(event) {

      this.submitChecker(event);

      this.errorMsg = '';
      this.infoMsg = '';

      var videoTitle = this.videoTitle.nativeElement.value
      var videoType = this.videoType.nativeElement.value
      var videoQuality = this.videoQuality.nativeElement.value
      var videoPersonalScore = this.videoPersonalScore.nativeElement.value
      var videoReleaseYear = this.videoReleaseYear.nativeElement.value
      var videoStored = this.videoStored.nativeElement.value
      var videoSawIt = this.videoSawIt.nativeElement.checked
      var videoHasRomanianSub = this.videoHasRomanianSub.nativeElement.checked
      var videoHasEnglishSub = this.videoHasEnglishSub.nativeElement.checked

      if(videoTitle == undefined || videoTitle.length == 0) {
          this.errorMsg = "Give a title to the registered movie.";
          return;
      }

      var jsonData = {
                         "videoTitle": videoTitle,
                         "videoType": videoType,
                         "videoQuality": videoQuality,
                         "videoPersonalScore": videoPersonalScore,
                         "videoReleaseYear": videoReleaseYear,
                         "videoStored": videoStored,
                         "videoSawIt": videoSawIt,
                         "videoHasRomanianSub": videoHasRomanianSub,
                         "videoHasEnglishSub": videoHasEnglishSub
                     };

      var httpOptions = {
          headers: new HttpHeaders({
            'Content-Type':'application/json',
            observe: 'body',
            responseType: 'json'
          }) };

       let response = this.http.post('http://localhost:8080/centralpoint/video/register', jsonData, httpOptions).subscribe(
              response => {
                console.log("Success: ");
                console.log(response);
                this.infoMsg = 'The movie was successfully registered';
              },
              err => {
                console.log("Error: ");
                console.log(err);
                this.errorMsg = 'The movies couldn\'t be registered on the backend';
              }
           );
  }

  errorMsg = '';
  infoMsg = '';

  @ViewChild('videoTitle') videoTitle;
  @ViewChild('videoType') videoType;
  @ViewChild('videoQuality') videoQuality;
  @ViewChild('videoPersonalScore') videoPersonalScore;
  @ViewChild('videoReleaseYear') videoReleaseYear;
  @ViewChild('videoStored') videoStored;
  @ViewChild('videoSawIt') videoSawIt;
  @ViewChild('videoHasRomanianSub') videoHasRomanianSub;
  @ViewChild('videoHasEnglishSub') videoHasEnglishSub;
}
