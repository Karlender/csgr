import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { YearPlanComponent } from './components/year-plan/year-plan.component';
import {CommonModule} from '@angular/common';
import {YearPlanDetailComponent} from "./components/year-plan-detail/year-plan-detail.component";

@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    AppComponent,
    YearPlanComponent,
    YearPlanDetailComponent
  ]
})
export class AppModule { }
