export interface Notifications{
    _id: string;
    userId: string;
    senderName: string;
    senderDp: string;
    postId: string;
    message: string;
    isRead: boolean;
    timestamp: string;
    _class: string;
}