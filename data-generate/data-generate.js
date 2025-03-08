const { faker } = require('@faker-js/faker');

function generateFakeUser() {
    const addresses = [];
    const addressCount = Math.floor(Math.random() * 3) + 1;
    for (let j = 0; j < addressCount; j++) {
      addresses.push({
        street: faker.location.streetAddress(),
        city: faker.location.city(),
        country: faker.location.country(),
        zipCode: faker.location.zipCode()
      });
    }

    const phoneNumbers = [];
    const phoneCount = Math.floor(Math.random() * 2) + 1;
    for (let k = 0; k < phoneCount; k++) {
      phoneNumbers.push(faker.phone.number());
    }

    const user = {
        name: faker.person.firstName(),
        lastname: faker.person.lastName(),
        email: faker.internet.email(),
        registeredAt: faker.date.past(),
        addresses: addresses,
        profile: {
          avatar: faker.image.avatar(),
          bio: faker.lorem.sentence()
        },
        phoneNumbers: phoneNumbers
      };
  
  return user;
}

function generateFakeProduct() {
    const tags = [];
    const tagCount = Math.floor(Math.random() * 3) + 1;
    for (let j = 0; j < tagCount; j++) {
      tags.push(faker.commerce.productMaterial());
    }

    const reviews = [];
    const reviewCount = Math.floor(Math.random() * 50);
    for (let k = 0; k < reviewCount; k++) {
      reviews.push({
        reviewer: faker.person.firstName() + " " + faker.person.lastName(),
        rating: Math.floor(Math.random() * 5) + 1,
        comment: faker.lorem.sentence(),
        reviewDate: faker.date.recent()
      });
    }

    const product = {
        name: faker.commerce.productName(),
        description: faker.commerce.productDescription(),
        price: parseFloat(faker.commerce.price()),
        category: {
          name: faker.commerce.department(),
          subCategory: faker.commerce.productAdjective()
        },
        tags: tags,
        reviews: reviews
      };
  
  return product;
}

function generateFakeOrder(userIds, productIds) {

    const randomUserId = userIds[Math.floor(Math.random() * userIds.length)];

    const orderProducts = [];
    const numProducts = Math.floor(Math.random() * 3) + 1;
    for (let j = 0; j < numProducts; j++) {
      const randomProductId = productIds[Math.floor(Math.random() * productIds.length)];
      orderProducts.push({
        productId: randomProductId,
        quantity: Math.floor(Math.random() * 5) + 1,
        details: {
          color: faker.color.human(),
          warranty: faker.helpers.arrayElement(['1 year', '2 years', 'No warranty'])
        }
      });
    }
    const order =  {
        userId: randomUserId,
        products: orderProducts,
        orderDate: faker.date.recent(),
        total: parseFloat(faker.commerce.price()),
        shipping: {
          address: {
            street: faker.location.streetAddress(),
            city: faker.location.city(),
            country: faker.location.country(),
            zipCode: faker.location.zipCode()
          },
          method: faker.helpers.arrayElement(['Standard', 'Express', 'Next-Day']),
          trackingNumber: faker.string.uuid()
        },
        payment: {
          method: faker.helpers.arrayElement(['Credit Card', 'PayPal', 'Bank Transfer']),
          transactionId: faker.string.uuid()
        }
      };

  return order;
}

module.exports = {
  generateFakeUser,
  generateFakeProduct,
  generateFakeOrder
};
