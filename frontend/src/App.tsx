import React, { useState } from 'react';
import {
  Shield,
  CreditCard,
  Send,
  Wallet,
  BookOpen,
  Building2,
  FileText,
  TrendingUp,
  Award,
  Zap,
  PieChart,
  Bell,
  User,
  Lock,
  CheckCircle2,
  AlertCircle,
  Menu,
  X,
  Sparkles,
  ArrowRight,
  RefreshCw,
  LogOut,
  ChevronRight
} from 'lucide-react';

import { identityServiceInstance } from '../backend/identity/identityService';
import { walletServiceInstance } from '../backend/services/walletService';
import { upiServiceInstance } from '../backend/services/upiService';
import { transactionEngineInstance } from '../backend/services/transactionCore';
import { ledgerServiceInstance } from '../backend/services/ledgerService';
import { lendingServiceInstance } from '../backend/services/lendingService';
import { creditScoringEngineInstance } from '../backend/services/creditScoringEngine';
import { fraudDetectionEngineInstance } from '../backend/services/fraudDetectionEngine';
import { investmentServiceInstance } from '../backend/services/investmentService';
import { analyticsEngineInstance } from '../backend/services/analyticsEngine';
import { adminOpsServiceInstance } from '../backend/services/adminOpsService';

export type ActiveTab =
  | 'overview'
  | 'payment_gateway'
  | 'upi_transfers'
  | 'wallet'
  | 'ledger'
  | 'settlement'
  | 'lending_credit'
  | 'investments'
  | 'insurance'
  | 'fraud_engine'
  | 'accounting_expense'
  | 'analytics'
  | 'audit_admin';

