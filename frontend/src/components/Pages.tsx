import React, { useState } from "react";
import { PaginationBar } from "./PaginationBar";

interface PagesProps {
    initialIndex: number;
    maxIndex: number;
    onPageChange: (index: number) => void;
}

const Pages: React.FC<PagesProps> = ({ initialIndex, maxIndex, onPageChange }) => {
    const [currentIndex, setCurrentIndex] = useState<number>(initialIndex);

    return <>
        {maxIndex >= 1 &&
            <PaginationBar
                currentIndex={currentIndex}
                onNext={index => {
                    setCurrentIndex(index)
                    onPageChange(index)
                }}
                onPrev={index => {
                    setCurrentIndex(index)
                    onPageChange(index)
                }}
                maxIndex={maxIndex}
            />
    }
    </>
};

export default Pages;