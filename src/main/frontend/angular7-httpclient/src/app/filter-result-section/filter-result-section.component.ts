import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-filter-result-section',
  templateUrl: './filter-result-section.component.html',
  styleUrls: ['./filter-result-section.component.css']
})
export class FilterResultSectionComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  @Input() filteredVideos: any[];
}
