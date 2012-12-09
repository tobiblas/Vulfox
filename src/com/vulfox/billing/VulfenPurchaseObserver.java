package com.vulfox.billing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.vulfox.billing.BillingConstants.PurchaseState;
import com.vulfox.billing.BillingConstants.ResponseCode;
import com.vulfox.billing.BillingService.RequestPurchase;
import com.vulfox.billing.BillingService.RestoreTransactions;
import com.vulfox.util.Logger;
import com.vulfox.util.SharedPrefsUtil;

/**
 * A {@link PurchaseObserver} is used to get callbacks when Android Market sends
 * messages to this application so that we can update the UI.
 */
public class VulfenPurchaseObserver extends PurchaseObserver {

	private Activity mActivity;
	private BillingService mBillingService;
	private BillingObserverListener mBillingListener;

	public VulfenPurchaseObserver(Activity activity, Handler handler,
			BillingService billingService,
			BillingObserverListener billingListener) {
		super(activity, handler);
		mActivity = activity;
		mBillingListener = billingListener;
		mBillingService = billingService;
	}

	/**
	 * If the database has not been initialized, we send a RESTORE_TRANSACTIONS
	 * request to Android Market to get the list of purchased items for this
	 * user. This happens if the application has just been installed or the user
	 * wiped data. We do not want to do this on every startup, rather, we want
	 * to do only when the database needs to be initialized.
	 */
	private void restoreTransactionHistory() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(mActivity.getApplicationContext());
		boolean initialized = prefs.getBoolean(
				SharedPrefsUtil.getTransactionHistoryInitializedKey(), false);
		if (!initialized) {
			mBillingService.restoreTransactions();
		}
	}

	private void addToTransactionHistory(String itemId) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(mActivity.getApplicationContext());
		String transHistory = prefs.getString(
				SharedPrefsUtil.getTransactionHistoryKey(), "");
		Editor editor = prefs.edit();

		transHistory += itemId + ",";
		editor.putString(SharedPrefsUtil.getTransactionHistoryKey(),
				transHistory);
		editor.commit();
	}

	@Override
	public void onBillingSupported(boolean supported, String type) {

		Logger.log("supported: " + supported);

		if (type == null || type.equals(BillingConstants.ITEM_TYPE_INAPP)) {
			if (supported) {
				//We restore transactions here since billing supported is only 
				//checked ONCE for the lifetime of the app.
				restoreTransactionHistory();
				mBillingListener.setBillingEnabled(true);
			} else {
				Logger.log("billing not supported");
			}
		}
	}

	@Override
	public void onPurchaseStateChange(PurchaseState purchaseState,
			String itemId, int quantity, long purchaseTime,
			String developerPayload) {

		Logger.log("onPurchaseStateChange() itemId: " + itemId + " "
				+ purchaseState);

		if (developerPayload == null) {
			Logger.log(itemId + ": " + purchaseState);
		} else {
			Logger.log(itemId + ": " + purchaseState + "\n\t"
					+ developerPayload);
		}

		if (purchaseState == PurchaseState.PURCHASED) {
			Logger.log("PURCHASED! CONGRATZ. ADDING IT TO TRANSACTION HISTORY");
			addToTransactionHistory(itemId);
			// TODO: agera här!
		}

		

	}

	@Override
	public void onRequestPurchaseResponse(RequestPurchase request,
			ResponseCode responseCode) {
		if (BillingConstants.DEBUG) {
			Logger.log(request.mProductId + ": " + responseCode);
		}
		if (responseCode == ResponseCode.RESULT_OK) {
			Logger.log("purchase was successfully sent to server");
			Logger.log("sending purchase request");
		} else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
			Logger.log("user canceled purchase");
			Logger.log("dismissed purchase dialog");
		} else {
			Logger.log("purchase failed");
			Logger.log("request purchase returned " + responseCode);
		}
	}

	@Override
	public void onRestoreTransactionsResponse(RestoreTransactions request,
			ResponseCode responseCode) {
		if (responseCode == ResponseCode.RESULT_OK) {

			Logger.log("completed RestoreTransactions request");

			// Update the shared preferences so that we don't perform
			// a RestoreTransactions again.
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(mActivity);
			Editor editor = prefs.edit();
			editor.putBoolean(
					SharedPrefsUtil.getTransactionHistoryInitializedKey(), true);
			editor.commit();
		} else {
			Logger.log("RestoreTransactions error: " + responseCode);
		}
	}
}
