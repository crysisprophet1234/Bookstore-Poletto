import { Authority } from '../utils/auth'

export type User = {

    id: number
    name: string
    firstname: string
    lastname: string
    email: string
    roles: Authority[]

}