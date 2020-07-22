package com.shizy.ftd.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shizy.ftd.util.StringUtils;
import org.bson.Document;
import org.junit.Test;

import java.util.*;

public class MongoUtils {

    /************************************************************************/

    private static MongoClient mongoClient;

    private static String defaultDatabaseName;


    public static void initMongoClient(String host, int port, String username, String password, String databaseName) {

        if (StringUtils.isBlack(username) || StringUtils.isBlack(password) || StringUtils.isBlack(databaseName)) {
            initMongoClient(host, port);
            return;
        }

        MongoCredential credential = MongoCredential.createCredential(username, databaseName, password.toCharArray());
        mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(credential));

        defaultDatabaseName = databaseName;
    }

    public static void initMongoClient(String host, int port) {
        mongoClient = new MongoClient(host, port);
    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    /************************************************************************/

    public static void insertListMap(List<Map> list, String collectionName) {
        insertListMap(list, collectionName, defaultDatabaseName);
    }

    public static void insertListMap(List<Map> list, String collectionName, String databaseName) {
        List<Document> documents = new ArrayList<>();

        for (Map map : list) {
            documents.add(new Document(map));
        }

        insertListDocument(documents, collectionName, databaseName);
    }

    public static void insertListDocument(List<Document> documents, String collectionName, String databaseName) {

        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName == null ? defaultDatabaseName : databaseName);

        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);

        collection.insertMany(documents);
    }

    public static void main(String[] args) {
//        try {
//            // 连接到 mongodb 服务
//            MongoClient mongoClient = new MongoClient("localhost", 27017);
//
//            // 连接到数据库
//            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
//
//            //创建集合
////            mongoDatabase.createCollection("test");
//
//            //获取集合
//            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
//
//
//            //插入文档
//            Document document = new Document("title", "MongoDB").
//                    append("description", "database").
//                    append("likes", 100).
//                    append("by", "Fly");
//            List<Document> documents = new ArrayList<Document>();
//
//            documents.add(document);
//            collection.insertMany(documents);
//
//
//            //检索所有文档
////            FindIterable<Document> findIterable = collection.find();
////            MongoCursor<Document> mongoCursor = findIterable.iterator();
////            while (mongoCursor.hasNext()) {
////                System.out.println(mongoCursor.next());
////            }
//
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//        }
    }

    /************************************************************************/

    @Test
    public void test() {
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("testKey", "testValue");
        list.add(map);

        MongoUtils.insertListMap(list, "collectionTest2", "databaseTest");

    }


}




























