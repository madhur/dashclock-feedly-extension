package in.co.madhur.dashclockfeedlyextension.db;

import android.provider.BaseColumns;

public final class FeedlyContract
{

	public FeedlyContract()
	{
	}

	/* Inner class that defines the table contents */
	
	public static abstract class FeedlyUser implements BaseColumns
	{
		public static final String TABLE_NAME = "feedlyuser";
		
		public static final String EMAIL="email";
		
	}
	
	public static abstract class Categories implements BaseColumns
	{
		public static final String TABLE_NAME = "categories";
		
		public static final String LABEL="label";
		
	}
	
	public static abstract class Subscriptions implements BaseColumns
	{
		public static final String TABLE_NAME = "subscriptions";
		
		public static final String TITLE="title";
		public static final String WEBSITE="website";
		public static final String UPDATED="updated";
		
	}
	
	public static abstract class SubscriptionCategory implements BaseColumns
	{
		public static final String TABLE_NAME = "subscriptioncategory";
		public static final String SUBSCRIPTION_ID = "sub_id";
		public static final String CATEGORY_ID="category_id";
		
		
	}
	
	public static abstract class UnreadCounts implements BaseColumns
	{
		public static final String TABLE_NAME = "unreadcounts";
		//public static final String SUBSCRIPTION_OR_CATEGORY_ID = "sub_or_category_id";
		public static final String COUNT="count";
		public static final String UPDATED = "updated";
		
	}
	
	
	public static abstract class FeedCountView implements BaseColumns
	{
		public static final String VIEW_NAME = "markers";
		public static final String TITLE="title";
		public static final String COUNT="count";
		public static final String UPDATED = "updated";
		
	}

	

	
}
