import { Category } from "./category"

export type Books = {

    id: number,
    name: string,
    releaseDate: string,
    imgUrl: string,
    status: string,
    author: { id: number, name: string},
    categories: [Category]
    
}