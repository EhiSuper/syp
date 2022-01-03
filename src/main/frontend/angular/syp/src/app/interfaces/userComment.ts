import { Song } from "./song";
import { User } from "./user";

export interface userComment{
    id: number
    user?: User
    song?: Song
    vote: number
    body: string
    date: Date
}