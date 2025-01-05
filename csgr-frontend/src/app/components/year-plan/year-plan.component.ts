import { Component, OnInit } from '@angular/core';
import { GroupChatService } from '../../services/group-chat.service';
import { YearPlan } from '../../models/year-plan.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {Router} from '@angular/router';

@Component({
  selector: 'app-year-plan',
  templateUrl: './year-plan.component.html',
  imports: [
    CommonModule,
    FormsModule
  ],
  styleUrls: ['./year-plan.component.css']
})
export class YearPlanComponent implements OnInit {

  yearPlans: YearPlan[] = [];
  newYearPlan = {
    employees: [],  // Initialize as an empty array
    groupCount: 2,  // Minimum group count
    rotation: 'DAILY',  // Rotation type (can be DAILY, WEEKLY, or MONTHLY)
    year: 2025
  }; // YearPlan creation form

  employeeName = '';  // Temporary variable to bind input for employee names

  constructor(private groupChatService: GroupChatService, private router: Router) { }

  ngOnInit(): void {
    this.loadYearPlans();
  }

  // Load year plans
  loadYearPlans(): void {
    this.groupChatService.listYearPlans().subscribe(data => {
      this.yearPlans = data;
    });
  }

  // Add employee to the list
  addEmployee(): void {
    if (this.employeeName.trim()) {
      // @ts-ignore
      this.newYearPlan.employees.push(this.employeeName.trim());
      this.employeeName = '';  // Clear input field after adding
    }
  }

  // Remove employee from the list
  removeEmployee(index: number): void {
    this.newYearPlan.employees.splice(index, 1);
  }

  // Create new year plan
  createYearPlan(): void {
    this.groupChatService.createYearPlan(this.newYearPlan).subscribe(response => {
      console.log('Year plan created with UUID:', response.uuid);
      this.loadYearPlans();
    });
  }

  viewYearPlan(uuid: string): void {
    this.router.navigate(['/year-plan', uuid]); // Navigate to the year plan detail page
  }

  // Download year plan as Excel
  downloadExcel(uuid: string): void {
    this.groupChatService.downloadYearPlanExcel(uuid).subscribe(response => {
      const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = `Jahresplan_${uuid}.xlsx`;
      link.click();
    });
  }

  // Download year plan as ICS
  downloadICS(uuid: string): void {
    this.groupChatService.downloadYearPlanICS(uuid).subscribe(response => {
      const blob = new Blob([response], { type: 'text/calendar' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = `Jahresplan_${uuid}.ics`;
      link.click();
    });
  }
}
