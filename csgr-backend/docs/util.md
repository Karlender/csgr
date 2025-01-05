# Util-Docs

More detailed documentation of how util components work.

## YearPlanUtil

### High level overview

High level overview of how a new year plan is created:

```mermaid
sequenceDiagram
    participant User
    participant YearPlanController
    participant YearPlanService
    participant YearPlanUtil
    participant YearPlanRepository
    User->>YearPlanController: Create new year plan
    YearPlanController->>YearPlanService: createGroupChatYearPlan(yearPlanDto)
    YearPlanService->>YearPlanUtil: createBalancedYearPlan(employees, numberOfGroups, rotation, year)
    YearPlanUtil-->>YearPlanService: return year plan
    YearPlanService->>YearPlanService: Generate YearPlan, Appointments & AppointmentGroups entities with the year plan data
    YearPlanService->>YearPlanRepository: Persist YearPlan
    YearPlanRepository-->>YearPlanService: Return persisted YearPlan
    YearPlanService-->>YearPlanController: return year plan UUID
    YearPlanController->>User: return year plan UUID
```

### Detailed look into the logic

A more detailed look into the year plan generation logic:

```mermaid
sequenceDiagram
    participant User as User
    participant YearPlanUtil as YearPlanUtil
    participant EncounterMatrixUtil as EncounterMatrixUtil
    participant GroupsUtil as Group Creation Logic
    participant EmployeeSelector as Employee Selector

    User ->> YearPlanUtil: createBalancedYearPlan(employees, numberOfGroups, rotation, year)
    activate YearPlanUtil

    YearPlanUtil ->> YearPlanUtil: Initialize stepFunction (DAILY, WEEKLY, MONTHLY)

    YearPlanUtil ->> EncounterMatrixUtil: initializeEncounterMatrix(employees)
    EncounterMatrixUtil -->> YearPlanUtil: encounterMatrix initialized

    loop For each date in year
        YearPlanUtil ->> GroupsUtil: createBalancedGroups(employees, numberOfGroups, encounterMatrix)
        activate GroupsUtil

        GroupsUtil ->> GroupsUtil: Shuffle employees
        loop For each group
            GroupsUtil ->> EmployeeSelector: selectEmployeeWithLeastPairwiseEncounters(currentGroup, candidates, encounterMatrix)
            activate EmployeeSelector

            EmployeeSelector ->> EmployeeSelector: Calculate total encounters for each candidate
            EmployeeSelector -->> GroupsUtil: Return candidate with least encounters
            deactivate EmployeeSelector

            GroupsUtil ->> GroupsUtil: Add selected employee to group
            GroupsUtil ->> GroupsUtil: Remove employee from candidates
        end

        GroupsUtil -->> YearPlanUtil: Balanced groups created
        deactivate GroupsUtil

        YearPlanUtil ->> EncounterMatrixUtil: updateEncounterMatrix(groups, encounterMatrix)
        EncounterMatrixUtil -->> YearPlanUtil: encounterMatrix updated
    end

    YearPlanUtil -->> User: Yearly schedule with balanced groups
    deactivate YearPlanUtil
```
