package in.co.madhur.dashclockfeedlyextension.db;

import in.co.madhur.dashclockfeedlyextension.db.FeedlyContract.Categories;
import in.co.madhur.dashclockfeedlyextension.db.FeedlyContract.FeedlyUser;
import in.co.madhur.dashclockfeedlyextension.db.FeedlyContract.SubscriptionCategory;
import in.co.madhur.dashclockfeedlyextension.db.FeedlyContract.Subscriptions;
import in.co.madhur.dashclockfeedlyextension.db.FeedlyContract.UnreadCounts;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static in.co.madhur.dashclockfeedlyextension.db.DBConsts.*;

public class FeedlyDB extends SQLiteOpenHelper
{

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "feedly.db";

	

	private static final String SQL_CREATE_FEEDLYUSER = "CREATE TABLE " + "%s"
			+ " (" + FeedlyUser._ID + " TEXT PRIMARY KEY," + FeedlyUser.EMAIL
			+ TEXT_TYPE + " )";



	private static final String SQL_CREATE_Categories = "CREATE TABLE " + "%s"
			+ " ("
			+ Categories._ID
			+ " TEXT PRIMARY KEY,"
			+ Categories.LABEL
			+  TEXT_TYPE +
			" )";
	
	private static final String SQL_CREATE_Subscriptions = "CREATE TABLE " + "%s"
			+ " ("
			+ Subscriptions._ID
			+ " TEXT PRIMARY KEY,"
			+ Subscriptions.TITLE
			+  TEXT_TYPE + COMMA_SEP
			+ Subscriptions.WEBSITE
			+ TEXT_TYPE + COMMA_SEP
			+ Subscriptions.UPDATED
			+ TEXT_TYPE+
			" )";
	
	private static final String SQL_CREATE_SubscriptionCategory = "CREATE TABLE " + "%s"
			+ " ("
			+ SubscriptionCategory._ID
			+ INTEGER_TYPE +  " PRIMARY KEY AUTOINCREMENT ,"
			+ SubscriptionCategory.SUBSCRIPTION_ID
			+  TEXT_TYPE + COMMA_SEP
			+ SubscriptionCategory.CATEGORY_ID
			+ TEXT_TYPE +
			" )";
	
	private static final String SQL_CREATE_UnreadCounts = "CREATE TABLE " + "%s"
			+ " ("
			+ UnreadCounts.SUBSCRIPTION_OR_CATEGORY_ID
			+  TEXT_TYPE + COMMA_SEP
			+ UnreadCounts.COUNT
			+ INTEGER_TYPE + COMMA_SEP
			+ UnreadCounts.UPDATED + TEXT_TYPE +
			" )";


	

	private static final String SQL_DELETE_ENTRY_FEEDLYUSER = "DROP TABLE IF EXISTS "
			+ FeedlyUser.TABLE_NAME;
	
	private static final String SQL_DELETE_ENTRY_Categories = "DROP TABLE IF EXISTS "
			+ Categories.TABLE_NAME;
	
	private static final String SQL_DELETE_ENTRY_Subscriptions = "DROP TABLE IF EXISTS "
			+ Subscriptions.TABLE_NAME;
	
	private static final String SQL_DELETE_ENTRY_SubscriptionCategory = "DROP TABLE IF EXISTS "
			+ SubscriptionCategory.TABLE_NAME;
	
	private static final String SQL_DELETE_ENTRY_UnreadCounts = "DROP TABLE IF EXISTS "
			+ UnreadCounts.TABLE_NAME;

	

	

	public FeedlyDB(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static String[] GetDeleteEntries()
	{

		return new String[] { SQL_DELETE_ENTRY_FEEDLYUSER,
				 };
	}

	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(String.format(SQL_CREATE_FEEDLYUSER, FeedlyUser.TABLE_NAME));
		db.execSQL(String.format(SQL_CREATE_Categories, Categories.TABLE_NAME));
		db.execSQL(String.format(SQL_CREATE_Subscriptions, Subscriptions.TABLE_NAME));
		db.execSQL(String.format(SQL_CREATE_SubscriptionCategory, SubscriptionCategory.TABLE_NAME));
		db.execSQL(String.format(SQL_CREATE_UnreadCounts, UnreadCounts.TABLE_NAME));
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

		db.execSQL(SQL_DELETE_ENTRY_FEEDLYUSER);
		db.execSQL(SQL_DELETE_ENTRY_Categories);
		db.execSQL(SQL_DELETE_ENTRY_Subscriptions);
		db.execSQL(SQL_DELETE_ENTRY_SubscriptionCategory);
		db.execSQL(SQL_DELETE_ENTRY_UnreadCounts);
		
		
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		onUpgrade(db, oldVersion, newVersion);
	}
}
