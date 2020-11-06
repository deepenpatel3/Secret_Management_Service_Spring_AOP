package edu.sjsu.cmpe275.aop;

import java.io.IOException;
import java.util.UUID;

public interface SecretService {
	// Please do NOT change this file, and it is NOT part of your submission.

	/**
	 * Creates a secret in the service. A new Secret object is created, identified
	 * by randomly generated UUID, with the current user as the owner of the secret.
	 * 
	 * @param userId       the ID of the current user
	 * @param secretConent the content of the secret to be created. No duplication
	 *                     check is performed; i.e., one can create different secret
	 *                     objects with the same content.
	 * @throws IOException              if there is a network failure
	 * @throws IllegalArgumentException if the userId is null, or the secretContent
	 *                                  is null, or the secretContent is null,
	 *                                  empty, or more than 100 characters
	 * @return returns the ID for the newly created secret object
	 */
	UUID createSecret(String userId, String secretContent) throws IOException, IllegalArgumentException;

	/**
	 * Reads a secret by its ID. A user can read a secrete that he has created or
	 * has been shared with.
	 * 
	 * @param secretId the ID of the secret being requested
	 * 
	 * @throws IOException              if there is a network failure
	 * @throws IllegalArgumentException if any argument is null
	 * @throws NotAuthorizedException   if the given user neither has created the
	 *                                  secret nor has an authorized path to reach
	 *                                  the creator of the secret. If there does not
	 *                                  exist a secret with the given UUID, this
	 *                                  exception is thrown too.
	 * @return the content of the requested secret object
	 * 
	 */
	String readSecret(String userId, UUID secretId)
			throws IOException, IllegalArgumentException, NotAuthorizedException;

	/**
	 * Share a secret with another user. A user can share a secret that he has
	 * created or has been shared with. The creator of a secret cannot share the
	 * message with himself.
	 *
	 * @param userId       the ID of the current user
	 * @param secretId     the ID of the secret being shared
	 * @param targetUserId the ID of the user to share the secret with
	 * @throws IOException              if there is a network failure
	 * @throws IllegalArgumentException if any argument is null or a creator
	 *                                  attempts to share a message he created with
	 *                                  himself.
	 * @throws NotAuthorizedException   if the user with userId neither has created
	 *                                  or is currently shared with the given
	 *                                  secret. If there does not exist a secret
	 *                                  with the given UUID, this exception is
	 *                                  thrown too.
	 */

	void shareSecret(String userId, UUID secretId, String targetUserId)
			throws IOException, IllegalArgumentException, NotAuthorizedException;

	/**
	 * Unshare the current user's secret with another user. A user can ONLY unshare
	 * a secret with another person if he has shared the same secret with the same
	 * person. Please note that sharing and unsharing are used to control access
	 * through readSecret. For a message created by A, if A shares with B and C, B
	 * shares with D, and D shares with E, B unshares with D, then E cannot read the
	 * secret. But if C shares with D, then E can read the secret again.
	 * 
	 * @param userId       the ID of the current user
	 * @param secretId     the ID of the secret being unshared
	 * @param targetUserId the ID of the user to unshare the secret with
	 * @throws IOException              if there is a network failure
	 * @throws IllegalArgumentException if any argument is null
	 * @throws NotAuthorizedException   if the user with userId has not successfully
	 *                                  shared the given secret with the
	 *                                  targetUserId, or the sharing has already
	 *                                  been unshared. If there does not exist a
	 *                                  secret with the given UUID, this exception
	 *                                  is thrown too.
	 */

	void unshareSecret(String userId, UUID secretId, String targetUserId)
			throws IOException, IllegalArgumentException, NotAuthorizedException;

}
