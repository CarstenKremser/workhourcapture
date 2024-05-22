export type workingDaysType = {
    allocated: Date,
    worked: Date,
    workingDays: [workingDayType]
}

export type workingDayType = {
    date: Date,
    allocated: Date,
    worked: Date,
    workingTimes: [workingTimeType]
}

export type workingTimeType = {
    startDate: Date,
    startTime: Date,
    endDate: Date,
    endTime: Date,
    duration: Date
}