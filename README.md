Server Api

To start server you must specify an existing directory for uploaded images in application.properties

Activity of server:

1. URL: "/users":

  1.1 GET request returns all users from database, format JSON array.
  GET can be with optional parameters:
   * online=[true|false], the list of users in response will be sorted by current status of users.
   * id=%d, it's a timestamp of request, the list of users in response will contain only users whom status was changed 
     after this timestamp

  1.2 POST request can create new entity in database. it's should contains a new User in JSON format.
  required fields: email(String) and name(String)
  not required fields: url(String), status_timestamp(number), online (boolean)
  field id. should be id=0 or absent in this request.
  This request returns id of new User
   
2. URL "/users/{userId}"

  2.1 GET request returns user with id = {userId}, format JSON entity

  2.2. PUT request can change status of User.
  Parameter: online=[true|false]
  returns JSON entity with following field:
  id - user's id
  oldStatus - old status of user
  currentStatus - new status of user