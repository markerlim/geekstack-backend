package com.geekstack.cards.utils;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextQuery;

public class Constants {

    //MongoDB
    public static final String C_UNIONARENA="CL_unionarena_v2";
    public static final String C_DUELMASTER="CL_duelmaster";
    public static final String C_ONEPIECE="CL_onepiece";
    public static final String C_DRAGONBALLZFW="CL_dragonballzfw";
    public static final String C_COOKIERUN="CL_cookierunbraverse";
    public static final String C_FULLAHEAD="cardprices_fulla";
    public static final String C_YUYUTEI="cardprices_yyt";
    public static final String C_USERPOST="userpost";
    public static final String C_USER="users";
    public static final String C_NOTIFICATION="notification";

    public static final String F_USERID="_id";
    public static final String F_USERID_REAL="userId";
    public static final String F_DECKS="decks";
    public static final String F_UADECKS="uadecks";
    public static final String F_OPDECKS="opdeck";
    public static final String F_DBZFWDECKS="dbzfwdecks";
    public static final String F_CRBDECKS="crbdecks";
    public static final String F_DECKUID="deckuid";

    public static final String T_UA="unionarena";
    public static final String T_OP="onepiece";
    public static final String T_CRB="cookierunbraverse";
    public static final String T_DBZ="dragonballzfw";

    public static final String F_BOOSTER="booster";
    public static final String F_ANIMECODE="animeCode";
    public static final String F_RARITY="rarity";
    public static final String F_COLOR="color";
    public static final String F_CATEGORY="category";
    public static final String F_CIVILIZATION="civilization";
    public static final String F_CARDTYPE="cardType";
    public static final String F_CARDUID="cardUid";
    public static final String F_CARDID="cardId";

    public static final String F_USERPOST_ID="_id";
    public static final String F_USERPOST_CID="commentId";
    public static final String F_USERPOST_LISTC="listofcomments";
    public static final String F_USERPOST_LISTL="listoflikes";

    public static final String PRICE_FULLA ="price_fulla_id";
    public static final String PRICE_YYT ="price_yyt_id";

    //Redis
    public static final String NOTIF_QUEUE_NAME="notiQueue";
    public static final String PROCESSING_QUEUE="holdingQueue";



    public static void QuerySorting(Query query, String field, boolean isAscending) {
        Sort.Direction direction = isAscending ? Sort.Direction.ASC : Sort.Direction.DESC;
        query.with(Sort.by(direction, field));
    }

    public static void TextQuerySorting(TextQuery textQuery, String field, boolean isAscending) {
        Sort.Direction direction = isAscending ? Sort.Direction.ASC : Sort.Direction.DESC;
        textQuery.with(Sort.by(direction, field));
    }

    public static void TextQuerySorting(TextQuery textQuery, String field1, boolean isAscending1, String field2, boolean isAscending2) {
        Sort.Order order1 = new Sort.Order(isAscending1 ? Sort.Direction.ASC : Sort.Direction.DESC, field1);
        Sort.Order order2 = new Sort.Order(isAscending2 ? Sort.Direction.ASC : Sort.Direction.DESC, field2);
        textQuery.with(Sort.by(order1, order2));
    }
    

}
