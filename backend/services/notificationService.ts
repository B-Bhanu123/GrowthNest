// FinCoreX Centralized Notification System

export interface NotificationRecord {
  notificationId: string;
  recipientUserId: string;
  channel: 'EMAIL' | 'SMS' | 'PUSH' | 'IN_APP';
  eventType: string;
  subject: string;
  content: string;
  status: 'SENT' | 'QUEUED' | 'FAILED';
  createdAt: string;
}

export class NotificationService {
  private notifications: NotificationRecord[] = [];

  public dispatchNotification(
    recipientUserId: string,
    channel: NotificationRecord['channel'],
    eventType: string,
    subject: string,
    content: string
  ): NotificationRecord {
    const record: NotificationRecord = {
      notificationId: `ntf_${Date.now()}`,
      recipientUserId,
      channel,
      eventType,
      subject,
      content,
      status: 'SENT',
      createdAt: new Date().toISOString()
    };
    this.notifications.unshift(record);
    return record;
  }

  public getNotifications(recipientUserId: string): NotificationRecord[] {
    return this.notifications.filter(n => n.recipientUserId === recipientUserId);
  }
}

export const notificationServiceInstance = new NotificationService();
