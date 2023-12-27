# SUDOku

## Requirements

- Node.js (v18.15.0)
- npm (v9.6.1)
- mongodb (community edition v6.0.5)

## Getting Started
Make sure you are in the root directory of the project, and the mongod service is running. Then follow these instructions:

1. Install dependencies
```
npm install
```
2. Create a system user for accessing the database
```
mongosh --port <MONOGDB_PORT>
```
```
use admin
```
```
db.createUser(
  {
    user: "fpf-system", 
    pwd: passwordPrompt(), 
    roles: [
      { role:"readWrite", db:"fpf" }
    ]
  }
)
```
3. Create a copy of the example `.env` file. Filling with the mongodb `<PORT>`, and user's name and password from step `2`.
```
cp .env.example .env
```
4. Run the application
```
npm start
```
## Administration
To create the first administration account, you will need to update a users role in mongosh. This administrator can then update other users' roles.
1. Create an account on the site, keeping a note of the email
2. Update their role in mongosh
```
mongosh -u fpf-system --port <MONGODB_PORT>
```
```
db.users.updateOne({ "email":"<EMAIL>" }, { $set: { "role":"ADMIN" } }, { upsert: true })
```

## Troubleshooting
### Stopping the Backend
To stop the backend, make sure you are in the root directory of the project and run:
```
npm stop
```
### Logs
- Frontend logs are displayed in the cli when running the application
- Backend logs can be found in `./logs/backend.log`

### Updating database user password
```
mongosh --port <PORT>
```
```
use admin
```
```
db.updateUser(
   "fpf-system",
   {
     pwd: passwordPrompt(),
     mechanisms: [ "SCRAM-SHA-256" ]
   }
)
```
### Frontend `"undefined"` error on home page
This is usually caused by the backend being down. Check logs in `./logs/backend.logs`. If it says `"connection to database failed"`:
- You have not setup your database and environmental variables correctly (see. [Getting Started](#Getting-Started))

If there are no logs, it is most likely either:
- you have not ran `npm install`
- The port specified as `FPF_BACKEND_PORT` in `.env` is unavailable. Try choosing a different free port.


## Authors and acknowledgment
Authors: 200007413, 200007047, 200032853, 200036815, 190006961

We would like to extend thanks to our supervisor, Dr. Ruth Letham, for providing invaluable guidance, constant support, and timely feedback throughout the year. We would also like to thank Dr. Olexandr Konovalov for offering insightful lectures with Scrum and Agile methodology that guided us ahead. Additionally, we would like to appreciate Professor Ian Gent for organizing and coordinating the module that ensures it runs smoothly.