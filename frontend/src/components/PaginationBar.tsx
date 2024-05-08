export type PaginationBarProps = {
    onNext: (next: number) => void
    onPrev: (prev: number) => void
    currentIndex: number,
    maxIndex: number
}

export function PaginationBar(props: PaginationBarProps) {
    const next = () => props.onNext(props.currentIndex + 1);
    const prev = () => props.onNext(props.currentIndex - 1);

    const disableLeft = props.currentIndex <= 0;
    const disableRight = props.currentIndex >= props.maxIndex;

    return <nav className="m-2">
        <ul className="pagination justify-content-center">
            <li className="page-item">
                <button disabled={disableLeft} type="button" className={"page-link " + (disableLeft ? "disabled" : "")} onClick={prev}>
                    {"<"}
                </button>
            </li>
            {props.currentIndex > 0 && <li className="page-item">
                <button className="page-link" onClick={prev}>
                    {props.currentIndex}
                </button>
            </li>}
            <li className="page-item active"><button className="page-link">{props.currentIndex + 1}</button></li>
            {props.currentIndex < props.maxIndex && <li className="page-item">
                <button className="page-link" onClick={next}>
                    {props.currentIndex + 2}
                </button>
            </li>}
            <li className="page-item">
                <button disabled={disableRight} className={"page-link " + (disableRight ? "disabled" : "")} onClick={next}>
                    {">"}
                </button>
            </li>
        </ul>
    </nav>;
}
