/**
 * FinCoreX Enterprise L1 Caching Engine: Dispute & Chargeback Handling (dispute)
 */

export class DisputeEnterpriseCacheEngine {
  private memoryMap = new Map<string, any>();

  public set(key: string, value: any, ttlSeconds: number = 300): void {
    this.memoryMap.set(key, { value, expiresAt: Date.now() + ttlSeconds * 1000 });
  }

  public get(key: string): any | null {
    const item = this.memoryMap.get(key);
    if (!item) return null;
    if (Date.now() > item.expiresAt) {
      this.memoryMap.delete(key);
      return null;
    }
    return item.value;
  }

  public delete(key: string): boolean {
    return this.memoryMap.delete(key);
  }
}

export const disputeCacheEngineInstance = new DisputeEnterpriseCacheEngine();
