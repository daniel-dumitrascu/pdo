import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {HttpClientModule} from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderSectionComponent } from './header-section/header-section.component';
import { RegisterSectionComponent } from './register-section/register-section.component';
import { RegisterPageComponent } from './register-page/register-page.component';
import { HomePageComponent } from './home-page/home-page.component';
import { FilterPageComponent } from './filter-page/filter-page.component';
import { FilterControlSectionComponent } from './filter-control-section/filter-control-section.component';
import { FilterResultSectionComponent } from './filter-result-section/filter-result-section.component';
import { FormsModule } from '@angular/forms';
import { ImportPageComponent } from './import-page/import-page.component';
import { ReactiveFormsModule } from '@angular/forms';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { VideoProfilePageComponent } from './video-profile-page/video-profile-page.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderSectionComponent,
    RegisterSectionComponent,
    RegisterPageComponent,
    HomePageComponent,
    FilterPageComponent,
    FilterControlSectionComponent,
    FilterResultSectionComponent,
    ImportPageComponent,
    PageNotFoundComponent,
    VideoProfilePageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
