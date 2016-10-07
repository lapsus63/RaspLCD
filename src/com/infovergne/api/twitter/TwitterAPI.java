package com.infovergne.api.twitter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infovergne.api.APICommons;

/**
 * <p>Connection to Twitter API. Need to create an application and a token with your Twitter account.</p>
 * Need to configure the following data in your user.home/.rasplcd/apikeys.conf file :
 * <ul>
 * <li>TWITTER_CONSUMER_KEY</li>
 * <li>TWITTER_SECRET_KEY</li>
 * <li>TWITTER_ACCESS_TOKEN</li>
 * <li>TWITTER_ACCESS_TOKEN_SECRET</li>
 * </ul>
 * 
 * @author Olivier
 */
public class TwitterAPI {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(TwitterAPI.class);

	/** @return the last tweets for a given username */
	public static JsonElement findTweets(String username) {
		String consKey = APICommons.getApiKey("TWITTER_CONSUMER_KEY");
		String secretKey  = APICommons.getApiKey("TWITTER_SECRET_KEY");
		String accessToken = APICommons.getApiKey("TWITTER_ACCESS_TOKEN");
		String accessTokenSecret = APICommons.getApiKey("TWITTER_ACCESS_TOKEN_SECRET");
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consKey)
				.setOAuthConsumerSecret(secretKey)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		JsonArray jsonArray = new JsonArray();
		try {
			Query query = new Query(username);
			QueryResult result;
			do {
				result = twitter.search(query);
				List<Status> tweets = twitter.getHomeTimeline(new Paging(1, 4));
				for (Status tweet : tweets) {
					JsonObject jsonTweet = new JsonObject();
					jsonTweet.addProperty("username", tweet.getUser().getScreenName());
					jsonTweet.addProperty("text", tweet.getText());
					jsonArray.add(jsonTweet);
					LOGGER.info("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
				}
			} while ((query = result.nextQuery()) != null);
		} catch (TwitterException te) {
			LOGGER.error(te.getMessage(), te);
		}
		JsonObject jsonRoot = new JsonObject();
		jsonRoot.add("tweets", jsonArray);
		return jsonRoot;
	}

}
