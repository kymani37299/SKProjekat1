package com.mare.jovan;

/**
 * Process validation results which could be occurred
 */
public enum EProcessResult {
	/**
	 * Destination path is not valid
	 */
	DEST_NOT_VALID,
	/**
	 * Source path is not valid
	 */
	SOURCE_NOT_VALID,
	/**
	 * User does not have permission for requested operation
	 */
	DENIED_ACCESS,
	/**
	 * File with given extension is not valid
	 */
	EXTENSION_FORBIDDEN,
	/**
	 * Process failed
	 */
	PROCESS_FAILED,
	/**
	 * Process is successful
	 */
	PROCESS_SUCCESS,
	/**
	 * There is no registered user with given credentials
	 */
	USER_NOT_FOUND
}