const { MongoClient } = require('mongodb')
const MongoStore = require('connect-mongo')

const DATABASE = 'fpf'
const HOST = '127.0.0.1'

const PORT = process.env.MONGODB_PORT ? process.env.MONGODB_PORT : 24109
const SECRET = process.env.FPF_MONGODB_SECRET ? process.env.FPF_MONGODB_SECRET : 'test'

const USERNAME = process.env.FPF_MONGODB_USER ? process.env.FPF_MONGODB_USER : 'admin'
const PWD = process.env.FPF_MONGODB_PASSWORD ? process.env.FPF_MONGODB_PASSWORD : 'BestGpOfTheYr22'


let url = []

url.push('mongodb://', USERNAME, ':', PWD, '@', HOST, ':', PORT)
url = url.join('')


// 'mongodb://admin:BestGpOfTheYr22-23@127.0.0.1:24109'

module.exports = {
  url,
  client: new MongoClient(url),
  session: {
    store: MongoStore.create({
      mongoUrl: url,
      DATABASE,
      touchAfter: 24 * 3600,
      crypto: {
        SECRET
      }
    }),
    secret: SECRET,
    resave: false,
    saveUninitialized: true,
    cookie: {
      secure: false,
      sameSite: true
    }
  }
}
