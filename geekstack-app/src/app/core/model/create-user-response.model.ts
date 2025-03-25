import { GSMongoUser } from "./mongo-user.model";
import { GSSqlUser } from "./sql-user.model";

export interface CreateUserResponse {
    userObject: {gsMongoUser:GSMongoUser, gsSQLUser:GSSqlUser};
  }
  