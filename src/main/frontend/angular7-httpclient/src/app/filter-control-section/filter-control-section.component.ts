import { Component, Output, EventEmitter, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import * as jquery from 'jquery';

@Component({
  selector: 'app-filter-control-section',
  templateUrl: './filter-control-section.component.html',
  styleUrls: ['./filter-control-section.component.css']
})
export class FilterControlSectionComponent implements OnInit {

  constructor(private http: HttpClient) { }

  ngOnInit() {
      this.http.get('http://localhost:8080/centralpoint/video/search', { responseType: 'text' })
                                .subscribe(response => {
                                                           console.log("Success");
                                                           var myObjStr = JSON.parse(response);
                                                           this.optionsGenre = myObjStr.genres;
                                                           this.optionsCountries = myObjStr.countries;
                                                       },
                                                       err =>
                                                       {
                                                           console.log("Error");
                                                           console.log(err);
                                                       });

      jquery('.js-example-basic-multiple').select2();
  }

  public ensureReleaseYearCorrectness(event) {
      if(event.target.checked == true)
      {
          if(event.target.id == "videoBeforeYear")
          {
              this.videoAfterYear.nativeElement.checked = false;
          }
          else
          {
              this.videoBeforeYear.nativeElement.checked = false;
          }
      }
  }

  public ensureRuntimeCorrectness(event) {
      if(event.target.checked == true)
      {
          if(event.target.id == "videoWithinRuntime")
          {
              this.videoExceedsRuntime.nativeElement.checked = false;
          }
          else
          {
              this.videoWithinRuntime.nativeElement.checked = false;
          }
      }
  }

  public ensureInternetScoreCorrectness(event) {
      if(event.target.checked == true)
      {
          if(event.target.id == "videoWithinInternetScore")
          {
              this.videoExceedsInternetScore.nativeElement.checked = false;
          }
          else
          {
              this.videoWithinInternetScore.nativeElement.checked = false;
          }
      }
  }

  public getVideoByFilter(event) {
      // Get the data
      var videoMainTitle = this.videoMainTitle.nativeElement.value
      var videoExactTitle = this.videoExactTitle.nativeElement.checked
      var videoSawIt = this.videoSawIt.nativeElement.checked
      var videoReleaseYear = this.videoReleaseYear.nativeElement.value
      var videoBeforeYear = this.videoBeforeYear.nativeElement.checked
      var videoAfterYear = this.videoAfterYear.nativeElement.checked
      var videoRuntime = this.videoRuntime.nativeElement.value
      var videoWithinRuntime = this.videoWithinRuntime.nativeElement.checked
      var videoExceedsRuntime = this.videoExceedsRuntime.nativeElement.checked
      var videoInternetScore = this.videoInternetScore.nativeElement.value
      var videoWithinInternetScore = this.videoWithinInternetScore.nativeElement.checked
      var videoExceedsInternetScore = this.videoExceedsInternetScore.nativeElement.checked

      this.infoMsg = ''

      var videoGenreMultiple = []
      for(let i = 0; i < this.videoGenreMultiple.nativeElement.selectedOptions.length; i++)
      {
          videoGenreMultiple.push(this.videoGenreMultiple.nativeElement.selectedOptions[i].label);
      }

      var videoCountryMultiple = []
      for(let i = 0; i < this.videoCountryMultiple.nativeElement.selectedOptions.length; i++)
      {
          videoCountryMultiple.push(this.videoCountryMultiple.nativeElement.selectedOptions[i].label);
      }

      // Construct the JSON
      var jsonData = {
                         "videoMainTitle": videoMainTitle,
                         "videoExactTitle": videoExactTitle,
                         "videoSawIt": videoSawIt,
                         "videoReleaseYear": videoReleaseYear,
                         "videoBeforeYear": videoBeforeYear,
                         "videoAfterYear": videoAfterYear,
                         "videoRuntime": videoRuntime,
                         "videoWithinRuntime": videoWithinRuntime,
                         "videoExceedsRuntime": videoExceedsRuntime,
                         "videoInternetScore": videoInternetScore,
                         "videoWithinInternetScore": videoWithinInternetScore,
                         "videoExceedsInternetScore": videoExceedsInternetScore,
                         "videoGenreMultiple": videoGenreMultiple,
                         "videoCountryMultiple": videoCountryMultiple
                     };

      // We can now send the request to the server
      var httpOptions = {
                headers: new HttpHeaders({
                    'Content-Type':'application/json',
                    observe: 'body',
                    responseType: 'json'
                })};

      this.http.post<any>('http://localhost:8080/centralpoint/video/search', jsonData, httpOptions).subscribe(
            response => {
              console.log("Success: ");
              console.log(response);
              this.videoFilterResult = response.videos;
              this.sendVideoFilterDataToParentPage();
              if(this.videoFilterResult == undefined || this.videoFilterResult.length == 0) {
                this.infoMsg = 'There was nothing found using this filter settings.'
              }
            },
            err => {
              console.log("Error: ");
              console.log(err);
            });
  }

  public sendVideoFilterDataToParentPage() {
      this.filterDataEvent.emit(this.videoFilterResult);
  }

  optionsGenre;
  optionsCountries;
  videoFilterResult: any[];
  infoMsg = '';

  @Output() filterDataEvent = new EventEmitter<any[]>();

  @ViewChild('videoMainTitle') videoMainTitle;
  @ViewChild('videoExactTitle') videoExactTitle;
  @ViewChild('videoSawIt') videoSawIt;
  @ViewChild('videoReleaseYear') videoReleaseYear;
  @ViewChild('videoBeforeYear') videoBeforeYear;
  @ViewChild('videoAfterYear') videoAfterYear;
  @ViewChild('videoRuntime') videoRuntime;
  @ViewChild('videoWithinRuntime') videoWithinRuntime;
  @ViewChild('videoExceedsRuntime') videoExceedsRuntime;
  @ViewChild('videoInternetScore') videoInternetScore;
  @ViewChild('videoWithinInternetScore') videoWithinInternetScore;
  @ViewChild('videoExceedsInternetScore') videoExceedsInternetScore;
  @ViewChild('videoGenreMultiple') videoGenreMultiple;
  @ViewChild('videoCountryMultiple') videoCountryMultiple;
}
