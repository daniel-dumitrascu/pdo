import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-import-page',
  templateUrl: './import-page.component.html',
  styleUrls: ['./import-page.component.css']
})
export class ImportPageComponent implements OnInit {

  constructor(private formBuilder: FormBuilder, private httpClient: HttpClient) { }

  ngOnInit() {
    this.uploadForm = this.formBuilder.group({
        profile: ['']
      });

    var browseButton = this.browseButton.nativeElement
    var importButton = this.importButton.nativeElement

    if(browseButton.value !== "")
    {
      importButton.disabled = false;
    }
    else
    {
      importButton.disabled = true;
    }
  }

  public AfterFileSelection($event) {
    //var browseButton = this.browseButton.nativeElement
    var importButton = this.importButton.nativeElement

    /*if(browseButton.value !== "")
    {
      importButton.disabled = false;
    }*/

    if ((<HTMLInputElement>event.target).files.length > 0) {
      importButton.disabled = false;
      const file = (<HTMLInputElement>event.target).files[0];
      this.uploadForm.get('profile').setValue(file);
    }
  }

  public StartSpinner($event) {
    var spinner = this.spinner.nativeElement
    spinner.hidden = false;
  }

  public onSubmit() {
    const formData = new FormData();
    formData.append('browseButton', this.uploadForm.get('profile').value);

    this.httpClient.post<any>('http://localhost:8080/centralpoint/video/import', formData).subscribe(
      (res) => console.log(res),
      (err) => console.log(err)
    );
  }

  uploadForm: FormGroup;

  @ViewChild('browseButton') browseButton;
  @ViewChild('importButton') importButton;
  @ViewChild('spinner') spinner;
}
