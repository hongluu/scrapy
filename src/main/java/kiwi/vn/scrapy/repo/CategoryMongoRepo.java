package kiwi.vn.scrapy.repo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class CategoryMongoRepo {
	public static void main(String[] args) {

	}

	private DBCollection table;

	private DBCollection getTable() {
		if (table == null) {
			@SuppressWarnings("resource")
			MongoClient mongoClient = new MongoClient();
			@SuppressWarnings("deprecation")
			DB db = mongoClient.getDB("compare-price");
			return db.getCollection("categories");
		}
		return table;
	}

	public void insert(String json) {
		DBCollection table = getTable();
		DBObject dbObject = (DBObject) JSON.parse(json);
		table.insert(dbObject);
	}
}
