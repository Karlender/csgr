import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {YearPlanComponent} from './components/year-plan/year-plan.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'csgr-frontend';
}
