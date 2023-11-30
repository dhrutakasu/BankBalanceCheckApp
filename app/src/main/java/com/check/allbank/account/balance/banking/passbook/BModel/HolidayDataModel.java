package com.check.allbank.account.balance.banking.passbook.BModel;

import java.util.ArrayList;
import java.util.List;

public class HolidayDataModel {
    private String state;
    private List<Holiday> holidays;

    public HolidayDataModel(String state, List<Holiday> holidays) {
        this.state = state;
        this.holidays = holidays;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public List<Holiday> getHolidaysForState(List<HolidayDataModel> stateHolidays, String stateName) {
        List<Holiday> holidaysForState = new ArrayList<>();

        for (HolidayDataModel stateHoliday : stateHolidays) {
            if (stateHoliday.getState().equalsIgnoreCase(stateName)) {
                holidaysForState.addAll(stateHoliday.getHolidays());
            }
        }

        return holidaysForState;
    }

}