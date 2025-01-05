import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {YearPlan, YearPlanDetail} from '../models/year-plan.model';
import {YearPlanResponse} from '../models/year-plan.response';

@Injectable({
  providedIn: 'root'
})
export class GroupChatService {

  private apiUrl = 'http://localhost:8080/groupchat/year-plan';

  constructor(private http: HttpClient) { }

  // List year plans
  listYearPlans(): Observable<YearPlan[]> {
    return this.http.get<YearPlan[]>(this.apiUrl);
  }

  // Get a specific year plan by UUID
  getYearPlan(uuid: string): Observable<YearPlanDetail> {
    return this.http.get<YearPlanDetail>(`${this.apiUrl}/${uuid}`);
  }

  // Create a new year plan
  createYearPlan(yearPlan: any): Observable<YearPlanResponse> {
    return this.http.post<YearPlanResponse>(this.apiUrl, yearPlan);
  }

  // Download Year Plan as Excel
  downloadYearPlanExcel(uuid: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${uuid}/xslx`, {
      headers: new HttpHeaders({ 'Accept': 'application/octet-stream' }),
      responseType: 'blob'
    });
  }

  // Download Year Plan as ICS
  downloadYearPlanICS(uuid: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${uuid}/ics`, {
      headers: new HttpHeaders({ 'Accept': 'text/calendar' }),
      responseType: 'blob'
    });
  }
}
