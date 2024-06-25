export class DateHelper {
    public static isDateInThePast(date: Date) {
        const currentDate = new Date();
        const eventStartDate = new Date(date);

        currentDate.setHours(0, 0, 0, 0);
        eventStartDate.setHours(0, 0, 0, 0);
        return eventStartDate <= currentDate
    }
}

export default DateHelper;