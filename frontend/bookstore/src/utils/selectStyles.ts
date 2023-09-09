import { CSSObjectWithLabel, ControlProps, StylesConfig } from "react-select";

export const selectStyles: StylesConfig<any, false> = {

    control: (provided: CSSObjectWithLabel, props: ControlProps<any, false>) => ({
        ...provided,
        height: '40px',
        border: '1px solid #407bff',
        ":hover": {
            border: '1px solid #407bff'
        }
    }),

    valueContainer: (provided) => ({
        ...provided,
        height: '40px',
        padding: '0px 8px'
    }),

    input: (provided) => ({
        ...provided,
        margin: '0px',
    }),

    singleValue: (provided) => ({
        ...provided,
        marginBottom: '8px',
        textOverflow: 'initial',
        overflow: 'visible'
    }),

    indicatorsContainer: (provided) => ({
        ...provided,
        height: '40px'
    }),

    indicatorSeparator: (provided) => ({
        ...provided
    })
}