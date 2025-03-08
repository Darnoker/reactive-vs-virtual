const { MongoClient } = require('mongodb');
const { generateFakeUser, generateFakeProduct, generateFakeOrder } = require('./data-generate');
require('dotenv').config();

const username = process.env.MONGO_INITDB_ROOT_USERNAME;
const password = process.env.MONGO_INITDB_ROOT_PASSWORD;
const uri = `mongodb://${username}:${password}@localhost:27017/testDB?authSource=admin`;

async function clearDatabase(db) {
  const collections = await db.collections();
  for (const collection of collections) {
    await collection.deleteMany({});
    console.log(`Wyczyszczono kolekcję ${collection.collectionName}`);
  }
}

async function putUsersBatch(totalCount = 10, batchSize = 1000, collection) {
  let allIds = [];
  for (let i = 0; i < totalCount; i += batchSize) {
    const currentBatchSize = Math.min(batchSize, totalCount - i);
    const users = [];
    for (let j = 0; j < currentBatchSize; j++) {
      users.push(generateFakeUser());
    }
    const result = await collection.insertMany(users);
    allIds = allIds.concat(Object.values(result.insertedIds));
    console.log(`Wstawiono użytkowników od ${i + 1} do ${i + currentBatchSize}`);
  }
  return allIds;
}

async function putProductsBatch(totalCount = 10, batchSize = 1000, collection) {
  let allIds = [];
  for (let i = 0; i < totalCount; i += batchSize) {
    const currentBatchSize = Math.min(batchSize, totalCount - i);
    const products = [];
    for (let j = 0; j < currentBatchSize; j++) {
      products.push(generateFakeProduct());
    }
    const result = await collection.insertMany(products);
    allIds = allIds.concat(Object.values(result.insertedIds));
    console.log(`Wstawiono produkty od ${i + 1} do ${i + currentBatchSize}`);
  }
  return allIds;
}

async function putOrdersBatch(totalCount = 10, batchSize = 1000, collection, userIds, productIds) {
  for (let i = 0; i < totalCount; i += batchSize) {
    const currentBatchSize = Math.min(batchSize, totalCount - i);
    const orders = [];
    for (let j = 0; j < currentBatchSize; j++) {
      orders.push(generateFakeOrder(userIds, productIds));
    }
    await collection.insertMany(orders);
    console.log(`Wstawiono zamówienia od ${i + 1} do ${i + currentBatchSize}`);
  }
}

async function run() {
  const client = new MongoClient(uri, { useUnifiedTopology: true });
  try {
    await client.connect();
    const db = client.db("testDB");

    await clearDatabase(db);
   
    const usersCollection = db.collection("users");
    const productsCollection = db.collection("products");
    const ordersCollection = db.collection("orders");

    const totalUsers = 1000000;
    const totalProducts = 10000;
    const totalOrders = 2000000;

    const usersBatchSize = 10000;
    const productsBatchSize = 1000;
    const ordersBatchSize = 10000;

    const userIds = await putUsersBatch(totalUsers, usersBatchSize, usersCollection);
    const productIds = await putProductsBatch(totalProducts, productsBatchSize, productsCollection);
    await putOrdersBatch(totalOrders, ordersBatchSize, ordersCollection, userIds, productIds);

    console.log("Dane z referencjami zostały wygenerowane");
  } catch (err) {
    console.error(err);
  } finally {
    await client.close();
  }
}

run().catch(console.dir);
