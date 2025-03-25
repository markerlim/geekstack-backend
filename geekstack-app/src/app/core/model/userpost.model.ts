import { CardRecord } from "./card-record.model";
import { CommentRecord } from "./comment-record.model";

export interface Userpost{
    postId: string;
    postType: string;
    code: string;
    userId: string;
    headline: string;
    content:string;
    isTournamentDeck: boolean;
    timestamp: string;
    selectedCards: CardRecord[];
    listofcards: CardRecord[];
    listoflikes: string[];
    listofcomments: CommentRecord[];
    name: string;
    displaypic: string;
    _class: string[];
}