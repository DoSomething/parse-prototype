var express = require('express')
  , bodyparser = require('body-parser')
  , mongoose = require('mongoose')
  ;

var app = express();
app.use(bodyparser.json());

var dbUri = process.env.DB_URI || 'mongodb://localhost/parse-prototype';
mongoose.connect(dbUri);

var userSchema = mongoose.Schema({
  name: String,
  device_token: String
});

app.post('/users', function(req, res) {
  var user = mongoose.model('User', userSchema);
  user.findOneAndUpdate(
    {name: req.body.name},  // condition
    {name: req.body.name},  // update
    {upsert: true, select: {name: 1}},  // options
    function(err, doc) {    // callback
      var response;
      console.log(doc);
      if (doc) {
        response = {
          message: 'user created',
          data: doc
        };
        res.send(response);
      }
      else {
        res.send(err);
      }
    }
  );
  
});

app.post('/poke', function(req, res) {
  // @todo send notification to the user poked
  res.send('poke received');
});

var server = app.listen(3000, function() {
  var host = server.address().address;
  var port = server.address().port;

  console.log('\n\n\tParse prototype server listening at http://%s:%s\n\n', host, port);
});
