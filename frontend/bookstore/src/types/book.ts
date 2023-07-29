import { Author } from "./author"
import { Category } from "./category"

export type Book = {

    id: number,
    name: string,
    releaseDate: string,
    imgUrl: string,
    status: string,
    author: Author,
    categories: Category[]
    
}