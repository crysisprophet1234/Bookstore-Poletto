export const formatDate = (date: string | undefined) => {

    let str = date?.replace('-', '');

    let year = str?.substring(0, 4);
    let month = str?.substring(4, 6);
    let day = str?.substring(7);

    return `${day}/${month}/${year}`;

}

export const formatDateTime = (date: string | undefined) => {

    let subsstr = date?.substring(0, 10)

    let str = subsstr?.replace('-', '');

    let year = str?.substring(0, 4);
    let month = str?.substring(4, 6);
    let day = str?.substring(7);

    return `${day}/${month}/${year}`;

}