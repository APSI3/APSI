import React, { useState, useEffect } from "react";
import DatePicker from "react-datepicker";

interface DateRangePickerProps {
    initialFrom: Date;
    initialTo: Date;
    onDateChange: (from: Date, to: Date) => void;
}

const DateRangePicker: React.FC<DateRangePickerProps> = ({ initialFrom, initialTo, onDateChange }) => {
    const [from, setFrom] = useState<Date>(initialFrom);
    const [to, setTo] = useState<Date>(initialTo);

    useEffect(() => {
        onDateChange(from, to);
    }, [from, to]);

    return (
        <>
            <h4>Data wydarzenia</h4>
            <span className="d-inline-flex">
                <label className="m-2">Od: </label>
                <div className="m-2">
                    <DatePicker
                        className="datepicker"
                        dateFormat={"dd/MM/yyyy"}
                        selected={from}
                        onChange={date => setFrom(date ?? new Date())}
                    />
                </div>
                <label className="m-2">Do:</label>
                <div className="m-2">
                    <DatePicker
                        className="datepicker"
                        dateFormat={"dd/MM/yyyy"}
                        selected={to}
                        onChange={date => setTo(date ?? new Date())}
                    />
                </div>
            </span>
        </>
    );
};

export default DateRangePicker;