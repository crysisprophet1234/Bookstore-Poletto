export const formatDate = (date: string | undefined) => {

    const datePart = date?.split(" ")[0]
    const dateParts = datePart?.split("-")
    const formattedDate = dateParts ? `${dateParts[2]}/${dateParts[1]}/${dateParts[0]}` : null

    return formattedDate

}

export const formatDateTime = (date: string) => {

    const datePart = date.split(" ")[0]
    const timePart = date.split(" ")[2]
    const dateParts = datePart.split("-")
    const formattedDate = `${dateParts[2]}/${dateParts[1]}/${dateParts[0]} ${timePart}`

    return formattedDate

}

export const formatDateApi = (date: Date) => {

    try {

        return date.toISOString().split('T')[0]

    } catch (error) {

        console.log(error)

    }

}