import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-filter-page',
  templateUrl: './filter-page.component.html',
  styleUrls: ['./filter-page.component.css']
})
export class FilterPageComponent implements OnInit {

  constructor() { }

  ngOnInit() { }

  public receiveFilterData($event) {
      this.videoFilterResult = $event
  }

  videoFilterResult: any[];
}
