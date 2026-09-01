import { IdentityService } from '../backend/identity/identityService';

describe('Phase 1: Identity & Access Management Tests', () => {
  let identityService: IdentityService;

  beforeEach(() => {
    identityService = new IdentityService();
  });

  test('should authenticate demo customer successfully', async () => {
    const authRes = await identityService.login('customer@fincorex.com', 'hash_demo_123');
    expect(authRes.accessToken).toBeDefined();
    expect(authRes.user.role).toBe('CUSTOMER');
    expect(authRes.user.status).toBe('ACTIVE');
  });

  test('should authenticate demo admin successfully', async () => {
    const authRes = await identityService.login('admin@fincorex.com', 'hash_demo_123');
    expect(authRes.user.role).toBe('ADMIN');
  });

  test('should reject invalid credentials', async () => {
    await expect(identityService.login('unknown@fincorex.com', 'bad_pass')).rejects.toThrow('Invalid credentials');
  });

  test('should verify valid 6-digit OTP', async () => {
    const isVerified = await identityService.verifyOTP('customer@fincorex.com', '123456');
    expect(isVerified).toBe(true);
  });
});
