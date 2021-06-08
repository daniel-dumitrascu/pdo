import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegisterPageComponent } from './register-page/register-page.component';
import { FilterPageComponent } from './filter-page/filter-page.component';
import { ImportPageComponent } from './import-page/import-page.component';
import { HomePageComponent } from './home-page/home-page.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { VideoProfilePageComponent } from './video-profile-page/video-profile-page.component';

const routes: Routes = [
  { path: 'video/import', component: ImportPageComponent },
  { path: 'video/register', component: RegisterPageComponent },
  { path: 'video/search', component: FilterPageComponent },
  { path: 'video/profile/:id', component: VideoProfilePageComponent },
  { path: '', component: HomePageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