export function App() {
  // Pre-filled login state for instant 1-click access
  const [isAuthenticated, setIsAuthenticated] = useState(true);
  const [userEmail, setUserEmail] = useState('customer@fincorex.com');
  const [userRole, setUserRole] = useState<'CUSTOMER' | 'MERCHANT' | 'ADMIN'>('CUSTOMER');
  const [activeTab, setActiveTab] = useState<ActiveTab>('overview');
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [notificationCount, setNotificationCount] = useState(3);
  const [showNotificationPopup, setShowNotificationPopup] = useState(false);

  // Demo State & Actions
  const [wallet, setWallet] = useState(walletServiceInstance.getWalletByCustomer('cust_demo_001')!);
  const [topUpAmount, setTopUpAmount] = useState('');
  const [upiPayee, setUpiPayee] = useState('techcorp@fincorex');
  const [upiAmount, setUpiAmount] = useState('150.00');
  const [upiStatusMsg, setUpiStatusMsg] = useState('');
  
  // Payment Order Checkout state
  const [checkoutAmount, setCheckoutAmount] = useState('299.99');
  const [checkoutMethod, setCheckoutMethod] = useState<'CARD' | 'UPI' | 'WALLET'>('CARD');
  const [checkoutResult, setCheckoutResult] = useState<any>(null);

  // Loan & Credit State
  const [loanPrincipal, setLoanPrincipal] = useState('15000');
  const [loanTenure, setLoanTenure] = useState('24');
  const [loanEMI, setLoanEMI] = useState<number | null>(719.86);

  // Fraud State
  const [fraudTxAmount, setFraudTxAmount] = useState('12000');
  const [fraudIP, setFraudIP] = useState('192.168.1.99');
  const [fraudVelocity, setFraudVelocity] = useState('6');
  const [fraudResult, setFraudResult] = useState<any>(null);

  // Quick 1-Click Login Handler
  const handleQuickLogin = (role: 'CUSTOMER' | 'MERCHANT' | 'ADMIN') => {
    const email = role === 'CUSTOMER' ? 'customer@fincorex.com' : role === 'MERCHANT' ? 'merchant@fincorex.com' : 'admin@fincorex.com';
    setUserEmail(email);
    setUserRole(role);
    setIsAuthenticated(true);
  };

  const handleWalletTopUp = (e: React.FormEvent) => {
    e.preventDefault();
    const val = parseFloat(topUpAmount);
    if (isNaN(val) || val <= 0) return;
    const updated = walletServiceInstance.topUpWallet(wallet.walletId, val);
    setWallet({ ...updated });
    setTopUpAmount('');
  };

  const handleUPITransfer = (e: React.FormEvent) => {
    e.preventDefault();
    const amt = parseFloat(upiAmount);
    if (isNaN(amt) || amt <= 0) return;
    const alias = upiServiceInstance.resolveVPA(upiPayee);
    if (!alias) {
      setUpiStatusMsg(`❌ VPA "${upiPayee}" not found on network.`);
      return;
    }
    const tx = transactionEngineInstance.createTransaction(
      `upi_idem_${Date.now()}`,
      wallet.customerId,
      alias.ownerId,
      amt,
      'USD',
      `UPI P2P Transfer to ${upiPayee}`
    );
    setUpiStatusMsg(`✅ Transfer of $${amt.toFixed(2)} to ${upiPayee} successful! Ref: ${tx.referenceCode}`);
  };

  const handleExecuteCheckout = (e: React.FormEvent) => {
    e.preventDefault();
    const amt = parseFloat(checkoutAmount);
    if (isNaN(amt) || amt <= 0) return;
    const tx = transactionEngineInstance.createTransaction(
      `chk_idem_${Date.now()}`,
      'cust_demo_001',
      'mer_demo_001',
      amt,
      'USD',
      `Merchant Online Checkout (${checkoutMethod})`
    );
    const authTx = transactionEngineInstance.transitionState(tx.transactionId, 'INITIATED');
    const capTx = transactionEngineInstance.transitionState(authTx.transactionId, 'AUTHORIZED');
    const setTx = transactionEngineInstance.transitionState(capTx.transactionId, 'CAPTURED');

    setCheckoutResult({
      orderId: setTx.transactionId,
      ref: setTx.referenceCode,
      status: setTx.state,
      amount: setTx.amount,
      fee: setTx.feeAmount
    });
  };

  const handleCalculateEMI = () => {
    const p = parseFloat(loanPrincipal);
    const t = parseInt(loanTenure, 10);
    if (isNaN(p) || isNaN(t)) return;
    const emi = lendingServiceInstance.calculateEMI(p, 0.085, t);
    setLoanEMI(emi);
  };

  const handleEvaluateFraud = () => {
    const amt = parseFloat(fraudTxAmount);
    const vel = parseInt(fraudVelocity, 10);
    const res = fraudDetectionEngineInstance.evaluateTransaction(
      `tx_eval_${Date.now()}`,
      'cust_demo_001',
      amt,
      fraudIP,
      vel,
      true
    );
    setFraudResult(res);
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col font-sans selection:bg-indigo-500 selection:text-white">
      {/* Top Header Bar */}
      <header className="sticky top-0 z-40 bg-slate-900/80 backdrop-blur-md border-b border-slate-800/80 px-4 lg:px-8 py-3.5 flex items-center justify-between">
        <div className="flex items-center gap-4">
          <button
            onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
            className="lg:hidden p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-xl"
          >
            <Menu className="w-5 h-5" />
          </button>
          <div className="flex items-center gap-3 cursor-pointer" onClick={() => setActiveTab('overview')}>
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-500 via-purple-600 to-pink-500 p-0.5 shadow-lg shadow-indigo-500/20">
              <div className="w-full h-full bg-slate-950 rounded-[10px] flex items-center justify-center">
                <Shield className="w-5 h-5 text-indigo-400" />
              </div>
            </div>
            <div>
              <div className="flex items-center gap-2">
                <h1 className="font-black text-lg text-white tracking-tight">FinCore<span className="text-indigo-400">X</span></h1>
                <span className="px-2 py-0.5 rounded-full text-[10px] font-bold bg-indigo-500/10 text-indigo-400 border border-indigo-500/20">GrowthNest</span>
              </div>
              <p className="text-[11px] text-slate-400 font-medium">Unified Financial Transaction Engine</p>
            </div>
          </div>
        </div>

        {/* Quick Role Switcher & User Profile */}
        <div className="flex items-center gap-3">
          <div className="hidden sm:flex items-center bg-slate-900 border border-slate-800 rounded-xl p-1 text-xs font-semibold">
            <button
              onClick={() => handleQuickLogin('CUSTOMER')}
              className={`px-3 py-1.5 rounded-lg transition ${userRole === 'CUSTOMER' ? 'bg-indigo-600 text-white shadow-md' : 'text-slate-400 hover:text-white'}`}
            >
              Customer
            </button>
            <button
              onClick={() => handleQuickLogin('MERCHANT')}
              className={`px-3 py-1.5 rounded-lg transition ${userRole === 'MERCHANT' ? 'bg-indigo-600 text-white shadow-md' : 'text-slate-400 hover:text-white'}`}
            >
              Merchant
            </button>
            <button
              onClick={() => handleQuickLogin('ADMIN')}
              className={`px-3 py-1.5 rounded-lg transition ${userRole === 'ADMIN' ? 'bg-indigo-600 text-white shadow-md' : 'text-slate-400 hover:text-white'}`}
            >
              Admin Ops
            </button>
          </div>

          <div className="relative">
            <button
              onClick={() => setShowNotificationPopup(!showNotificationPopup)}
              className="p-2.5 bg-slate-900 border border-slate-800 hover:border-slate-700 text-slate-300 rounded-xl transition relative"
            >
              <Bell className="w-4 h-4" />
              {notificationCount > 0 && (
                <span className="absolute -top-1 -right-1 w-4 h-4 rounded-full bg-pink-500 text-white text-[10px] font-bold flex items-center justify-center">
                  {notificationCount}
                </span>
              )}
            </button>

            {showNotificationPopup && (
              <div className="absolute right-0 mt-2 w-80 bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl p-4 z-50">
                <div className="flex items-center justify-between mb-3 pb-2 border-b border-slate-800">
                  <h4 className="font-bold text-xs text-white">System Notifications</h4>
                  <button onClick={() => setNotificationCount(0)} className="text-[10px] text-indigo-400 font-bold hover:underline">Clear all</button>
                </div>
                <div className="space-y-2 text-xs">
                  <div className="p-2.5 bg-slate-800/50 rounded-xl border border-slate-800">
                    <div className="font-bold text-emerald-400">Payment Captured</div>
                    <div className="text-slate-400 mt-0.5">Order #pay_ord_9912 authorized & settled ($299.99)</div>
                  </div>
                  <div className="p-2.5 bg-slate-800/50 rounded-xl border border-slate-800">
                    <div className="font-bold text-indigo-400">KYC Status Verified</div>
                    <div className="text-slate-400 mt-0.5">Customer KYC updated to Tier-3 Verified</div>
                  </div>
                </div>
              </div>
            )}
          </div>

          <div className="flex items-center gap-2 pl-2 border-l border-slate-800">
            <div className="w-8 h-8 rounded-full bg-indigo-500/20 border border-indigo-500/30 flex items-center justify-center text-indigo-300 font-bold text-xs">
              {userEmail[0].toUpperCase()}
            </div>
            <div className="hidden md:block text-left text-xs">
              <div className="font-bold text-white leading-none">{userEmail.split('@')[0]}</div>
              <div className="text-[10px] text-indigo-400 font-semibold mt-0.5">{userRole}</div>
            </div>
          </div>
        </div>
      </header>

      {/* Main Workspace Body */}
      <div className="flex-1 flex overflow-hidden">
        {/* Navigation Sidebar */}
        <aside className={`fixed inset-y-0 left-0 z-30 w-64 bg-slate-900/90 backdrop-blur-lg border-r border-slate-800/80 transform ${isMobileMenuOpen ? 'translate-x-0' : '-translate-x-full'} lg:translate-x-0 transition-transform duration-200 ease-in-out flex flex-col pt-16 lg:pt-0`}>
          <div className="p-4 space-y-1 overflow-y-auto flex-1 text-xs font-semibold">
            <div className="px-3 py-2 text-[10px] font-bold uppercase tracking-wider text-slate-500">Main Platform</div>
            
            <button
              onClick={() => { setActiveTab('overview'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'overview' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <PieChart className="w-4 h-4" /> Overview Dashboard
            </button>

            <button
              onClick={() => { setActiveTab('payment_gateway'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'payment_gateway' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <CreditCard className="w-4 h-4" /> Payment Gateway
            </button>

            <button
              onClick={() => { setActiveTab('upi_transfers'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'upi_transfers' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <Send className="w-4 h-4" /> UPI Transfers
            </button>

            <button
              onClick={() => { setActiveTab('wallet'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'wallet' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <Wallet className="w-4 h-4" /> Stored-Value Wallet
            </button>

            <div className="px-3 py-2 mt-4 text-[10px] font-bold uppercase tracking-wider text-slate-500">Core Financial Ledger</div>

            <button
              onClick={() => { setActiveTab('ledger'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'ledger' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <BookOpen className="w-4 h-4" /> Double-Entry Ledger
            </button>

            <button
              onClick={() => { setActiveTab('settlement'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'settlement' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <Building2 className="w-4 h-4" /> Settlement & Reconcile
            </button>

            <div className="px-3 py-2 mt-4 text-[10px] font-bold uppercase tracking-wider text-slate-500">Financial Services</div>

            <button
              onClick={() => { setActiveTab('lending_credit'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'lending_credit' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <FileText className="w-4 h-4" /> Lending & Credit Score
            </button>

            <button
              onClick={() => { setActiveTab('investments'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'investments' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <TrendingUp className="w-4 h-4" /> Investment Platform
            </button>

            <button
              onClick={() => { setActiveTab('insurance'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'insurance' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <Award className="w-4 h-4" /> Insurance System
            </button>

            <div className="px-3 py-2 mt-4 text-[10px] font-bold uppercase tracking-wider text-slate-500">Intelligence & Ops</div>

            <button
              onClick={() => { setActiveTab('fraud_engine'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'fraud_engine' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <Shield className="w-4 h-4" /> Fraud Engine
            </button>

            <button
              onClick={() => { setActiveTab('analytics'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'analytics' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <Zap className="w-4 h-4" /> Financial Analytics
            </button>

            <button
              onClick={() => { setActiveTab('audit_admin'); setIsMobileMenuOpen(false); }}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition ${activeTab === 'audit_admin' ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-600/30' : 'text-slate-400 hover:text-white hover:bg-slate-800/60'}`}
            >
              <Lock className="w-4 h-4" /> Audit & Admin Controls
            </button>
          </div>

          <div className="p-4 border-t border-slate-800/80 bg-slate-950/40 text-xs">
            <div className="flex items-center justify-between text-slate-400 mb-1">
              <span>PostgreSQL Schema</span>
              <span className="font-mono text-indigo-400 text-[10px] font-bold">24 Domains</span>
            </div>
            <div className="flex items-center justify-between text-slate-400">
              <span>Ledger Invariant</span>
              <span className="font-mono text-emerald-400 text-[10px] font-bold">Dr = Cr ✓</span>
            </div>
          </div>
        </aside>

        {/* Primary Content View Area */}
        <main className="flex-1 overflow-y-auto lg:ml-64 p-4 lg:p-8 space-y-6">
          {/* TAB 1: OVERVIEW DASHBOARD */}
          {activeTab === 'overview' && (
            <div className="space-y-6">
              <div className="bg-gradient-to-r from-indigo-900/60 via-purple-900/40 to-slate-900 p-6 lg:p-8 rounded-3xl border border-indigo-500/20 shadow-2xl relative overflow-hidden">
                <div className="relative z-10 max-w-2xl">
                  <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-500/20 text-indigo-300 text-xs font-bold mb-3 border border-indigo-500/30">
                    <Sparkles className="w-3.5 h-3.5" /> Welcome to FinCoreX Platform
                  </div>
                  <h2 className="text-3xl lg:text-4xl font-black text-white tracking-tight leading-tight">
                    Unified Financial Transaction Engine
                  </h2>
                  <p className="text-sm text-slate-300 mt-2 leading-relaxed">
                    Managing the complete lifecycle of customer wallets, payment orchestration, UPI instant transfers, double-entry ledger accounting, credit scoring, lending, and real-time fraud intelligence.
                  </p>

                  <div className="flex flex-wrap items-center gap-3 mt-6">
                    <button
                      onClick={() => setActiveTab('payment_gateway')}
                      className="px-5 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl text-xs font-bold flex items-center gap-2 transition shadow-lg shadow-indigo-600/30"
                    >
                      Test Payment Checkout <ArrowRight className="w-4 h-4" />
                    </button>
                    <button
                      onClick={() => setActiveTab('upi_transfers')}
                      className="px-5 py-2.5 bg-slate-800 hover:bg-slate-700 text-slate-200 rounded-xl text-xs font-bold flex items-center gap-2 transition border border-slate-700"
                    >
                      Instant UPI Send <Send className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              </div>

              {/* High-Level Metrics */}
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                <div className="bg-slate-900/60 border border-slate-800 p-5 rounded-2xl">
                  <div className="text-xs text-slate-400 font-semibold uppercase tracking-wider">Gross Merchandise Volume (GMV)</div>
                  <div className="text-2xl font-black text-white mt-2">$14,850,900.50</div>
                  <div className="text-xs text-emerald-400 font-bold mt-1">↑ +18.4% monthly volume</div>
                </div>

                <div className="bg-slate-900/60 border border-slate-800 p-5 rounded-2xl">
                  <div className="text-xs text-slate-400 font-semibold uppercase tracking-wider">Stored-Value Wallet Balance</div>
                  <div className="text-2xl font-black text-emerald-400 mt-2">${wallet.availableBalance.toLocaleString(undefined, { minimumFractionDigits: 2 })}</div>
                  <div className="text-xs text-slate-400 font-bold mt-1">Reserved: ${wallet.reservedBalance.toFixed(2)}</div>
                </div>

                <div className="bg-slate-900/60 border border-slate-800 p-5 rounded-2xl">
                  <div className="text-xs text-slate-400 font-semibold uppercase tracking-wider">Payment Success SLA</div>
                  <div className="text-2xl font-black text-indigo-400 mt-2">99.42%</div>
                  <div className="text-xs text-indigo-400 font-bold mt-1">Zero downtime SLA</div>
                </div>

                <div className="bg-slate-900/60 border border-slate-800 p-5 rounded-2xl">
                  <div className="text-xs text-slate-400 font-semibold uppercase tracking-wider">Fraud Risk Rate</div>
                  <div className="text-2xl font-black text-pink-400 mt-2">0.08%</div>
                  <div className="text-xs text-slate-400 font-bold mt-1">Real-time AI Shield Active</div>
                </div>
              </div>
            </div>
          )}

          {/* TAB 2: PAYMENT GATEWAY */}
          {activeTab === 'payment_gateway' && (
            <div className="space-y-6">
              <div className="bg-slate-900/60 border border-slate-800 p-6 rounded-2xl">
                <h3 className="font-extrabold text-lg text-white mb-1">Payment Gateway & Orchestration Demo</h3>
                <p className="text-xs text-slate-400 mb-6">Test the payment state machine: CREATED → INITIATED → AUTHORIZED → CAPTURED → SETTLED</p>

                <form onSubmit={handleExecuteCheckout} className="max-w-xl space-y-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-300 mb-1">Checkout Amount ($)</label>
                    <input
                      type="number"
                      step="0.01"
                      value={checkoutAmount}
                      onChange={e => setCheckoutAmount(e.target.value)}
                      className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2.5 text-sm text-white font-mono focus:outline-none focus:border-indigo-500"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-bold text-slate-300 mb-1">Payment Instrument Method</label>
                    <div className="grid grid-cols-3 gap-3">
                      {(['CARD', 'UPI', 'WALLET'] as const).map(method => (
                        <button
                          key={method}
                          type="button"
                          onClick={() => setCheckoutMethod(method)}
                          className={`py-2.5 rounded-xl text-xs font-bold border transition ${checkoutMethod === method ? 'bg-indigo-600 text-white border-indigo-500' : 'bg-slate-950 text-slate-400 border-slate-800 hover:border-slate-700'}`}
                        >
                          {method}
                        </button>
                      ))}
                    </div>
                  </div>

                  <button
                    type="submit"
                    className="w-full py-3 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl text-xs font-bold transition shadow-lg shadow-indigo-600/30"
                  >
                    Authorize & Capture Payment
                  </button>
                </form>

                {checkoutResult && (
                  <div className="mt-6 p-4 bg-emerald-500/10 border border-emerald-500/20 rounded-2xl text-xs space-y-2">
                    <div className="font-extrabold text-emerald-400 flex items-center gap-2">
                      <CheckCircle2 className="w-4 h-4" /> Payment Order Successfully Processed
                    </div>
                    <div className="font-mono text-slate-300 space-y-1">
                      <div>Order ID: <span className="text-white font-bold">{checkoutResult.orderId}</span></div>
                      <div>Ref Code: <span className="text-white font-bold">{checkoutResult.ref}</span></div>
                      <div>Final State: <span className="text-emerald-400 font-bold">{checkoutResult.status}</span></div>
                      <div>Settlement Fee (1.5% MDR): <span className="text-white font-bold">${checkoutResult.fee.toFixed(2)}</span></div>
                    </div>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* TAB 3: UPI TRANSFERS */}
          {activeTab === 'upi_transfers' && (
            <div className="space-y-6">
              <div className="bg-slate-900/60 border border-slate-800 p-6 rounded-2xl">
                <h3 className="font-extrabold text-lg text-white mb-1">UPI Instant Transfer Network Simulation</h3>
                <p className="text-xs text-slate-400 mb-6">Virtual Payment Address (VPA) alias resolution and peer-to-peer settlement rails.</p>

                <form onSubmit={handleUPITransfer} className="max-w-xl space-y-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-300 mb-1">Recipient VPA Address</label>
                    <input
                      type="text"
                      value={upiPayee}
                      onChange={e => setUpiPayee(e.target.value)}
                      className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2.5 text-sm text-white font-mono focus:outline-none focus:border-indigo-500"
                    />
                    <span className="text-[10px] text-slate-500">Available demo VPAs: <code className="text-indigo-400">alex@fincorex</code>, <code className="text-indigo-400">techcorp@fincorex</code></span>
                  </div>

                  <div>
                    <label className="block text-xs font-bold text-slate-300 mb-1">Transfer Amount ($)</label>
                    <input
                      type="number"
                      step="0.01"
                      value={upiAmount}
                      onChange={e => setUpiAmount(e.target.value)}
                      className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2.5 text-sm text-white font-mono focus:outline-none focus:border-indigo-500"
                    />
                  </div>

                  <button
                    type="submit"
                    className="w-full py-3 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl text-xs font-bold transition shadow-lg shadow-indigo-600/30"
                  >
                    Execute Instant UPI Transfer
                  </button>
                </form>

                {upiStatusMsg && (
                  <div className="mt-4 p-3 bg-slate-950 border border-slate-800 rounded-xl text-xs font-mono text-slate-200">
                    {upiStatusMsg}
                  </div>
                )}
              </div>
            </div>
          )}

          {/* TAB 4: STORED-VALUE WALLET */}
          {activeTab === 'wallet' && (
            <div className="space-y-6">
              <div className="bg-slate-900/60 border border-slate-800 p-6 rounded-2xl">
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
                  <div>
                    <h3 className="font-extrabold text-lg text-white">Stored-Value Digital Wallet</h3>
                    <p className="text-xs text-slate-400">Customer ID: {wallet.customerId} • Tier: {wallet.tier}</p>
                  </div>
                  <div className="text-right">
                    <div className="text-xs text-slate-400 font-semibold">Available Balance</div>
                    <div className="text-3xl font-black text-emerald-400">${wallet.availableBalance.toLocaleString(undefined, { minimumFractionDigits: 2 })}</div>
                  </div>
                </div>

                <form onSubmit={handleWalletTopUp} className="max-w-md flex items-center gap-3 mb-6">
                  <input
                    type="number"
                    placeholder="Enter amount to top up ($)"
                    value={topUpAmount}
                    onChange={e => setTopUpAmount(e.target.value)}
                    className="flex-1 bg-slate-950 border border-slate-800 rounded-xl px-4 py-2 text-xs text-white focus:outline-none focus:border-indigo-500 font-mono"
                  />
                  <button
                    type="submit"
                    className="px-5 py-2.5 bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl text-xs font-bold transition"
                  >
                    Top Up Wallet
                  </button>
                </form>

                <h4 className="font-bold text-xs text-white uppercase tracking-wider mb-3">Wallet Statement History</h4>
                <div className="space-y-2">
                  {walletServiceInstance.getHistory(wallet.walletId).map(tx => (
                    <div key={tx.id} className="p-3 bg-slate-950 border border-slate-800/80 rounded-xl flex items-center justify-between text-xs">
                      <div>
                        <div className="font-bold text-white">{tx.type} • {tx.reference}</div>
                        <div className="text-[10px] text-slate-500">{new Date(tx.timestamp).toLocaleString()}</div>
                      </div>
                      <div className="text-right font-mono font-bold text-emerald-400">
                        +${tx.amount.toFixed(2)}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {/* TAB 5: DOUBLE-ENTRY LEDGER */}
          {activeTab === 'ledger' && (
            <div className="space-y-6">
              <div className="bg-slate-900/60 border border-slate-800 p-6 rounded-2xl">
                <h3 className="font-extrabold text-lg text-white mb-1">Double-Entry Financial Ledger</h3>
                <p className="text-xs text-slate-400 mb-6">Immutable accounting journal entries enforcing invariant: Total Debits = Total Credits.</p>

                <div className="space-y-3">
                  {ledgerServiceInstance.getAccounts().map(acc => (
                    <div key={acc.code} className="p-4 bg-slate-950 border border-slate-800 rounded-xl flex items-center justify-between text-xs">
                      <div>
                        <div className="font-bold text-white">{acc.code} — {acc.name}</div>
                        <div className="text-[10px] text-indigo-400 font-bold mt-0.5">{acc.type}</div>
                      </div>
                      <div className="font-mono text-sm font-bold text-white">
                        ${acc.balance.toLocaleString(undefined, { minimumFractionDigits: 2 })}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {/* TAB 6: LENDING & CREDIT */}
          {activeTab === 'lending_credit' && (
            <div className="space-y-6">
              <div className="bg-slate-900/60 border border-slate-800 p-6 rounded-2xl">
                <h3 className="font-extrabold text-lg text-white mb-1">Loan Calculator & Credit Scoring Engine</h3>
                <p className="text-xs text-slate-400 mb-6">Underwriting, EMI schedule generation, and explainable credit risk scores.</p>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="space-y-4">
                    <h4 className="font-bold text-xs text-white uppercase tracking-wider">EMI Repayment Calculator</h4>
                    <div>
                      <label className="block text-xs font-bold text-slate-400 mb-1">Principal Amount ($)</label>
                      <input
                        type="number"
                        value={loanPrincipal}
                        onChange={e => setLoanPrincipal(e.target.value)}
                        className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2 text-xs text-white font-mono"
                      />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-400 mb-1">Tenure (Months)</label>
                      <input
                        type="number"
                        value={loanTenure}
                        onChange={e => setLoanTenure(e.target.value)}
                        className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2 text-xs text-white font-mono"
                      />
                    </div>
                    <button
                      onClick={handleCalculateEMI}
                      className="w-full py-2.5 bg-indigo-600 text-white rounded-xl text-xs font-bold"
                    >
                      Calculate EMI
                    </button>
                    {loanEMI !== null && (
                      <div className="p-3 bg-indigo-500/10 border border-indigo-500/20 rounded-xl text-xs">
                        Monthly EMI: <span className="font-bold font-mono text-indigo-400">${loanEMI.toFixed(2)}/mo</span>
                      </div>
                    )}
                  </div>

                  <div className="p-4 bg-slate-950 border border-slate-800 rounded-2xl space-y-3">
                    <h4 className="font-bold text-xs text-white uppercase tracking-wider">Credit Profile Summary</h4>
                    <div className="flex items-center justify-between">
                      <span className="text-xs text-slate-400">Credit Score</span>
                      <span className="text-2xl font-black text-emerald-400 font-mono">742 / 850</span>
                    </div>
                    <div className="flex items-center justify-between text-xs">
                      <span className="text-slate-400">Risk Category</span>
                      <span className="font-bold text-emerald-400">LOW</span>
                    </div>
                    <div className="flex items-center justify-between text-xs">
                      <span className="text-slate-400">Debt-To-Income (DTI)</span>
                      <span className="font-mono text-white">17.65%</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* TAB 7: FRAUD ENGINE */}
          {activeTab === 'fraud_engine' && (
            <div className="space-y-6">
              <div className="bg-slate-900/60 border border-slate-800 p-6 rounded-2xl">
                <h3 className="font-extrabold text-lg text-white mb-1">Real-Time Fraud Detection Engine</h3>
                <p className="text-xs text-slate-400 mb-6">Rules evaluation, IP blacklisting, velocity checks, and decision scoring (ALLOW / REVIEW / BLOCK).</p>

                <div className="max-w-xl space-y-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-300 mb-1">Transaction Amount ($)</label>
                    <input
                      type="number"
                      value={fraudTxAmount}
                      onChange={e => setFraudTxAmount(e.target.value)}
                      className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2 text-xs text-white font-mono"
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-300 mb-1">Origin IP Address</label>
                    <input
                      type="text"
                      value={fraudIP}
                      onChange={e => setFraudIP(e.target.value)}
                      className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2 text-xs text-white font-mono"
                    />
                    <span className="text-[10px] text-slate-500">Try blacklisted IP: <code className="text-pink-400">192.168.1.99</code></span>
                  </div>

                  <button
                    onClick={handleEvaluateFraud}
                    className="w-full py-2.5 bg-pink-600 hover:bg-pink-500 text-white rounded-xl text-xs font-bold transition"
                  >
                    Run Fraud Risk Evaluation
                  </button>

                  {fraudResult && (
                    <div className="p-4 bg-slate-950 border border-slate-800 rounded-2xl text-xs space-y-2">
                      <div className="flex items-center justify-between">
                        <span className="font-bold text-white">Risk Score: {fraudResult.riskScore}/100</span>
                        <span className={`font-bold px-2 py-0.5 rounded text-[10px] ${fraudResult.decision === 'BLOCK' ? 'bg-pink-500/20 text-pink-400' : 'bg-emerald-500/20 text-emerald-400'}`}>
                          {fraudResult.decision}
                        </span>
                      </div>
                      {fraudResult.triggeredRules.map((rule: string, i: number) => (
                        <div key={i} className="text-[11px] text-pink-300">• {rule}</div>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            </div>
          )}

          {/* TAB 8: AUDIT & ADMIN */}
          {activeTab === 'audit_admin' && (
            <div className="space-y-6">
              <div className="bg-slate-900/60 border border-slate-800 p-6 rounded-2xl">
                <h3 className="font-extrabold text-lg text-white mb-1">Audit & Operations Center</h3>
                <p className="text-xs text-slate-400 mb-6">System feature flags control and immutable compliance audit log tracking.</p>

                <h4 className="font-bold text-xs text-white uppercase tracking-wider mb-3">Feature Flags</h4>
                <div className="space-y-2 mb-6">
                  {adminOpsServiceInstance.getFeatureFlags().map(flag => (
                    <div key={flag.key} className="p-3 bg-slate-950 border border-slate-800 rounded-xl flex items-center justify-between text-xs">
                      <div>
                        <div className="font-bold text-white">{flag.key}</div>
                        <div className="text-slate-400 text-[11px]">{flag.description}</div>
                      </div>
                      <button
                        onClick={() => adminOpsServiceInstance.toggleFeatureFlag(flag.key)}
                        className={`px-3 py-1 rounded-lg font-bold text-[10px] ${flag.isEnabled ? 'bg-emerald-500/20 text-emerald-400' : 'bg-slate-800 text-slate-500'}`}
                      >
                        {flag.isEnabled ? 'ENABLED' : 'DISABLED'}
                      </button>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}
        </main>
      </div>
    </div>
  );
}

export default App;
