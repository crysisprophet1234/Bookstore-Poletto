import { Category } from "./category"

export type Book = {

    id: number,
    name: string,
    releaseDate: string,
    imgUrl: string,
    status: string,
    author: { id: number, name: string},
    categories: [Category]
    
}