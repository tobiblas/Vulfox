package com.vulfox.billing;

public interface BillingObserverListener {

	void setBillingEnabled(boolean enabled);
	
	void purchased();
}
