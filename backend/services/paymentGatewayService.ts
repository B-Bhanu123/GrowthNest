// FinCoreX Payment Gateway & Orchestration Service

export type PaymentMethodType = 'CARD' | 'UPI' | 'WALLET' | 'NETBANKING' | 'TOKENIZED_CARD';

export interface PaymentOrder {
  orderId: string;
  merchantId: string;
  customerId: string;
  amount: number;
  currency: string;
  paymentMethod: PaymentMethodType;
  providerName: string;
  status: 'CREATED' | 'INITIATED' | 'AUTHORIZED' | 'CAPTURED' | 'SETTLED' | 'FAILED';
  idempotencyKey: string;
  createdAt: string;
}

export class PaymentGatewayService {
  private orders: Map<string, PaymentOrder> = new Map();

  public createPaymentOrder(
    idempotencyKey: string,
    merchantId: string,
    customerId: string,
    amount: number,
    paymentMethod: PaymentMethodType = 'CARD'
  ): PaymentOrder {
    const orderId = `pay_ord_${Date.now()}_${Math.floor(Math.random() * 899 + 100)}`;
    const providerName = paymentMethod === 'UPI' ? 'INTERNAL_UPI_RAILS' : 'GLOBAL_CARD_GATEWAY';

    const order: PaymentOrder = {
      orderId,
      merchantId,
      customerId,
      amount,
      currency: 'USD',
      paymentMethod,
      providerName,
      status: 'CREATED',
      idempotencyKey,
      createdAt: new Date().toISOString()
    };

    this.orders.set(orderId, order);
    return order;
  }

  public authorizeAndCapture(orderId: string): PaymentOrder {
    const order = this.orders.get(orderId);
    if (!order) throw new Error('Order not found');

    order.status = 'AUTHORIZED';
    // Simulate immediate capture for digital payments
    order.status = 'CAPTURED';
    return order;
  }
}

export const paymentGatewayInstance = new PaymentGatewayService();
