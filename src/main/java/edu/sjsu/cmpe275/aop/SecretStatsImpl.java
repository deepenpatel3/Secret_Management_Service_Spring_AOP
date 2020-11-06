package edu.sjsu.cmpe275.aop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.UUID;

public class SecretStatsImpl implements SecretStats {
    
	Map<UUID, String> secrets = new HashMap<UUID, String>();
	
	Map<String, ArrayList<Integer>> secretOccurences = new HashMap<>(); 
	
	Map<UUID, HashSet<String>> readOccurences = new HashMap<>();

	HashMap<UUID, LinkedHashMap<String, HashSet<String>>> SecretConnectionChain = new HashMap<>();
	
	public void addSecretToConnection(String userId, UUID secretId, String secretContent) throws IOException {

		secrets.put(secretId, secretContent);
		LinkedHashMap< String, HashSet<String>> conn = new  LinkedHashMap< String, HashSet<String>>();
		conn.put(userId, new HashSet<String>());
		
		SecretConnectionChain.put(secretId, conn);
	}
	
	public void UpdateReadStats(String userId,UUID secretId) throws IOException {
		
		if(!userId.equals(SecretConnectionChain.get(secretId).entrySet().iterator().next().getKey())) {
			HashSet<String> peopleWhoRead = readOccurences.getOrDefault(secretId, new HashSet<String>());
			if(!peopleWhoRead.contains(userId)) {
				//add the userId
				peopleWhoRead.add(userId);
				readOccurences.put(secretId, peopleWhoRead);
			}
		}
		
	}
	
	public void UpdateConnectionAfterSharing(String userId, UUID secretId, String targetUserId) throws IOException {

		LinkedHashMap< String, HashSet<String>> conn = SecretConnectionChain.get(secretId);
		
		
		if (conn.containsKey(userId)) {

			if(!conn.get(userId).contains(targetUserId)) {
				conn.get(userId).add(targetUserId);
				updateSharingOccurences(userId, targetUserId);
			} 
		}else {
			conn.put(userId, new HashSet<String>() {{ add(targetUserId);}});
			updateSharingOccurences(userId, targetUserId);
		}
	}
	
	public void updateSharingOccurences(String userId, String targetUserId) throws IOException {
		
		if(secretOccurences.containsKey(targetUserId)) {

			Integer sharedWith = secretOccurences.get(targetUserId).get(0);
			Integer shared = secretOccurences.get(targetUserId).get(1);

			secretOccurences.put(targetUserId, new ArrayList<>(Arrays.asList(++sharedWith,shared)));

		}else {

			ArrayList<Integer> sharingOccurences = new ArrayList<>(Arrays.asList(1,0));

			secretOccurences.put(targetUserId, sharingOccurences);

		}
		
		if(secretOccurences.containsKey(userId)) {

			Integer sharedWith = secretOccurences.get(userId).get(0);
			Integer shared = secretOccurences.get(userId).get(1);

			secretOccurences.put(userId, new ArrayList<>(Arrays.asList(sharedWith,++shared)));

		}else {
			ArrayList<Integer> sharingOccurences = new ArrayList<>(Arrays.asList(0,1));
			secretOccurences.put(userId, sharingOccurences);
		}
	}
	
	public void UpdateConnectionAfterUnsharing(String userId, UUID secretId, String targetUserId) throws IOException {
		LinkedHashMap< String, HashSet<String>> conn = SecretConnectionChain.get(secretId);
		
		conn.get(userId).remove(targetUserId);
	}
	
	public Boolean CheckForReadAccess(String userId, UUID secretId) throws IOException {

		if(!secrets.containsKey(secretId)) {
			return false;
		}
	
		LinkedHashMap< String, HashSet<String>> conn = SecretConnectionChain.get(secretId);
		Map.Entry<String, HashSet<String>> creator = SecretConnectionChain.get(secretId).entrySet().iterator().next();

		String startPoint = creator.getKey();
		
		if(startPoint.equals(userId)) {
			return true;
		}
		HashSet<String> visited = new HashSet<>();
		
		Queue<String> q  = new LinkedList<>(); 
		q.add(startPoint);
		while(!q.isEmpty()) {
			HashSet<String> sharedPeople = conn.get(q.peek());

			// added neighbours
			if(sharedPeople != null) {
				for(String person: sharedPeople) {

					if(person == userId)
						return true;
					if(visited.contains(person))
						continue;
					
					q.add(person);
				}
			}
			visited.add(q.peek());
			// removed head
			q.remove();
		}
		return false;
	}
	
	public Boolean CheckForUnshareAccess(String userId, UUID secretId, String targetUserId) throws IOException {
		
		if(!secrets.containsKey(secretId)) {
			return false;
		}
		
		LinkedHashMap< String, HashSet<String>> conn = SecretConnectionChain.get(secretId);
		
		if(!conn.containsKey(userId)) {
			return false;
		}else {
			if(!conn.get(userId).contains(targetUserId)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void resetStatsAndSystem() {
		
		secrets = new HashMap<UUID, String>();
		
		secretOccurences = new HashMap<String, ArrayList<Integer>>(); 
		
		readOccurences = new HashMap<UUID, HashSet<String>>();

		SecretConnectionChain = new HashMap<UUID, LinkedHashMap<String, HashSet<String>>>();
	}

	@Override
	public int getLengthOfLongestSecret() {
		int max = 0;
		for(Entry<UUID, String> e: secrets.entrySet()) {
			int length = e.getValue().length();
			if( length > max) {
				max = length;
			}
		}
		return max;
	}

	@Override
	public String getMostTrustedUser() {
		Integer max = Integer.MIN_VALUE;
		String User = new String("");
		for(Entry<String, ArrayList<Integer>> e: secretOccurences.entrySet()) {
			Integer netSharing = e.getValue().get(0);

			if(netSharing > max) {
				max = netSharing;
				User = e.getKey();
			}
			if(netSharing == max && e.getKey().compareToIgnoreCase(User) < 0) {
				User = e.getKey();
			}
		}
		if(User.equals("")) { 
			return null;
		}
		return User;
	}

	@Override
	public String getWorstSecretKeeper() {
		Integer min = Integer.MAX_VALUE;
		String User = new String("");
		for(Entry<String, ArrayList<Integer>> e: secretOccurences.entrySet()) {
			Integer netSharing = e.getValue().get(0)-e.getValue().get(1);
			if(netSharing < min) {
				min = netSharing;
				User = e.getKey();
			}
			if(netSharing == min && e.getKey().compareToIgnoreCase(User) < 0) {
				User = e.getKey();
			}
		}
		if(User.equals("")) { 
			return null;
		}
		return User;
	}

	@Override
	public String getBestKnownSecret() {
		int max = 0;
		UUID mostReadSecret = null;
		for(Entry<UUID, HashSet<String>> e: readOccurences.entrySet()) {
			int numberOfReaders = e.getValue().size();
			if(numberOfReaders > max) {
				max = numberOfReaders;
				mostReadSecret = e.getKey();
			}
			if(numberOfReaders == max && secrets.get(e.getKey()).compareToIgnoreCase(secrets.get(mostReadSecret)) < 0) {
				mostReadSecret = e.getKey();
			}
		}
		if(max == 0) {
			return null;
		}
		return secrets.get(mostReadSecret);
	}
    
}

