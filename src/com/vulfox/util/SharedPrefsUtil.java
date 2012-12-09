package com.vulfox.util;

public final class SharedPrefsUtil {

	private SharedPrefsUtil() {
	}

	/**
	 * Key used to determine if we should get transaction history from google play or not.
	 * @return
	 */
	public static String getBillingSupportedKey() {
		return "billingSupported";
	}
	
	/**
	 * Key used to determine if we should get transaction history from google play or not.
	 * @return
	 */
	public static String getTransactionHistoryInitializedKey() {
		return "transactionHistoryInitialized";
	}
	
	/**
	 * Key used to get transaction history.
	 * @return
	 */
	public static String getTransactionHistoryKey() {
		return "transactionHistory";
	}
	
	/**
	 * Vibrator enabled shared prefs key
	 */
	public static String getVibratorEnabledPrefsKey() {
		return "vibrate";
	}
	
	/**
	 * Sound enabled shared prefs key
	 */
	public static String getSoundEnabledPrefsKey() {
		return "sound";
	}
	
	/**
	 * Saved topscore for level and world combo
	 */
	public static String getTopScorePrefsKey(int worldNum, int levelNum) {
		return "TopScore_" + worldNum + "_" + levelNum;
	}
	
	/**
	 * Saved number of stars for world and level combo
	 */
	public static String getStarsPrefsKey(int world, int level) {
		return "topStars_" + world + "_" + level;
	}
	
	/**
	 * Saved number of stars for world and level combo
	 */
	public static String getAlienShownForWorld2Level1Key() {
		return "alienShownForWorld2Level1";
	}
	
	/**
	 * Key for how many worlds have been unlocked.
	 */
	public static String getUnlockedWorldsPrefsKey() {
		return "worldsunlocked";
	}
	
	/**
	 * Key for how many levels have been unlocked for a world.
	 */
	public static String getUnlockedLevelsPrefsKey(int world) {
		return "levelsunlocked_" + world;
	}
	
}
