import { Routes } from '@angular/router';
import {YearPlanDetailComponent} from './components/year-plan-detail/year-plan-detail.component';
import {YearPlanComponent} from './components/year-plan/year-plan.component';

export const routes: Routes = [
  { path: '', component: YearPlanComponent },
  { path: 'year-plan/:uuid', component: YearPlanDetailComponent }, // Route to the detail page
];
