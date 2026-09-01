import React, { useState } from 'react';
import { Activity, RefreshCw } from 'lucide-react';

/**
 * FinCoreX Domain Dashboard View Component: Investment & Portfolio Platform
 */
export const InvestmentDomainView: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'overview' | 'records' | 'analytics' | 'config'>('overview');
  const [searchTerm, setSearchTerm] = useState('');
  const [isProcessing, setIsProcessing] = useState(false);

  const mockRecords = [
    { id: 'investment_101', ref: 'REF-INVESTMENT-8821', owner: 'Alex Vance', amount: 1450.00, status: 'COMPLETED', time: '10 mins ago' },
    { id: 'investment_102', ref: 'REF-INVESTMENT-8822', owner: 'TechCorp Global', amount: 8900.50, status: 'PROCESSING', time: '25 mins ago' },
    { id: 'investment_103', ref: 'REF-INVESTMENT-8823', owner: 'Elena Rostova', amount: 320.00, status: 'COMPLETED', time: '1 hour ago' },
    { id: 'investment_104', ref: 'REF-INVESTMENT-8824', owner: 'Marcus Aurelius', amount: 12500.00, status: 'AUDITED', time: '3 hours ago' }
  ];

  const handleRefresh = () => {
    setIsProcessing(true);
    setTimeout(() => setIsProcessing(false), 600);
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-slate-900/60 backdrop-blur border border-slate-800 p-6 rounded-2xl">
        <div>
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 text-xs font-semibold mb-2">
            <Activity className="w-3.5 h-3.5" /> Domain Microservice: INVESTMENT
          </div>
          <h2 className="text-2xl font-extrabold text-white tracking-tight">Investment & Portfolio Platform</h2>
          <p className="text-sm text-slate-400 mt-1">High-throughput operational controller and real-time ledger monitor.</p>
        </div>
        <div className="flex items-center gap-3">
          <button 
            onClick={handleRefresh} 
            disabled={isProcessing}
            className="flex items-center gap-2 px-4 py-2 bg-slate-800 hover:bg-slate-700 text-slate-200 rounded-xl text-sm font-semibold transition border border-slate-700"
          >
            <RefreshCw className={`w-4 h-4 ${isProcessing ? 'animate-spin' : ''}`} /> Refresh State
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="bg-slate-900/40 border border-slate-800 p-5 rounded-2xl">
          <div className="text-xs text-slate-500 font-semibold uppercase tracking-wider">Total Throughput</div>
          <div className="text-2xl font-black text-white mt-2">$2,485,900</div>
          <div className="text-xs text-emerald-400 font-bold mt-1">↑ +14.2% vs last period</div>
        </div>
        <div className="bg-slate-900/40 border border-slate-800 p-5 rounded-2xl">
          <div className="text-xs text-slate-500 font-semibold uppercase tracking-wider">Active State Count</div>
          <div className="text-2xl font-black text-white mt-2">1,248</div>
          <div className="text-xs text-slate-400 font-bold mt-1">Zero latency queuing</div>
        </div>
        <div className="bg-slate-900/40 border border-slate-800 p-5 rounded-2xl">
          <div className="text-xs text-slate-500 font-semibold uppercase tracking-wider">Success Rate</div>
          <div className="text-2xl font-black text-emerald-400 mt-2">99.98%</div>
          <div className="text-xs text-emerald-500 font-bold mt-1">SLA Compliant</div>
        </div>
        <div className="bg-slate-900/40 border border-slate-800 p-5 rounded-2xl">
          <div className="text-xs text-slate-500 font-semibold uppercase tracking-wider">Audit Security Index</div>
          <div className="text-2xl font-black text-indigo-400 mt-2">100 / 100</div>
          <div className="text-xs text-indigo-400 font-bold mt-1">Immutable Logs Enabled</div>
        </div>
      </div>

      <div className="bg-slate-900/50 border border-slate-800 rounded-2xl overflow-hidden">
        <div className="p-4 border-b border-slate-800 flex items-center justify-between">
          <h3 className="font-bold text-white text-base">Live Investment & Portfolio Platform Activity Stream</h3>
          <input
            type="text"
            placeholder="Search records or refs..."
            value={searchTerm}
            onChange={e => setSearchTerm(e.target.value)}
            className="bg-slate-800 border border-slate-700 text-white text-xs px-3 py-1.5 rounded-lg focus:outline-none focus:border-indigo-500 w-64"
          />
        </div>
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-950/60 text-xs font-bold text-slate-400 uppercase border-b border-slate-800">
              <tr>
                <th className="px-6 py-3">Record ID</th>
                <th className="px-6 py-3">Reference Code</th>
                <th className="px-6 py-3">Owner Identity</th>
                <th className="px-6 py-3">Financial Value</th>
                <th className="px-6 py-3">Execution Status</th>
                <th className="px-6 py-3">Timestamp</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60 font-mono text-xs">
              {mockRecords.map(rec => (
                <tr key={rec.id} className="hover:bg-slate-800/40 transition">
                  <td className="px-6 py-4 font-bold text-indigo-400">{rec.id}</td>
                  <td className="px-6 py-4 text-white font-semibold">{rec.ref}</td>
                  <td className="px-6 py-4 font-sans text-slate-200">{rec.owner}</td>
                  <td className="px-6 py-4 font-bold text-emerald-400">${rec.amount.toLocaleString(undefined, { minimumFractionDigits: 2 })}</td>
                  <td className="px-6 py-4">
                    <span className={`px-2.5 py-1 rounded-full text-[10px] font-bold border ${
                      rec.status === 'COMPLETED' ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20' : 'bg-indigo-500/10 text-indigo-400 border-indigo-500/20'
                    }`}>
                      {rec.status}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-slate-500 font-sans">{rec.time}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default InvestmentDomainView;
