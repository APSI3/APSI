import { CountryDTO } from "../api/DTOs";

interface CountriesSearchResultProps {
    country: CountryDTO;
    onClick: (country: CountryDTO) => void;
}

export function CountriesSearchResult(props: CountriesSearchResultProps) {

    const onClick = () => {
        props.onClick(props.country)
    }

    return (
        <div className="search-result-item" onClick={onClick}>
                {props.country.full_name}
        </div>
    )
}
