import _ from "lodash"

export const handler = async (event) => {
  
  var users = [
  { 'user': 'Carlos',  'active': true },
  { 'user': 'Gil-dong',    'active': false },
  { 'user': 'Pat', 'active': false }
  ];
   
  let out = _.findLastIndex(users, function(o) { return o.user == 'Pat'; });
  const response = {
    statusCode: 200,
    body: JSON.stringify(out + ", " + users[out].user),
  };
  return response;
};
