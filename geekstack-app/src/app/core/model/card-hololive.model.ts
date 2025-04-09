export interface HololiveCard {
    _id: string;
    cardName: string;
    urlimage: string;
    detailUrl: string;
    cardNameJP: string;
    cardType: string;
    cardTypeJP: string;
    rarity: string;
    included_products: string;
    color: string | null;
    life: string | null;
    hp: string | null;
    bloomLevel: string | null;
    passingBaton: string | null;
    spArts: string | null;
    tags: string[];
    skillJP: string;
    skill: string;
    keywordJP: string | null;
    keyword: string | null;
    spSkillJP: string;
    spSkill: string;
    illustrator: string;
    cardId: string;
    cardUid: string;
    booster: string;
  }
