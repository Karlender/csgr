import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GroupChatService } from '../../services/group-chat.service';
import {CommonModule} from '@angular/common';
import {YearPlanDetail} from '../../models/year-plan.model';

@Component({
  selector: 'app-year-plan-detail',
  templateUrl: './year-plan-detail.component.html',
  imports: [
    CommonModule
  ],
  styleUrls: ['./year-plan-detail.component.css']
})
export class YearPlanDetailComponent implements OnInit {
  yearPlanDetail: YearPlanDetail | null = null;
  employeesInMatrix: string[] = [];
  selectedEmployee: string | null = null;
  encounterDetails: { [key: string]: number } = {};

  constructor(
    private route: ActivatedRoute,
    private groupChatService: GroupChatService
  ) {}

  ngOnInit(): void {
    const uuid = this.route.snapshot.paramMap.get('uuid');
    if (uuid) {
      this.groupChatService.getYearPlan(uuid).subscribe((data: YearPlanDetail) => {
        this.yearPlanDetail = data;
        this.employeesInMatrix = Object.keys(data.encounterMatrix || {});
      });
    }
  }

  showEncounters(employee: string): void {
    this.selectedEmployee = employee;
    this.encounterDetails = this.yearPlanDetail?.encounterMatrix[employee] || {};
  }
}
