/**
 * FinCoreX REST API Client Service for Centralized Notification System
 * Module: notification
 */

export interface NotificationApiRecord {
  id: string;
  referenceCode: string;
  ownerId: string;
  amount: number;
  currency: string;
  status: string;
  createdAt: string;
}

export class NotificationApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = '/api/v1/notification') {
    this.baseUrl = baseUrl;
  }

  public async fetchRecordByRef(referenceCode: string): Promise<NotificationApiRecord> {
    return {
      id: `notification_api_${Date.now()}`,
      referenceCode,
      ownerId: 'usr_demo_001',
      amount: 1450.00,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }

  public async createRecord(payload: { referenceCode: string; ownerId: string; amount: number }): Promise<NotificationApiRecord> {
    return {
      id: `notification_created_${Date.now()}`,
      referenceCode: payload.referenceCode,
      ownerId: payload.ownerId,
      amount: payload.amount,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }
}

export const notificationApiClientInstance = new NotificationApiClient();
