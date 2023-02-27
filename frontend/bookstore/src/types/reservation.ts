import { Book } from "./book";
import { User } from "./user";

export type Reservation = {

    id: number;
    moment: string;
    devolution: string;
    weeks: string;
    status: string;
    client: User;
    books: [Book]

}