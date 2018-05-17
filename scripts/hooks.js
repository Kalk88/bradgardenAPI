const hooks = require('hooks');

//let stash = {};

/*
hooks.after("Members > Collection Members > Add Members", function (transaction, done) {
  stash.memberId = transaction.real.body;
    hooks.log("before add members " + stash.memberId);
  done();
});

hooks.beforeValidation("Members > Collection Members > Add Members", function (transaction, done) {
  hooks.log("before validation of add members");
  stash.memberId
   transaction.request.body = stash.memberId;
  done();
});



hooks.before("Members > Collection Members > Get Members", function (transaction) {
  transaction.skip = true;
});

hooks.before("Members > Single Member > Get a Member", function (transaction) {
  transaction.skip = true;
});

hooks.before("Members > Single Member > Remove Member", function (transaction) {
  transaction.skip = true;
});


hooks.before("Games > Collection Games> Add Game", function (transaction) {
  transaction.skip = true;
});


hooks.beforeAll(function (transactions, done) {
  hooks.log('before all');
  done();
});
hooks.beforeEach(function (transaction , done) {
  hooks.log('before each');
  done();
});

hooks.beforeEachValidation(function (transaction) {
  hooks.log('before each validation');
  done();
});

hooks.after("Endpoints > Get Endpoints > Get Endpoints", function (transaction, done) {
  hooks.log("after");
  done();
});
/*
hooks.afterEach(function (transaction, done) {
  hooks.log('after each');
  done();
});
*/

