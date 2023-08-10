export const formatDate = (date: string | undefined) => {

    let str = date?.replace('-', '')

    let year = str?.substring(0, 4)
    let month = str?.substring(4, 6)
    let day = str?.substring(7)

    return `${day}/${month}/${year}`

}

export const formatDateTime = (date: string) => {

    const datePart = date.split(" ")[0];
    const timePart = date.split(" ")[2];
    const dateParts = datePart.split("-");
    const formattedDate = `${dateParts[0]}/${dateParts[1]}/${dateParts[2]} ${timePart}`;

    return formattedDate

}