package in.co.madhur.dashclockfeedlyextension.db;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.Marker;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.api.Profile;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import in.co.madhur.dashclockfeedlyextension.db.FeedlyContract.Categories;
import in.co.madhur.dashclockfeedlyextension.db.FeedlyContract.FeedlyUser;
import in.co.madhur.dashclockfeedlyextension.db.FeedlyContract.SubscriptionCategory;
import in.co.madhur.dashclockfeedlyextension.db.FeedlyContract.Subscriptions;
import in.co.madhur.dashclockfeedlyextension.db.FeedlyContract.UnreadCounts;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DbHelper
{
	private static Context context;
	private static FeedlyDB db;
	private static DbHelper dbHelper;

	public static synchronized DbHelper getInstance(Context context)
	{
		if (dbHelper == null)
		{
			DbHelper.context = context;
			db = new FeedlyDB(context);
			dbHelper = new DbHelper();
			return dbHelper;
		}
		else
			return dbHelper;

	}

	public void WriteCategories(List<Category> categories) throws Exception
	{
		SQLiteDatabase database = db.getWritableDatabase();

		try

		{
			String sql = "INSERT OR REPLACE INTO "
					+ FeedlyContract.Categories.TABLE_NAME + "("
					+ Categories._ID + "," + Categories.LABEL + ") "
					+ " VALUES (?, ?)";

			SQLiteStatement statement = database.compileStatement(sql);
			database.beginTransaction();

			for (int i = 0; i < categories.size(); i++)
			{
				statement.clearBindings();

				statement.bindString(1, categories.get(i).getId());

				statement.bindString(2, categories.get(i).getLabel());

				statement.execute();
			}

			database.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{

			database.endTransaction();
			database.close();
		}

	}

	public void WriteProfile(Profile profile) throws Exception
	{

		SQLiteDatabase database = db.getWritableDatabase();

		try

		{
			String sql = "INSERT OR REPLACE INTO "
					+ FeedlyContract.FeedlyUser.TABLE_NAME + "("
					+ FeedlyUser._ID + "," + FeedlyUser.EMAIL + ") "
					+ " VALUES (?, ?)";

			SQLiteStatement statement = database.compileStatement(sql);
			database.beginTransaction();

			statement.clearBindings();

			statement.bindString(1, profile.getId());

			statement.bindString(2, profile.getEmail());

			statement.execute();

			database.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			database.endTransaction();
			database.close();
		}

	}

	public void WriteSubscriptions(List<Subscription> subscriptions)
			throws Exception
	{

		SQLiteDatabase database = db.getWritableDatabase();

		try

		{
			String sql = "INSERT OR REPLACE INTO " + Subscriptions.TABLE_NAME
					+ "(" + Subscriptions._ID + "," + Subscriptions.TITLE + ","
					+ Subscriptions.WEBSITE + "," + Subscriptions.UPDATED
					+ ") " + " VALUES (?, ? , ? , ?)";

			SQLiteStatement statement = database.compileStatement(sql);
			database.beginTransaction();

			for (int i = 0; i < subscriptions.size(); i++)
			{
				statement.clearBindings();

				statement.bindString(1, subscriptions.get(i).getId());

				statement.bindString(2, subscriptions.get(i).getTitle());

				statement.bindString(3, subscriptions.get(i).getWebsite());

				statement.bindLong(4, subscriptions.get(i).getUpdated());

				AddSubscriptionCategory(subscriptions.get(i).getId(), subscriptions.get(i).getCategories());

				statement.execute();
			}

			database.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{

			database.endTransaction();
			database.close();
		}

	}

	private void AddSubscriptionCategory(String subscriptionId, List<Category> categories)
	{
		SQLiteDatabase database = db.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (Category category : categories)
		{
			values.put(SubscriptionCategory.SUBSCRIPTION_ID, subscriptionId);
			values.put(SubscriptionCategory.CATEGORY_ID, category.getId());
		}

		if (categories.size() > 0)
			database.insert(SubscriptionCategory.TABLE_NAME, null, values);

	}

	public void WriteMarkers(Markers markers) throws Exception
	{
		SQLiteDatabase database = db.getWritableDatabase();

		try

		{
			String sql = "INSERT OR REPLACE INTO " + UnreadCounts.TABLE_NAME
					+ "(" + UnreadCounts._ID + "," + UnreadCounts.COUNT + ","
					+ UnreadCounts.UPDATED + ") " + " VALUES (?, ?  , ?)";

			SQLiteStatement statement = database.compileStatement(sql);
			database.beginTransaction();

			List<Marker> listMarkers = markers.getUnreadcounts();
			for (int i = 0; i < markers.getUnreadcounts().size(); i++)
			{
				statement.clearBindings();

				statement.bindString(1, listMarkers.get(i).getId());

				statement.bindLong(2, listMarkers.get(i).getCount());

				statement.bindLong(3, listMarkers.get(i).getUpdated());

				statement.execute();
			}

			database.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{

			database.endTransaction();
			database.close();
		}

	}

	public Profile GetProfile()
	{
		SQLiteDatabase database = db.getReadableDatabase();

		Cursor c = database.query(FeedlyUser.TABLE_NAME, // The table to
				// query
				null, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
		);

		if (c.moveToFirst())
		{
			Profile profile = new Profile();
			profile.setId(c.getString(c.getColumnIndexOrThrow(FeedlyUser._ID)));
			profile.setEmail(c.getString(c.getColumnIndexOrThrow(FeedlyUser.EMAIL)));

			return profile;

		}
		
		c.close();

		return null;
	}

	public List<Category> GetCategories()
	{
		SQLiteDatabase database = db.getReadableDatabase();
		List<Category> categories = new ArrayList<Category>();

		Cursor c = database.query(Categories.TABLE_NAME, // The table to
				// query
				null, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
		);

		if (c.moveToFirst())
		{
			do
			{
				Category category = new Category();
				category.setId(c.getString(c.getColumnIndexOrThrow(Categories._ID)));
				category.setLabel(c.getString(c.getColumnIndexOrThrow(Categories.LABEL)));

				categories.add(category);
			}
			while (c.moveToNext());
		}
		
		c.close();

		return categories;

	}

	public List<Subscription> GetSubscriptions()
	{
		SQLiteDatabase database = db.getReadableDatabase();
		List<Subscription> subscriptions = new ArrayList<Subscription>();

		Cursor c = database.query(Subscriptions.TABLE_NAME, // The table to
				// query
				null, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
		);

		if (c.moveToFirst())
		{
			do
			{
				Subscription subscription = new Subscription();
				subscription.setId(c.getString(c.getColumnIndexOrThrow(Subscriptions._ID)));
				subscription.setTitle(c.getString(c.getColumnIndexOrThrow(Subscriptions.TITLE)));
				subscription.setWebsite(c.getString(c.getColumnIndexOrThrow(Subscriptions.WEBSITE)));
				subscription.setUpdated(c.getLong(c.getColumnIndexOrThrow(Subscriptions.UPDATED)));
				// subscription.setVisualUrl(c.getString(c.getColumnIndexOrThrow(Subscriptions.)));

				subscriptions.add(subscription);
			}
			while (c.moveToNext());
		}
		
		c.close();

		return subscriptions;
	}

	public Markers GetUnreadCounts()
	{
		SQLiteDatabase database = db.getReadableDatabase();
		Markers markers = new Markers();
		List<Marker> listMarkers = new ArrayList<Marker>();

		Cursor c = database.query(UnreadCounts.TABLE_NAME, // The table to
				// query
				null, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
		);

		if (c.moveToFirst())
		{
			do
			{
				Marker marker = new Marker();
				marker.setCount(c.getInt(c.getColumnIndexOrThrow(UnreadCounts.COUNT)));
				marker.setId(c.getString(c.getColumnIndexOrThrow(UnreadCounts._ID)));
				marker.setUpdated(c.getLong(c.getColumnIndexOrThrow(UnreadCounts.UPDATED)));

				listMarkers.add(marker);
			}
			while (c.moveToNext());
		}
		
		c.close();

		markers.setUnreadcounts(listMarkers);

		return markers;
	}

	public List<Subscription> GetSubScriptionsForCategory(String categoryId)
	{
		SQLiteDatabase database = db.getReadableDatabase();
		List<Subscription> subscriptions = new ArrayList<Subscription>();

		Cursor c = database.query(SubscriptionCategory.TABLE_NAME, // The table
																	// to
				// query
				null, // The columns to return
				SubscriptionCategory.CATEGORY_ID + "=?", // The columns for the
															// WHERE clause
				new String[] { categoryId }, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
		);

		if (c.moveToFirst())
		{
			do
			{
				Subscription sub = GetSubscription(c.getString(c.getColumnIndexOrThrow(SubscriptionCategory.SUBSCRIPTION_ID)));
				if (sub != null)
					subscriptions.add(sub);
			}
			while (c.moveToNext());
		}
		
		c.close();

		return subscriptions;
	}

	private Subscription GetSubscription(String subscriptionId)
	{
		SQLiteDatabase database = db.getReadableDatabase();
		Subscription sub = new Subscription();
		Cursor c = database.query(Subscriptions.TABLE_NAME, // The table to
				// query
				null, // The columns to return
				Subscriptions._ID+"=?", // The columns for the WHERE clause
				new String[] { subscriptionId }, // The values for the WHERE
													// clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
		);

		if (c.moveToFirst())
		{

			sub.setId(c.getString(c.getColumnIndexOrThrow(Subscriptions._ID)));
			sub.setTitle(c.getString(c.getColumnIndexOrThrow(Subscriptions.TITLE)));
			sub.setUpdated(c.getLong(c.getColumnIndexOrThrow(Subscriptions.UPDATED)));
			sub.setWebsite(c.getString(c.getColumnIndexOrThrow(Subscriptions.WEBSITE)));
		}
		
		c.close();

		return sub;

	}

	public boolean IsFetchRequired()
	{
		if (GetSubscriptions().size() > 0)
			return false;

		return true;
	}

}
