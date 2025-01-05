export interface YearPlan {
  uuid: string;
  year: number;
  rotation: string;
  employees: string[];
  appointments: Appointment[];
}

export interface YearPlanDetail {
  uuid: string;
  year: number;
  rotation: string;
  employees: string[];
  appointments: Appointment[];
  encounterMatrix: {
    [key: string]: {
      [key: string]: number
    }
  };
}

export interface Appointment {
  uuid: string;
  fromDate: string;
  groups: AppointmentGroup[];
}

export interface AppointmentGroup {
  uuid: string;
  employees: string[];
}
