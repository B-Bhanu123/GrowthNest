// FinCoreX Transaction Processing Core Engine

export type TransactionState =
  | 'CREATED'
  | 'INITIATED'
  | 'AUTHORIZED'
  | 'CAPTURED'
  | 'SETTLED'
  | 'FAILED'
  | 'CANCELLED'
  | 'EXPIRED'
  | 'REFUNDED'
  | 'PARTIALLY_REFUNDED';

export interface TransactionRecord {
  transactionId: string;
  idempotencyKey: string;
  senderId: string;
  receiverId: string;
  amount: number;
  currency: string;
  feeAmount: number;
  state: TransactionState;
  referenceCode: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export class TransactionEngine {
  private transactions: Map<string, TransactionRecord> = new Map();
  private idempotencyRegistry: Set<string> = new Set();

  private readonly validTransitions: Record<TransactionState, TransactionState[]> = {
    CREATED: ['INITIATED', 'CANCELLED', 'EXPIRED'],
    INITIATED: ['AUTHORIZED', 'FAILED', 'CANCELLED', 'EXPIRED'],
    AUTHORIZED: ['CAPTURED', 'FAILED', 'CANCELLED', 'EXPIRED'],
    CAPTURED: ['SETTLED', 'REFUNDED', 'PARTIALLY_REFUNDED'],
    SETTLED: ['REFUNDED', 'PARTIALLY_REFUNDED'],
    FAILED: [],
    CANCELLED: [],
    EXPIRED: [],
    REFUNDED: [],
    PARTIALLY_REFUNDED: ['REFUNDED']
  };

  public createTransaction(
    idempotencyKey: string,
    senderId: string,
    receiverId: string,
    amount: number,
    currency: string = 'USD',
    description: string = 'Payment Transfer'
  ): TransactionRecord {
    if (this.idempotencyRegistry.has(idempotencyKey)) {
      throw new Error(`Duplicate transaction idempotency key: ${idempotencyKey}`);
    }

    const txId = `tx_${Date.now()}_${Math.floor(Math.random() * 8999 + 1000)}`;
    const record: TransactionRecord = {
      transactionId: txId,
      idempotencyKey,
      senderId,
      receiverId,
      amount,
      currency,
      feeAmount: Number((amount * 0.015).toFixed(2)),
      state: 'CREATED',
      referenceCode: `REF-${Math.floor(Math.random() * 89999999 + 10000000)}`,
      description,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    this.idempotencyRegistry.add(idempotencyKey);
    this.transactions.set(txId, record);
    return record;
  }

  public transitionState(txId: string, nextState: TransactionState): TransactionRecord {
    const tx = this.transactions.get(txId);
    if (!tx) throw new Error(`Transaction ${txId} not found`);

    const allowed = this.validTransitions[tx.state];
    if (!allowed.includes(nextState)) {
      throw new Error(`Illegal state transition from ${tx.state} to ${nextState}`);
    }

    tx.state = nextState;
    tx.updatedAt = new Date().toISOString();
    return tx;
  }

  public getTransaction(txId: string): TransactionRecord | undefined {
    return this.transactions.get(txId);
  }
}

export const transactionEngineInstance = new TransactionEngine();
