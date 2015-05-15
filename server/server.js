var PARSE_APP_ID = 'PrEnANMldxheFLyqIlT7gfGYdD23UrgGKsArgBu4';
var PARSE_API_KEY = 'JoHlyxXXXTkucrE6vrwfjPvTzBxal3TsUP92R2az';

var express = require('express')
  , bodyparser = require('body-parser')
  , mongoose = require('mongoose')
  , request = require('request')
  ;

var app = express();
app.set('port', (process.env.PORT || 5000));
app.use(bodyparser.json());

var dbUri = process.env.DB_URI || process.env.MONGOLAB_URI || 'mongodb://localhost/parse-prototype';
mongoose.connect(dbUri);

var userSchema = mongoose.Schema({
  name: String,
  installation_id: String,
  device_type: String
});

app.get('/', function(req, res) {
  res.send('Hello!');
});

app.post('/users', function(req, res) {
  var user = mongoose.model('User', userSchema);
  user.findOneAndUpdate(
    {name: req.body.name},  // condition
    {                       // update
      name: req.body.name,
      installation_id: req.body.installation_id,
      device_type: req.body.device_type
    },
    {upsert: true, select: {name: 1}},  // options
    function(err, doc) {    // callback
      var response;
      if (err) {
        res.send(err);
        return;
      }

      if (doc) {
        response = {
          message: 'user updated',
          data: doc
        };
        res.send(response);
      }
      else {
        res.send({message: 'user created'});
      }
    }
  );
  
});

app.post('/poke', function(req, res) {
  var user = mongoose.model('User', userSchema);
  var from = req.body.from;
  var to = req.body.to;

  user.findOne(
    {name: to},  // condition
    function(err, doc) {  // callback
      var url = 'https://api.parse.com/1/push';
      var body,
          options;

      if (err) {
        res.send(err);
        return;
      }

      if (doc) {
        res.send('Received poke for ' + doc.name + '@' + doc.installation_id);

        body = {
          where: {
            installationId: doc.installation_id
          },
          data: {
            alert: 'You\'ve been poked by ' + from + '!'
          }
        };

        options = {
          url: url,
          headers: {
            'X-Parse-Application-Id': PARSE_APP_ID,
            'X-Parse-REST-API-Key': PARSE_API_KEY
          },
          json: true,
          body: body
        };
        console.log(options);

        request.post(options, function(err, httpResponse, body) {
          console.log(body);
        });
      }
      else {
        res.status(404).send('User ' + to + 'not found.');
      }
    }
  );
});

var server = app.listen(app.get('port'), function() {
  
  console.log('\n\n\tParse prototype server listening on port: %s\n\n', app.get('port'));

});
