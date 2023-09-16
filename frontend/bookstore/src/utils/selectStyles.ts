import { CSSObjectWithLabel, ControlProps, GroupBase, StylesConfig } from "react-select"

export const selectStyles: StylesConfig<any, boolean> = {

    control: (provided: CSSObjectWithLabel, props: ControlProps<any, boolean, GroupBase<any>>) => ({
        ...provided,
        height: '45px',
        border: '1px solid #407bff',
        ":hover": {
            border: '1px solid #407bff'
        },
    }),

    valueContainer: (provided) => ({
        ...provided,
        height: '45px',
        padding: '0px 8px'
    }),

    input: (provided) => ({
        ...provided,
        margin: '0px',
    }),

    singleValue: (provided) => ({
        ...provided,
        marginBottom: '5px',
        textOverflow: 'initial',
        overflow: 'visible'
    }),

    indicatorsContainer: (provided) => ({
        ...provided,
        height: '40px'
    }),

    indicatorSeparator: (provided) => ({
        ...provided
    }),

    multiValue: (provided) => ({
        ...provided,
        marginBottom: '5px',
        marginRight: '5px',
        textOverflow: 'initial',
        overflow: 'visible'
    }),

    placeholder: (provided) => ({
        ...provided,
        marginBottom: '5px'
    })

}