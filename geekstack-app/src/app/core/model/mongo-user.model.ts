import { ListOfDecks } from "./listofdecks.model";

export interface GSMongoUser{
    userId: string;
    crbdecks: ListOfDecks[];
    uadecks: ListOfDecks[];
    opdecks: ListOfDecks[];
    dbzfwdecks: ListOfDecks[];
}